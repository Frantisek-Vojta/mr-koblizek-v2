package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EightBallCommand implements Command {

    private static final List<String> RESPONSES = List.of(
            "It is certain.", "It is decidedly so.", "Without a doubt.",
            "Yes, definitely.", "You may rely on it.", "As I see it, yes.",
            "Most likely.", "Outlook good.", "Yes.", "Signs point to yes.",
            "Reply hazy, try again.", "Ask again later.", "Better not tell you now.",
            "Cannot predict now.", "Concentrate and ask again.",
            "Don't count on it.", "My reply is no.", "My sources say no.",
            "Outlook not so good.", "Very doubtful."
    );

    @Override
    public String getName() {
        return "8ball";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String question = event.getOption("question").getAsString();
        int index = ThreadLocalRandom.current().nextInt(RESPONSES.size());
        String response = RESPONSES.get(index);

        int color = index < 10 ? 0x00FF00 : index < 15 ? 0xFFFF00 : 0xFF0000;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🎱 Magic 8-Ball")
                .addField("Question", question, false)
                .addField("Answer", response, false)
                .setColor(color);

        event.replyEmbeds(embed.build()).queue();
    }
}
