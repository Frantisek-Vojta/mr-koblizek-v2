package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FactCommand implements Command {

    private static final List<String> FACTS = List.of(
            "Honey never spoils. Archaeologists found 3000-year-old honey in Egyptian tombs.",
            "A day on Venus is longer than a year on Venus.",
            "Octopuses have three hearts and blue blood.",
            "Bananas are berries, but strawberries are not.",
            "The Eiffel Tower grows about 15 cm taller in summer due to heat expansion.",
            "Cleopatra lived closer in time to the Moon landing than to the construction of the Great Pyramid.",
            "A group of flamingos is called a flamboyance.",
            "The shortest war in history lasted 38 to 45 minutes (Anglo-Zanzibar War, 1896).",
            "There are more possible chess games than atoms in the observable universe.",
            "Sharks are older than trees — they've existed for over 400 million years.",
            "You cannot hum while holding your nose closed.",
            "A snail can sleep for up to 3 years.",
            "The human body contains enough iron to make a 3-inch nail.",
            "Light takes 8 minutes to travel from the Sun to Earth.",
            "Wombat poop is cube-shaped."
    );

    @Override
    public String getName() {
        return "fact";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String fact = FACTS.get(ThreadLocalRandom.current().nextInt(FACTS.size()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🧠 Random Fact")
                .setDescription(fact)
                .setColor(0x1E90FF)
                .setFooter("Requested by " + event.getUser().getName());

        event.replyEmbeds(embed.build()).queue();
    }
}
