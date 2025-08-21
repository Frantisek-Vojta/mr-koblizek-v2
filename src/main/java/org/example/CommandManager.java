package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.commands.*;
import org.example.economy.EconomyManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final Map<String, Command> commands = new HashMap<>();
    private final EconomyManager economyManager;

    public CommandManager() {
        this.economyManager = new EconomyManager();
        registerCommands();
    }

    private void registerCommands() {
        // Register all commands
        registerCommand(new DonutCommand());
        registerCommand(new FreeNitroCommand());
        registerCommand(new GuessCommand());
        registerCommand(new HelpCommand());
        registerCommand(new PingCommand());
        registerCommand(new BotInfoCommand());
        registerCommand(new LoveCommand());
        registerCommand(new MemeCommand());
    }

    private void registerCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName().toLowerCase();

        if ("e".equals(commandName)) {
            handleEconomyCommand(event);
            return;
        }

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
}