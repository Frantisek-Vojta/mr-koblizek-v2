package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HelpCommand implements Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("📘 COMMAND HELP 📘")
                .setDescription("Here are all available commands:")
                .addField("/donut", "Get a random donut image", false)
                .addField("/freenitro", "Get totally legit free nitro", false)
                .addField("/guess [1-2]", "Try to guess the correct number", false)
                .addField("/ping", "Check bot latency", false)
                .addField("/botinfo", "Show bot information", false)
                .addField("/help", "Show this message", false)
                .addField("/love [user1] [user2]", "Calculate love percentage between two users", false)
                .setColor(0xfcb603);

        event.replyEmbeds(embed.build()).queue();
    }
}
