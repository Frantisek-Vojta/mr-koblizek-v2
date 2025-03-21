package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.FileUpload;







// funguje uz ten autopush?
import javax.swing.text.html.Option;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;
















public class Main extends ListenerAdapter {

    private static final Path IMG_KOBLIZEK = Paths.get("imgs/koblizek");
    private static final Path IMG_MEME = Paths.get("imgs/meme");
    private static final Path IMG_NITRO = Paths.get("imgs/nitro");

    // Metoda pro načtení tokenu ze souboru
    public static String loadToken(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            return properties.getProperty("token", ""); // Vrátí token nebo prázdný string, pokud není nalezen
        } catch (IOException e) {
            System.err.println("Chyba při načítání souboru: " + e.getMessage());
            return null;
        }
    }





// komentar je na to jestli funguje --force push xd xd xd


    public static void main(String[] args) throws InterruptedException{
        // Načtení tokenu ze souboru
        String token = loadToken("src/config.properties");




        // Inicializace JDA
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new Main())
                .build()
                .awaitReady();

        // Nastavení příkazů pro bota
        jda.updateCommands()
                .addCommands(
                        Commands.slash("koblizek", "Send random image of donut"),
                        Commands.slash("meme", "Send random very funny meme"),
                        Commands.slash("freenitro", "Give you free nitro frfr no scam 100% working"),
                        Commands.slash("botinfo", "Send info about me"),
                        Commands.slash("help", "Show help"),
                        Commands.slash("ping", "Check bot's latency"),
                        Commands.slash("idk", "Send shrug emoji"),
                        Commands.slash("guess", "Guess the numbeer" )
                                .addOption(OptionType.INTEGER, "number", "Your guess 1 or 2", true)
                )
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        MessageChannel channel = event.getMessageChannel();

        // Příkaz koblizek
        if (command.equals("koblizek")) {
            try {
                sendRandomImage(channel, IMG_KOBLIZEK, "🍩 Random photo of koblizek 🍩", "Here is your random koblizek image!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Příkaz meme
        else if (command.equals("meme")) {
            try {
                sendRandomImage(channel, IMG_MEME, "😂 Random meme 😂", "Here is your random meme!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Příkaz free nitro
        else if (command.equals("freenitro")) {
            try {
                sendRandomImage(channel, IMG_NITRO, ":rocket: Here is your free nitro :rocket:", "No scam 100% working no virus frfr", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Příkaz botinfo
        else if (command.equals("botinfo")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("<:icon:1347146418049122315> Bot Info <:icon:1347146418049122315>")
                    .setDescription("Info about me")
                    .addField("I'm a cool and best Discord bot! || frfr ||", "this is real btw", false)
                    .addField("I'm created by: xCel_cze#0", " ", false)
                    .addField("I'm created in Java programming language", "I think xcel love this language ", false)
                    .addField("xCel is very very cool and good boy because he gave me life", "frfr", false)
                    .addField("I´m on **" + event.getJDA().getGuilds().size() + "** servers!", "plz add me to your server im good bot ): ", false)
                    .setColor(0xfcb603);
            event.replyEmbeds(embed.build()).queue();
        }

        // Příkaz help
        else if (command.equals("help")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("====== Help ======")
                    .addField("/koblizek", "Show random image of donut", false)
                    .addField("/help", "Show this", false)
                    .addField("/botinfo", "Show info about me", false)
                    .addField("/idk", "Show: ¯\\_(ツ)_/¯", false)
                    .addField("/freenitro", "Give you free nitro frfr noscam 100% working", false)
                    .addField("/meme", "Show random very funny meme", false)
                    .addField("/guess <your number>", "Guess 1 or 2. If you guess correctly, you'll win!", false)
                    .setColor(0xfcb603)
                    .setFooter("I don't know what to add? DM me for tips plz");
            event.replyEmbeds(embed.build()).queue();


            // funguje auto commit? ano nebo ne

        } else if (command.equals("guess")) {
                String[] parts = event.getOptions().get(0).getAsString().split("\\s+");

                if (parts.length != 1) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setDescription("Please use the command like: /guess 1 or /guess 2")
                            .setColor(0xfcb603)
                            .build()).queue();
                    return;
                }

                try {
                    int guess = Integer.parseInt(parts[0]);
                    if (guess < 1 || guess > 2) {
                        event.replyEmbeds(new EmbedBuilder()
                                .setDescription("The number must be either 1 or 2!")
                                .setColor(0xfcb603)
                                .build()).queue();
                        return;
                    }

                    int random = (int) (Math.random() * 2) + 1;

                    EmbedBuilder embed = new EmbedBuilder();
                    if (guess == random) {
                        embed.setDescription("🎉 **OMG! You won!** 🎉")
                                .setColor(0x00FF00);
                    } else {
                        embed.setDescription("😔 **Bruh! You lost, lil bro** 😔")
                                .setColor(0xFF0000);
                    }
                    event.replyEmbeds(embed.build()).queue();
                } catch (NumberFormatException e) {
                    event.replyEmbeds(new EmbedBuilder()
                            .setDescription("Please provide a valid number: 1 or 2!")
                            .setColor(0xfcb603)
                            .build()).queue();
                }
            }



            // Příkaz ping
        else if (command.equals("ping")) {
            long latency = event.getJDA().getGatewayPing();
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Ping")
                    .setDescription("Lateency: " + latency + " ms")
                    .setColor(0xfcb603);

            event.replyEmbeds(embed.build()).queue();
        }
    }

    private void sendRandomImage(MessageChannel channel, Path directory, String title, String description, SlashCommandInteractionEvent event) throws IOException {
        if (!Files.exists(directory) || Files.list(directory).count() == 0) {
            event.reply("There are no images available.");
            return;
        }
        Random random = new Random();
        File[] files = directory.toFile().listFiles();
        if (files != null && files.length > 0) {
            File image = files[random.nextInt(files.length)];
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setColor(0xfcb603)
                    .setImage("attachment://" + image.getName());
            event.replyEmbeds(embed.build())
                    .addFiles(FileUpload.fromData(image, image.getName())).queue();
        }





    }
}