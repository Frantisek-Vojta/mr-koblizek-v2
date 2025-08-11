package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.concurrent.ThreadLocalRandom;

public class LoveCommand implements Command {
    @Override
    public String getName() {
        return "love";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User user1 = event.getOption("user", OptionMapping::getAsUser);
        User user2 = event.getOption("user2", OptionMapping::getAsUser);

        if (user1 == null || user2 == null) {
            event.reply("Please tag two users!").setEphemeral(true).queue();
            return;
        }

        int lovePercent = ThreadLocalRandom.current().nextInt(0, 101);
        String loveMessage = getLoveMessage(lovePercent);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("💖 LOVE METER 💖")
                .setDescription(user1.getAsMention() + " ❤️ " + user2.getAsMention())
                .addField("Compatibility", lovePercent + "%", false)
                .addField("Result", loveMessage, false)
                .setColor(0xfcb603);

        event.replyEmbeds(embed.build()).queue();
    }

    private String getLoveMessage(int percent) {
        if (percent >= 90) return "Soulmates! 💕";
        if (percent >= 70) return "Perfect match! 💘";
        if (percent >= 50) return "Good potential! 💓";
        if (percent >= 30) return "Maybe friends? 💔";
        return "Not compatible... 😢";
    }
}
