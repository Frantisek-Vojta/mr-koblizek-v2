package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.User;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BaltopCommand extends EconomyCommand {
    private final Database database;

    public BaltopCommand(Database database) {
        this.database = database;
    }

    @Override
    public String getName() {
        return "baltop";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<UserData> topUsers = database.getAllUsers().values().stream()
                .sorted(Comparator.comparingLong(UserData::getCoins).reversed())
                .limit(10)
                .collect(Collectors.toList());

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🏆 Top 10 Richest Users")
                .setColor(0xFEE75C);

        StringBuilder description = new StringBuilder();

        for (int i = 0; i < topUsers.size(); i++) {
            UserData userData = topUsers.get(i);

            // Vytvoříme mention pomocí user ID - to vytvoří klikatelný ping
            String userMention = "<@" + userData.getUserId() + ">";

            description.append("**")
                    .append(i + 1)
                    .append(".** ")
                    .append(userMention)
                    .append(" - **")
                    .append(String.format("%,d", userData.getCoins()))
                    .append("** coins\n");
        }

        embed.setDescription(description.toString());
        event.replyEmbeds(embed.build()).queue();
    }
}