package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand implements Command {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long latency = event.getJDA().getGatewayPing();
        String status = latency < 150 ? "Excellent" : latency < 300 ? "Good" : "Slow";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🏓 PONG! 🏓")
                .addField("Latency", latency + "ms", false)
                .addField("Status", status, false)
                .setColor(0xfcb603);

        event.replyEmbeds(embed.build()).queue();
    }
}
