package org.example.economy.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.commands.Command;

public abstract class EconomyCommand implements Command {
    @Override
    public abstract String getName();
    
    @Override
    public abstract void execute(SlashCommandInteractionEvent event);
    
    protected String getEconomyCommandName() {
        return "e";
    }
}