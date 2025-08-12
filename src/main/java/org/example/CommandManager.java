package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.commands.*;
import org.example.economy.EconomyManager;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final EconomyManager economyManager;

    public CommandManager() {
        // Inicializace EconomyManageru (registruje a načte data)
        this.economyManager = new EconomyManager();

        // Běžné (neekonomické) příkazy
        addCommand(new DonutCommand());
        addCommand(new FreeNitroCommand());
        addCommand(new GuessCommand());
        addCommand(new HelpCommand());
        addCommand(new PingCommand());
        addCommand(new BotInfoCommand());
        addCommand(new LoveCommand());
        addCommand(new MemeCommand());
    }

    private void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void handle(SlashCommandInteractionEvent event) {
        String name = event.getName();

        // Ekonomické příkazy jsou pod slash příkazem "e" a řeší se přes EconomyManager
        if ("e".equals(name)) {
            try {
                economyManager.handleCommand(event);
            } catch (Exception e) {
                event.reply("❌ An error occurred while executing the economy command.")
                        .setEphemeral(true)
                        .queue();
                e.printStackTrace();
            }
            return;
        }

        // Ostatní (běžné) příkazy
        Command command = commands.get(name);
        if (command != null) {
            try {
                command.execute(event);
            } catch (Exception e) {
                event.reply("❌ An error occurred while executing this command.")
                        .setEphemeral(true)
                        .queue();
                e.printStackTrace();
            }
        } else {
            // Neznámý příkaz – vždy odpovědět, aby Discord nehlásil „application did not respond“
            event.reply("❌ Unknown command: `" + name + "`")
                    .setEphemeral(true)
                    .queue();
        }
    }
}