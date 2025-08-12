package org.example.economy;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.commands.*;
import org.example.economy.data.Database;
import org.example.economy.jobs.JobManager;
import org.example.economy.shop.ShopManager;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {
    private final Map<String, EconomyCommand> commands = new HashMap<>();
    private final Database database;
    private final ShopManager shopManager;
    private final JobManager jobManager;

    public EconomyManager() {
        this.database = new Database();
        this.shopManager = new ShopManager();
        this.jobManager = new JobManager(database);
        registerCommands();
        initializeDatabase();
    }

    private void initializeDatabase() {
        database.initialize();
    }

    private void registerCommands() {
        addCommand(new BalanceCommand(database));
        addCommand(new BaltopCommand(database));
        addCommand(new WorkCommand(database, jobManager));
        addCommand(new ProfileCommand(database, jobManager));
        addCommand(new JobCommand(database, jobManager));
        addCommand(new ShopCommand(shopManager));
        addCommand(new BuyCommand(database, shopManager));
        addCommand(new SlotsCommand(database));
        addCommand(new HelpCommand(database));
    }

    private void addCommand(EconomyCommand command) {
        commands.put(command.getName(), command);
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        // Musí být slash příkaz "e"
        if (!"e".equals(event.getName())) {
            event.reply("❌ Invalid economy command context.").setEphemeral(true).queue();
            return;
        }

        String sub = extractSubcommandName(event);
        if (sub == null) {
            event.reply("ℹ️ Use `/e <subcommand>` (e.g. `balance`, `work`, `job list`).").setEphemeral(true).queue();
            return;
        }

        EconomyCommand command = commands.get(sub);
        if (command != null) {
            try {
                command.execute(event);
            } catch (Exception e) {
                handleCommandError(event, e);
            }
        } else {
            event.reply("❌ Unknown economy subcommand: `" + sub + "`. Try `/e balance`, `/e work`, `/e job list`.").setEphemeral(true).queue();
        }
    }

    private String extractSubcommandName(SlashCommandInteractionEvent event) {
        // Vrací např. "balance", "work", "slots", nebo u skupiny "job" vrací "list"/"select"/"leave"
        return event.getSubcommandName();
    }

    private void handleCommandError(SlashCommandInteractionEvent event, Exception e) {
        event.reply("❌ An error occurred while executing this command.").setEphemeral(true).queue();
        e.printStackTrace();
    }

    public Database getDatabase() {
        return database;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public JobManager getJobManager() {
        return jobManager;
    }
}