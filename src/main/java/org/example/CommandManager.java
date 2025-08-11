package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager() {
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
        Command command = commands.get(event.getName());
        if (command != null) {
            command.execute(event);
        }
    }
}
