package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RoastCommand implements Command {

    private static final List<String> ROASTS = List.of(
            "I'd roast you, but my mom said I'm not allowed to burn trash.",
            "You're the reason the gene pool needs a lifeguard.",
            "I'd call you a clown, but that would be an insult to clowns.",
            "Your WiFi password is probably your only secret.",
            "You're proof that even evolution makes mistakes.",
            "I've seen better looking things crawl out of a keyboard.",
            "If brains were gasoline, you couldn't power a go-kart.",
            "You're like a software update — nobody asked for you.",
            "Even your search history is embarrassed by you.",
            "You're the human equivalent of a 404 error.",
            "Your personality has the energy of a low battery warning.",
            "I'd explain the joke, but I don't have time to explain everything.",
            "You're like a cloud — when you disappear, it's a beautiful day.",
            "The trash gets taken out more than you do.",
            "You have the charisma of a loading screen."
    );

    @Override
    public String getName() {
        return "roast";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User target = event.getOption("user", OptionMapping::getAsUser);

        if (target == null) {
            event.reply("Please tag a user to roast!").setEphemeral(true).queue();
            return;
        }

        if (target.equals(event.getUser())) {
            event.reply("Roasting yourself? Bold move. 💀").setEphemeral(true).queue();
            return;
        }

        if (target.isBot()) {
            event.reply("I'm not roasting my fellow bots. We stick together. 🤖").setEphemeral(true).queue();
            return;
        }

        String roast = ROASTS.get(ThreadLocalRandom.current().nextInt(ROASTS.size()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🔥 Roast incoming for " + target.getName() + "!")
                .setDescription(roast)
                .setThumbnail(target.getEffectiveAvatarUrl())
                .setFooter("Requested by " + event.getUser().getName())
                .setColor(0xFF4500);

        event.replyEmbeds(embed.build()).queue();
    }
}
