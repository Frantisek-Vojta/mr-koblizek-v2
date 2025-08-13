package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;

public class BalanceCommand extends EconomyCommand {
    private final Database database;

    public BalanceCommand(Database database) {
        this.database = database;
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        database.updateUserName(userId, event.getUser().getName());

        User user = event.getOption("user") != null ? 
            event.getOption("user").getAsUser() : event.getUser();
        
        UserData userData = database.getUserData(user.getId());
        
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("💰 " + user.getName() + "'s Balance")
            .setColor(0x5865F2)
            .setThumbnail(user.getEffectiveAvatarUrl())
            .addField("Coins", String.format("%,d", userData.getCoins()), true)
            .addField("Bank", "0", true); // Můžete dodělat bankovní systém později
            
        event.replyEmbeds(embed.build()).queue();
    }

}