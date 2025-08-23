package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import org.example.commands.RoleUpdate;

public class Main {

    // Vložte svůj token sem (necommitujte do veřejného repa):
    private static final String TOKEN = "MTQwNDQxNzg2Nzk0ODIzMjc1NQ.G1DvCW.caU_YfnixlZ3McnziuOeJV5uMrhU-GQLShPfNI";

    public static void main(String[] args) throws Exception {
        if (TOKEN == null || TOKEN.isBlank() || "<DISCORD_BOT_TOKEN>".equals(TOKEN)) {
            System.err.println("Chyba: Token není nastaven. Upravte konstantu TOKEN v Main.java.");
            System.exit(1);
            return;
        }


        JDABuilder builder = JDABuilder.createDefault(TOKEN)
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,     // PRO ROLE UPDATES
                        GatewayIntent.MESSAGE_CONTENT,   // PRO PŘÍKAZY
                        GatewayIntent.GUILD_PRESENCES    // PRO LEPŠÍ DETEKCI UŽIVATELŮ
                )
                .setActivity(Activity.playing("economy"));

        // Přidejte oba listenery
        builder.addEventListeners(new CommandManager(), new RoleUpdate());

        JDA jda = builder.build();
        jda.awaitReady();
        System.out.println("Bot je online jako: " + jda.getSelfUser().getAsTag());

        // MODE: GUILD-ONLY registrace /e (okamžité) + smazání globální /e, aby se nezdvojovalo

        var econ = createEconomyCommand();

        // 1) Smaž globální /e (pokud někdy existoval) – zabrání duplicitám
        jda.retrieveCommands().queue(cmds -> cmds.stream()
                .filter(c -> "e".equalsIgnoreCase(c.getName()))
                .forEach(c -> c.delete().queue(
                        v -> System.out.println("Smazán globální /e"),
                        e -> System.err.println("Mazání globálního /e selhalo: " + e.getMessage())
                ))
        );

        // 2) Upsert /e pro všechny guildy (okamžité)
        jda.getGuilds().forEach(g ->
                g.upsertCommand(econ).queue(
                        v -> System.out.println("Upsert /e pro " + g.getName()),
                        e -> System.err.println("Upsert /e selhal pro " + g.getName() + ": " + e.getMessage())
                )
        );

        // 3) Volitelně: pro jistotu smaž případné staré top-level /work na gildách (pokud se dřív registroval)
        jda.getGuilds().forEach(g ->
                g.retrieveCommands().queue(gcmds ->
                        gcmds.stream()
                                .filter(c -> "work".equalsIgnoreCase(c.getName()))
                                .forEach(c -> c.delete().queue(
                                        v -> System.out.println("Smazán /work v " + g.getName()),
                                        e -> System.err.println("Mazání /work v " + g.getName() + " selhalo: " + e.getMessage())
                                ))
                )
        );
    }

    // Definice /e se skupinou "job" a subpříkazy
    private static CommandData createEconomyCommand() {
        SubcommandGroupData jobGroup = new SubcommandGroupData("job", "Manage your profession")
                .addSubcommands(
                        new SubcommandData("list", "View available jobs"),
                        new SubcommandData("select", "Choose a profession")
                                .addOption(OptionType.STRING, "job", "Select your profession", true),
                        new SubcommandData("leave", "Quit your current job")
                );

        return Commands.slash("e", "Economy system commands")
                .addSubcommandGroups(jobGroup)
                .addSubcommands(
                        new SubcommandData("work", "Earn money from your job"),
                        new SubcommandData("balance", "Check your coin balance"),
                        new SubcommandData("profile", "View player profile")
                                .addOption(OptionType.USER, "user", "Player to inspect", false),
                        new SubcommandData("shop", "Browse available items"),
                        new SubcommandData("buy", "Purchase an item")
                                .addOption(OptionType.STRING, "item", "Item ID to purchase", true),
                        new SubcommandData("slots", "Play slot machine")
                                .addOption(OptionType.INTEGER, "bet", "Amount to wager", true),
                        new SubcommandData("baltop", "View wealth leaderboard"),
                        new SubcommandData("help", "Show command list")
                );
    }
}