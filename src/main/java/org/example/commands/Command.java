package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {
    String getName();
    void execute(SlashCommandInteractionEvent event);
}
