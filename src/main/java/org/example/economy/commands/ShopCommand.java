package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.shop.ShopManager;

public class ShopCommand extends EconomyCommand {
    private final ShopManager shopManager;

    public ShopCommand(ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("🛒 Shop")
            .setColor(0xEB459E);

        shopManager.getAvailableItems().forEach(item -> {
            embed.addField(
                item.getName() + " - " + item.getPrice() + " coins",
                item.getDescription(),
                false
            );
        });

        event.replyEmbeds(embed.build()).queue();
    }
}