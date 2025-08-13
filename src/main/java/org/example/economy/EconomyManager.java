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
        initialize();
    }

    private void initialize() {
        database.initialize();
        registerCommands();
        System.out.println("[Economy] Systém inicializován s " + commands.size() + " příkazy");
    }

    private void registerCommands() {
        // Hlavní příkazy
        registerCommand("balance", new BalanceCommand(database));
        registerCommand("baltop", new BaltopCommand(database));
        registerCommand("profile", new ProfileCommand(database, jobManager));
        registerCommand("help", new HelpCommand(database));

        // Práce
        registerCommand("job", new JobCommand(database, jobManager));
        registerCommand("work", new WorkCommand(database, jobManager));

        // Obchod
        registerCommand("shop", new ShopCommand(shopManager));
        registerCommand("buy", new BuyCommand(database, shopManager));

        // Hazard
        registerCommand("slots", new SlotsCommand(database));
    }

    private void registerCommand(String commandName, EconomyCommand command) {
        commands.put(commandName.toLowerCase(), command);
        System.out.println("[Economy] Registrován příkaz: " + commandName);
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        try {
            if (!"e".equals(event.getName())) {
                sendError(event, "Špatný kontext příkazu. Použij /e příkady pro ekonomický systém.");
                return;
            }

            String subcommand = event.getSubcommandName();
            if (subcommand == null) {
                sendError(event, "Chybí podpříkaz. Použij `/e help` pro dostupné příkazy.");
                return;
            }

            // Aktualizace jména uživatele
            updateUserData(event);

            EconomyCommand command = commands.get(subcommand.toLowerCase());
            if (command != null) {
                command.execute(event);
            } else {
                sendError(event, "Neznámý příkaz. Použij `/e help` pro dostupné příkazy.");
            }
        } catch (Exception e) {
            handleUnexpectedError(event, e);
        }
    }

    private void updateUserData(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        String userName = event.getUser().getName();
        database.updateUserName(userId, userName);
    }

    private void sendError(SlashCommandInteractionEvent event, String message) {
        event.reply("❌ " + message).setEphemeral(true).queue();
    }

    private void handleUnexpectedError(SlashCommandInteractionEvent event, Exception e) {
        System.err.println("Chyba při zpracování příkazu:");
        e.printStackTrace();
        event.reply("⚠️ Došlo k neočekávané chybě. Zkus to prosím později.")
                .setEphemeral(true)
                .queue();
    }

    public Database getDatabase() {
        return database;
    }
}