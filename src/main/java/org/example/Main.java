package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.example.commands.RoleUpdate;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    private final CommandManager commandManager = new CommandManager();
    private static final String BOT_TOKEN = "MTQwNDQxNzg2Nzk0ODIzMjc1NQ.GzoSOT.KlCsF5dx-miXacQiDZBYNzsqfnfCBCrEeukYl8"; // Sem doplň token

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                .addEventListeners(
                        new Main(),       // Slash command listener
                        new RoleUpdate()  // Role update listener
                )
                .build();
    }

    @Override //commit
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        commandManager.handle(event);
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("[SYSTEM] Bot is now online and registering commands...");

        for (Guild server : event.getJDA().getGuilds()) {
            server.updateCommands().addCommands(
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
                    Commands.slash("meme", "Show random meme")
            ).queue();
        }
    }
}

