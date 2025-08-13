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
        // Core commands
        addCommand(new BalanceCommand(database));
        addCommand(new BaltopCommand(database));
        addCommand(new ProfileCommand(database, jobManager));
        addCommand(new HelpCommand(database));

        // Job commands
        addCommand(new JobCommand(database, jobManager));

        // Money earning
        addCommand(new WorkCommand(database, jobManager));

        // Shop commands
        addCommand(new ShopCommand(shopManager));
        addCommand(new BuyCommand(database, shopManager));

        // Gambling
        addCommand(new SlotsCommand(database));
    }

    private void addCommand(EconomyCommand command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        try {
            // Verify this is an economy command
            if (!"e".equals(event.getName())) {
                sendError(event, "Invalid command context. Use /e commands for economy system.");
                return;
            }

            // Get the subcommand (or subcommand group + subcommand)
            String subcommandPath = getFullCommandPath(event);

            // Special case for help command
            if ("help".equals(subcommandPath)) {
                commands.get("help").execute(event);
                return;
            }

            // Find and execute the command
            EconomyCommand command = commands.get(subcommandPath);
            if (command != null) {
                command.execute(event);
            } else {
                sendError(event, "Unknown command. Use `/e help` for available commands.");
            }
        } catch (Exception e) {
            handleUnexpectedError(event, e);
        }
    }

    private String getFullCommandPath(SlashCommandInteractionEvent event) {
        // Handle subcommand groups (like "job select")
        if (event.getSubcommandGroup() != null) {
            return event.getSubcommandGroup() + " " + event.getSubcommandName();
        }
        // Handle simple subcommands (like "balance")
        return event.getSubcommandName();
    }

    private void sendError(SlashCommandInteractionEvent event, String message) {
        event.reply("❌ " + message)
                .setEphemeral(true)
                .queue();
    }

    private void handleUnexpectedError(SlashCommandInteractionEvent event, Exception e) {
        System.err.println("Error processing economy command:");
        e.printStackTrace();

        event.reply("⚠️ An unexpected error occurred. Please try again later.")
                .setEphemeral(true)
                .queue();
    }

    // Getters
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