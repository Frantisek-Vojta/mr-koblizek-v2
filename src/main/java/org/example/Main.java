package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.commands.RoleUpdate;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Main extends ListenerAdapter {

    // POZOR: token nikdy nedávej do kódu. Použij např. proměnnou prostředí.
    private static final String BOT_TOKEN = "TOKEN";

    public static void main(String[] args) throws LoginException, InterruptedException {
        CommandManager commandManager = new CommandManager();

        JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES
                )
                .setMemberCachePolicy(net.dv8tion.jda.api.utils.MemberCachePolicy.ALL)
                .setChunkingFilter(net.dv8tion.jda.api.utils.ChunkingFilter.ALL)
                .addEventListeners(
                        new Main(),
                        new RoleUpdate(),
                        new CommandManager()
                )
                .build();

        // Volitelné jednorázové vyčištění guild příkazů (aby zmizely duplikáty /e ze scope guild):
        jda.awaitReady();
        jda.getGuilds().forEach(guild -> guild.updateCommands().addCommands(java.util.List.of()).queue());
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("[SYSTEM] Bot is now online! Registering commands...");
        registerGlobalCommands(event.getJDA());
    }

    private void registerGlobalCommands(JDA jda) {
        List<CommandData> commands = new ArrayList<>();

        // Basic commands
        commands.add(Commands.slash("ping", "Check bot latency"));
        commands.add(Commands.slash("help", "Show command list"));
        commands.add(Commands.slash("botinfo", "Show info about bot"));
        commands.add(Commands.slash("freenitro", "give you free nitro"));
        commands.add(Commands.slash("meme", "meme"));
        commands.add(Commands.slash("donut", "donut photo"));

        // Economy system
        commands.add(createEconomyCommand());

        jda.updateCommands().addCommands(commands).queue(
                success -> System.out.println("[SUCCESS] Registered all commands"),
                error -> {
                    System.err.println("[ERROR] Failed to register commands: " + error.getMessage());
                    error.printStackTrace();
                }
        );
    }

    private CommandData createEconomyCommand() {
        OptionData jobOption = new OptionData(OptionType.STRING, "job", "Select your profession", true)
                .addChoices(
                        new Command.Choice("Miner", "miner"),
                        new Command.Choice("Fisher", "fisher"),
                        new Command.Choice("Lumberjack", "lumberjack"),
                        new Command.Choice("Programmer", "programmer"),
                        new Command.Choice("CEO", "ceo")
                );

        SubcommandGroupData jobGroup = new SubcommandGroupData("job", "Manage your profession")
                .addSubcommands(
                        new SubcommandData("list", "View available jobs"),
                        new SubcommandData("select", "Choose a profession").addOptions(jobOption),
                        new SubcommandData("leave", "Quit your current job")
                );

        return Commands.slash("e", "Economy system commands")
                .addSubcommandGroups(jobGroup)
                .addSubcommands(
                        new SubcommandData("work", "Earn money from your job"),
                        new SubcommandData("balance", "Check your coin balance"),
                        new SubcommandData("profile", "View player profile")
                                .addOption(OptionType.USER, "user", "Player to inspect", false),
                        new SubcommandData("shop", "Browse available items"),
                        new SubcommandData("buy", "Purchase an item")
                                .addOption(OptionType.STRING, "item", "Item ID to purchase", true),
                        new SubcommandData("slots", "Play slot machine")
                                .addOption(OptionType.INTEGER, "bet", "Amount to wager", true),
                        new SubcommandData("baltop", "View wealth leaderboard"),
                        new SubcommandData("help", "Show command list")
                );
    }
}
