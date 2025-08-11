package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

public class GuessCommand implements Command {
    @Override
    public String getName() {
        return "guess";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int guess = Objects.requireNonNull(event.getOption("number")).getAsInt();
        int random = (int) (Math.random() * 2) + 1;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🎲 GUESS RESULT 🎲")
                .setColor(guess == random ? 0x00FF00 : 0xFF0000)
                .setDescription(guess == random
                        ? "🎉 **You won!** The number was " + random
                        : "❌ **You lost!** The number was " + random);

        event.replyEmbeds(embed.build()).queue();
    }
}
