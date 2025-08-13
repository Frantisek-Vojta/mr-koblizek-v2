package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.utils.SlotsMachine;

import java.util.List;

public class SlotsCommand extends EconomyCommand {
    private final Database database;
    private final SlotsMachine slotsMachine;

    public SlotsCommand(Database database) {
        this.database = database;
        this.slotsMachine = new SlotsMachine();
    }

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long bet = event.getOption("bet").getAsLong();
        UserData userData = database.getUserData(event.getUser().getId());

        if (bet < 10) {
            event.reply("Minimum bet is 10 coins!").setEphemeral(true).queue();
            return;
        }

        if (userData.getCoins() < bet) {
            event.reply("You don't have enough coins for this bet!").setEphemeral(true).queue();
            return;
        }

        // Spin the slots
        List<List<String>> result = slotsMachine.spin();
        int multiplier = slotsMachine.calculateMultiplier(result);
        long winAmount = bet * multiplier;

        // Update user balance
        userData.removeCoins(bet);
        if (winAmount > 0) {
            userData.addCoins(winAmount);
        }
        database.save();

        // Build result message with 3x3 grid
        StringBuilder gridDisplay = new StringBuilder();
        for (List<String> row : result) {
            gridDisplay.append(String.join(" | ", row)).append("\n");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🎰 Slot Machine (3x3)")
                .setDescription(gridDisplay.toString())
                .setColor(multiplier > 0 ? 0x57F287 : 0xED4245);

        if (multiplier > 0) {
            embed.addField("Result", String.format("You won **%,d coins** (x%d)!", winAmount, multiplier), false);
        } else {
            embed.addField("Result", String.format("You lost **%,d coins**!", bet), false);
        }

        embed.addField("Balance", String.format("You now have **%,d coins**", userData.getCoins()), false);

        event.replyEmbeds(embed.build()).queue();
    }
}