package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.ThreadLocalRandom;

public class CoinflipCommand implements Command {

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        boolean heads = ThreadLocalRandom.current().nextBoolean();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🪙 Coin Flip")
                .setDescription(heads ? "**Heads!** 👑" : "**Tails!** 🦅")
                .setColor(heads ? 0xFFD700 : 0xC0C0C0)
                .setFooter("Flipped by " + event.getUser().getName());

        event.replyEmbeds(embed.build()).queue();
    }
}
