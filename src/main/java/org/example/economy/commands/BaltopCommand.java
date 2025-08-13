package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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
        List<UserData> allUsers = database.getAllUsers().values().stream()
                .sorted(Comparator.comparingLong(UserData::getCoins).reversed())
                .collect(Collectors.toList());

        // Calculate total economy wealth
        long totalWealth = allUsers.stream()
                .mapToLong(UserData::getCoins)
                .sum();

        // Take top 10 users
        List<UserData> topUsers = allUsers.stream()
                .limit(10)
                .collect(Collectors.toList());

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🏆 Richest members (Top 10)")
                .setColor(0xFEE75C);

        // Build leaderboard
        StringBuilder leaderboard = new StringBuilder();
        for (int i = 0; i < topUsers.size(); i++) {
            UserData userData = topUsers.get(i);
            String mention = "<@" + userData.getUserId() + ">";

            leaderboard.append(String.format(
                    "**%d.** %s - %,d coins\n",
                    i + 1,
                    mention,
                    userData.getCoins()
            ));
        }

        embed.setDescription(leaderboard.toString());
        embed.setFooter("Total economy wealth: " + String.format("%,d", totalWealth) + " coins");

        event.replyEmbeds(embed.build()).queue();
    }
}