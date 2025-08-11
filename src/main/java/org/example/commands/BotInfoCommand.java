package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BotInfoCommand implements Command {
    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("<:icon:1404423510201208874> Bot Info <:icon:1404423510201208874>")
                .setDescription("Info about me")
                .addField("I'm a cool and best Discord bot! || frfr ||", "this is real btw", false)
                .addField("I'm created by: xCel_cze#0", " ", false)
                .addField("I'm created in Java programming language", "I think xcel love this language ", false)
                .addField("xCel is very very cool and good boy because he gave me life", "frfr", false)
                .addField("I´m on **" + event.getJDA().getGuilds().size() + "** servers!", "plz add me to your server im good bot ): ", false)
                .setColor(0xfcb603);

        event.replyEmbeds(embed.build()).queue();
    }
}
