package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.shop.ShopItem;
import org.example.economy.shop.ShopManager;

public class BuyCommand extends EconomyCommand {
    private final Database database;
    private final ShopManager shopManager;

    public BuyCommand(Database database, ShopManager shopManager) {
        this.database = database;
        this.shopManager = shopManager;
    }

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String itemId = event.getOption("item").getAsString();
        UserData userData = database.getUserData(event.getUser().getId());
        ShopItem item = shopManager.getItemById(itemId);
        
        if (item == null) {
            event.reply("❌ This item doesn't exist in the shop!").queue();
            return;
        }
        
        if (userData.getCoins() < item.getPrice()) {
            event.reply(String.format("❌ You don't have enough coins! You need %,d more.", 
                item.getPrice() - userData.getCoins())).queue();
            return;
        }
        
        // Process purchase
        userData.removeCoins(item.getPrice());
        userData.getInventory().merge(item.getId(), 1, Integer::sum);
        database.save();
        
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("✅ Purchase Successful")
            .setDescription("You bought: **" + item.getName() + "**")
            .addField("Price", String.format("%,d coins", item.getPrice()), true)
            .addField("New Balance", String.format("%,d coins", userData.getCoins()), true)
            .setColor(0x57F287);
        
        event.replyEmbeds(embed.build()).queue();
    }
}