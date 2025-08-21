// Java
package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.jobs.JobManager;
import org.example.economy.commands.WorkCommand;
import org.example.economy.jobs.JobType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class Main {

    // Volitelně: nastav svůj token přímo sem (jinak používej ENV/VM/args/config):
    private static final String TOKEN_IN_CODE = "MTQwNDQxNzg2Nzk0ODIzMjc1NQ.GdBIMc.Fbuh9YZWjLSz_y0Mr_Y3pM2cS7god3JzCuTcp4";

    public static void main(String[] args) throws Exception {
        String token = resolveToken(args);

        if (token == null || token.isBlank() || "<VÁŠ_DISCORD_BOT_TOKEN>".equals(TOKEN_IN_CODE)) {
            System.err.println("Chyba: Discord token nebyl nalezen nebo je placeholder.");
            System.err.println("Možnosti:");
            System.err.println("  - Nastav TOKEN_IN_CODE ve třídě Main na tvůj token.");
            System.err.println("  - Nebo použij ENV: DISCORD_TOKEN=...");
            System.err.println("  - Nebo VM option: -Ddiscord.token=...");
            System.err.println("  - Nebo argument: --token=... / -t ... / args[0]");
            System.exit(1);
            return;
        }

        // Infrastruktura
        Database database = new Database();
        JobManager jobManager = new JobManager(database);

        // MIGRACE: všem existujícím profilům s UNEMPLOYED/null nastav MINER
        migrateExistingProfiles(database, jobManager);

        WorkCommand workCommand = new WorkCommand(database, jobManager);

        // JDA
        JDABuilder builder = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.playing("economy"));
        builder.addEventListeners(new SlashListener(workCommand));

        JDA jda = builder.build();
        jda.awaitReady();
        System.out.println("Bot je online jako: " + jda.getSelfUser().getAsTag());

        // Registrace slash commandů
        String guildId = System.getenv("GUILD_ID"); // volitelné pro rychlou registraci v jedné guildě
        if (guildId != null && !guildId.isBlank() && jda.getGuildById(guildId) != null) {
            jda.getGuildById(guildId)
               .updateCommands()
               .addCommands(Commands.slash("work", "Odpracuj směnu a získej odměnu a XP"))
               .queue(
                   s -> System.out.println("Guild slash commands zaregistrovány pro guildId=" + guildId),
                   e -> System.err.println("Registrace guild commands selhala: " + e.getMessage())
               );
        } else {
            jda.updateCommands()
               .addCommands(Commands.slash("work", "Odpracuj směnu a získej odměnu a XP"))
               .queue(
                   s -> System.out.println("Globální slash commands zaregistrovány"),
                   e -> System.err.println("Registrace global commands selhala: " + e.getMessage())
               );
        }

        // Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Ukládám databázi a vypínám bota...");
                database.save();
                jda.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    // Listener pro /work
    private static class SlashListener extends ListenerAdapter {
        private final WorkCommand workCommand;

        public SlashListener(WorkCommand workCommand) {
            this.workCommand = workCommand;
        }

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if ("work".equalsIgnoreCase(event.getName())) {
                try {
                    workCommand.execute(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    event.reply("Nastala neočekávaná chyba při zpracování příkazu.").setEphemeral(true).queue();
                }
            }
        }
    }

    // Token resolver: 1) TOKEN_IN_CODE -> 2) ENV -> 3) VM prop -> 4) config.properties -> 5) args
    private static String resolveToken(String[] args) {
        if (TOKEN_IN_CODE != null && !TOKEN_IN_CODE.isBlank() && !"<VÁŠ_DISCORD_BOT_TOKEN>".equals(TOKEN_IN_CODE)) {
            return TOKEN_IN_CODE;
        }
        String env = System.getenv("DISCORD_TOKEN");
        if (env != null && !env.isBlank()) return env;

        String prop = System.getProperty("discord.token");
        if (prop != null && !prop.isBlank()) return prop;

        try {
            Path cfg = Path.of("config.properties");
            if (Files.exists(cfg)) {
                try (InputStream in = Files.newInputStream(cfg)) {
                    Properties p = new Properties();
                    p.load(in);
                    String cfgToken = p.getProperty("discord.token");
                    if (cfgToken != null && !cfgToken.isBlank()) return cfgToken;
                }
            }
        } catch (IOException ignored) {}

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String a = args[i];
                if (a.startsWith("--token=")) return a.substring("--token=".length());
                if ("-t".equals(a) && i + 1 < args.length) return args[i + 1];
            }
            if (!args[0].isBlank()) return args[0];
        }
        return null;
    }

    // MIGRACE EXISTUJÍCÍCH PROFILŮ:
    // Bez ohledu na to, jak Database uchovává data, pokusíme se najít kolekci uživatelů přes reflexi.
    // - Pokud najde Collection<UserData>, projde ji.
    // - Pokud najde Map<*, UserData>, projde values().
    // Každému uživateli s job==null nebo UNEMPLOYED nastaví MINER a uloží změny.
    @SuppressWarnings("unchecked")
    private static void migrateExistingProfiles(Database database, JobManager jobManager) {
        int migrated = 0;

        try {
            // Zkus najít metody, které by mohly vracet uživatele
            Method[] methods = database.getClass().getMethods();
            for (Method m : methods) {
                if (m.getParameterCount() == 0) {
                    String name = m.getName().toLowerCase();
                    boolean maybeUsers = name.contains("all") || name.contains("users") || name.contains("values") || name.contains("list");
                    if (!maybeUsers) continue;

                    Object result = null;
                    try {
                        result = m.invoke(database);
                    } catch (Throwable ignored) {
                        continue;
                    }
                    if (result == null) continue;

                    // Collection<UserData>
                    if (result instanceof Collection<?> col) {
                        for (Object o : col) {
                            if (o instanceof UserData ud) {
                                if (fixJobIfUnemployed(ud)) {
                                    migrated++;
                                }
                            }
                        }
                        continue; // zkusí ještě další metody, ale už máme část migrováno
                    }

                    // Map<?, UserData> -> values()
                    if (result instanceof Map<?, ?> map) {
                        for (Object v : map.values()) {
                            if (v instanceof UserData ud) {
                                if (fixJobIfUnemployed(ud)) {
                                    migrated++;
                                }
                            }
                        }
                        continue;
                    }
                }
            }
        } catch (Throwable t) {
            System.err.println("Migrace profilů: došlo k chybě: " + t.getMessage());
        }

        if (migrated > 0) {
            try {
                database.save();
            } catch (Throwable t) {
                System.err.println("Ukládání po migraci selhalo: " + t.getMessage());
            }
        }
        System.out.println("Migrace profilů dokončena. Upraveno uživatelů: " + migrated);
    }

    // Pomocná metoda: pokud je job null/UNEMPLOYED, nastav MINER a vrať true (byla provedena změna)
    private static boolean fixJobIfUnemployed(UserData userData) {
        try {
            var job = userData.getJob();
            if (job == null || job == JobType.UNEMPLOYED) {
                userData.setJob(JobType.MINER);
                return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }
}
