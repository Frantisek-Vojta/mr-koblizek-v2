// Java
package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.commands.*;
import org.example.economy.EconomyManager;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.jobs.JobType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commands = new HashMap<>();
    private final EconomyManager economyManager;

    public CommandManager() {
        this.economyManager = new EconomyManager();

        // Registrace běžných (ne‑economy) příkazů
        registerCommand(new DonutCommand());
        registerCommand(new FreeNitroCommand());
        registerCommand(new GuessCommand());
        registerCommand(new HelpCommand());
        registerCommand(new PingCommand());
        registerCommand(new BotInfoCommand());
        registerCommand(new LoveCommand());
        registerCommand(new MemeCommand());

        // MIGRACE: všem s UNEMPLOYED/null nastav MINER (jednorázově při startu)
        migrateDefaultJobsToMiner(economyManager.getDatabase());
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();

        // Ekonomika je pod /e
        if ("e".equals(commandName)) {
            handleEconomyCommand(event);
            return;
        }

        // Ostatní top-level příkazy
        Command command = commands.get(commandName);
        if (command != null) {
            executeCommandSafely(event, command);
        } else {
            event.reply("❌ Unknown command: `" + commandName + "`")
                    .setEphemeral(true)
                    .queue();
        }
    }

    private void handleEconomyCommand(SlashCommandInteractionEvent event) {
        try {
            // Uvnitř EconomyManager očekávejte směrování podle event.getSubcommandGroup() / getSubcommandName()
            economyManager.handleCommand(event);
        } catch (Exception e) {
            event.reply("❌ An error occurred while executing the economy command.")
                    .setEphemeral(true)
                    .queue();
            e.printStackTrace();
        }
    }

    private void executeCommandSafely(SlashCommandInteractionEvent event, Command command) {
        try {
            command.execute(event);
        } catch (Exception e) {
            event.reply("❌ An error occurred while executing this command.")
                    .setEphemeral(true)
                    .queue();
            e.printStackTrace();
        }
    }

    // Důležité: pokud Main volá commandManager.handle(event), deleguj na JDA handler
    public void handle(SlashCommandInteractionEvent event) {
        onSlashCommandInteraction(event);
    }

    // --- Migrace výchozí práce na MINER ---
    private void migrateDefaultJobsToMiner(Database database) {
        int migrated = 0;
        try {
            Method[] methods = database.getClass().getMethods();
            for (Method m : methods) {
                if (m.getParameterCount() != 0) continue;

                Object result;
                try {
                    result = m.invoke(database);
                } catch (Throwable ignored) {
                    continue;
                }
                if (result == null) continue;

                if (result instanceof Collection<?> col) {
                    for (Object o : col) {
                        if (o instanceof UserData ud) {
                            if (ensureMiner(ud)) migrated++;
                        }
                    }
                } else if (result instanceof Map<?, ?> map) {
                    for (Object v : map.values()) {
                        if (v instanceof UserData ud) {
                            if (ensureMiner(ud)) migrated++;
                        }
                    }
                }
            }
        } catch (Throwable t) {
            System.err.println("Migrace jobů: chyba: " + t.getMessage());
        }

        if (migrated > 0) {
            try {
                database.save();
            } catch (Throwable t) {
                System.err.println("Uložení po migraci selhalo: " + t.getMessage());
            }
        }
        System.out.println("Migrace jobů dokončena. Přenastaveno uživatelů: " + migrated);
    }

    private boolean ensureMiner(UserData user) {
        try {
            var job = user.getJob();
            if (job == null || job == JobType.UNEMPLOYED) {
                user.setJob(JobType.MINER);
                return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }
}