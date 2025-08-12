package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.example.commands.RoleUpdate;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    private final CommandManager commandManager = new CommandManager();
    private static final String BOT_TOKEN = "MTQwNDQxNzg2Nzk0ODIzMjc1NQ.GzoSOT.KlCsF5dx-miXacQiDZBYNzsqfnfCBCrEeukYl8";

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                .addEventListeners(
                        new Main(),
                        new RoleUpdate()
                )
                .build();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        commandManager.handle(event);
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("[SYSTEM] Bot is now online and registering commands...");

        // Job select option choices
        OptionData jobOption = new OptionData(OptionType.STRING, "job", "Job to select", true)
                .addChoices(
                        new Command.Choice("Miner", "miner"),
                        new Command.Choice("Fisher", "fisher"),
                        new Command.Choice("Lumberjack", "lumberjack"),
                        new Command.Choice("Programmer", "programmer"),
                        new Command.Choice("CEO", "ceo")
                );

        // Subcommand group "job"
        SubcommandGroupData jobGroup = new SubcommandGroupData("job", "Job management")
                .addSubcommands(
                        new SubcommandData("list", "List available jobs"),
                        new SubcommandData("leave", "Leave your current job"),
                        new SubcommandData("select", "Select a job")
                                .addOptions(jobOption)
                );

        // Update global commands
        event.getJDA().updateCommands().addCommands(
                // Basic commands
                Commands.slash("donut", "Show random donut image"),
                Commands.slash("freenitro", "Get free nitro (totally legit)"),
                Commands.slash("guess", "Guess 1 or 2 to win")
                        .addOption(OptionType.INTEGER, "number", "Your guess (1 or 2)", true),
                Commands.slash("help", "Show command list"),
                Commands.slash("ping", "Show bot latency"),
                Commands.slash("botinfo", "Show bot information"),
                Commands.slash("love", "Calculate love percentage")
                        .addOption(OptionType.USER, "user", "First user", true)
                        .addOption(OptionType.USER, "user2", "Second user", true),
                Commands.slash("meme", "Show random meme"),

                // Economy commands
                Commands.slash("e", "Economy system commands")
                        // subcommand groups (job)
                        .addSubcommandGroups(jobGroup)
                        // standalone subcommands
                        .addSubcommands(
                                new SubcommandData("profile", "Show your economy profile")
                                        .addOption(OptionType.USER, "user", "User to view profile of", false),
                                new SubcommandData("work", "Work to earn money"),
                                new SubcommandData("slots", "Play slots")
                                        .addOption(OptionType.INTEGER, "bet", "Amount to bet", true),
                                new SubcommandData("shop", "View the shop"),
                                new SubcommandData("buy", "Buy an item from the shop")
                                        .addOption(OptionType.STRING, "item", "Item to buy", true),
                                new SubcommandData("baltop", "Show top 10 richest users"),
                                new SubcommandData("balance", "Check your balance"),
                                new SubcommandData("help", "Show command list")
                        )
        ).queue();
    }
}





