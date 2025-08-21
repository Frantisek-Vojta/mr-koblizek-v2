package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
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
        List<UserData> topUsers = database.getAllUsers().values().stream()
            .sorted(Comparator.comparingLong(UserData::getCoins).reversed())
            .limit(10)
            .collect(Collectors.toList());

        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("🏆 Top 10 Richest Users")
            .setColor(0xFEE75C);

        for (int i = 0; i < topUsers.size(); i++) {
            UserData userData = topUsers.get(i);
            String userName = event.getJDA().getUserById(userData.getUserId()) != null ? 
                event.getJDA().getUserById(userData.getUserId()).getName() : "Unknown User";
            
            embed.addField(
                (i + 1) + ". " + userName,
                String.format("%,d coins", userData.getCoins()),
                false
            );
        }

        event.replyEmbeds(embed.build()).queue();
    }
}