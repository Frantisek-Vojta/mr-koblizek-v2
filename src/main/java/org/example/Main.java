package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main extends ListenerAdapter {

    private static final Path IMG_KOBLIZEK = Paths.get("imgs/koblizek");
    private static final Path IMG_MEME = Paths.get("imgs/meme");
    private static final Path IMG_NITRO = Paths.get("imgs/nitro");


    public static void main(String[] args) throws InterruptedException, LoginException {
        if (args.length < 1) {
            throw new RuntimeException("Missing angrument xd");
        }


        JDA jda = JDABuilder.createDefault(args[0])
                .addEventListeners(new Main())
                .build().awaitReady();
        jda.updateCommands()
                .addCommands(
                        Commands.slash("koblizek", "Send random image of donut"),
                        Commands.slash("meme", "Send random very funny meme"),
                        Commands.slash("freenitro", "Give you free nitro frfr no scam 100% working"),
                        Commands.slash("botinfo", "Send info about me"),
                        Commands.slash("help", "Show help"),
                        Commands.slash("ping", "Check bot's latency"),
                        Commands.slash("idk", "Send shrug emoji")
                        //Commands.slash("",""),
                        //Commands.slash("",""),
                        //Commands.slash("",""),
                        //Commands.slash("","")
                )
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        MessageChannel channel = event.getMessageChannel();

        // Koblizek command
        if (command.equals("koblizek")) {
            try {
                sendRandomImage(channel, IMG_KOBLIZEK, "🍩 Random photo of koblizek 🍩", "Here is your random koblizek image!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Meme command
        else if (command.equals("meme")) {
            try {
                sendRandomImage(channel, IMG_MEME, "😂 Random meme 😂", "Here is your random meme!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Free Nitro command
        else if (command.equals("freenitro")) {
            try {
                sendRandomImage(channel, IMG_NITRO, ":rocket: Here is your free nitro :rocket:", "No scam 100% working no virus frfr", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Bot info command
        else if (command.equals("botinfo")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("<:icon:1347146418049122315> Bot Info <:icon:1347146418049122315>")
                    .setDescription("Info about me")
                    .addField("I'm a cool and best Discord bot! || frfr ||", "this is real btw", false)
                    .addField("I'm created by: xCel_cze#0", " ", false)
                    .addField("I'm created by: Java programming language", "I think xcel love this language ", false)
                    .addField("xCel is very very cool and good boy because he gave me life", "frfr", false)
                    .addField("I´m on **" + event.getJDA().getGuilds().size() + "** servers!", "plz add me to your server im good bot ): ", false) // ZDE JE NOVÁ ŘÁDKA
                    .setColor(0xfcb603);
            event.replyEmbeds(embed.build()).queue();
        }


        // Help command
        else if (command.equals("help")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("====== Help ======")
                    .addField("/koblizek", "Show random image of donut", false)
                    .addField("/help", "Show this", false)
                    .addField("/botinfo", "Show info about me", false)
                    .addField("/idk", "Show: ¯\\_(ツ)_/¯", false)
                    .addField("/freenitro", "Give you free nitro frfr noscam 100% working", false)
                    .addField("/meme", "Show random very funny meme", false)
                    .setColor(0xfcb603)
                    .setFooter("I don't know what to add? DM me for tips plz");
            event.replyEmbeds(embed.build()).queue();
        }



        //roulette
        else if (command.equals("roulette")) {
            // Příjem parametrů od uživatele - barva a částka
            String color = event.getOption("color") != null ? event.getOption("color").getAsString() : "";
            double betAmount = event.getOption("amount") != null ? event.getOption("amount").getAsDouble() : 0;

            // Výběr náhodné barvy (red, black, nebo green)
            String[] colors = {"red", "black", "green"};
            Random random = new Random();
            String result = colors[random.nextInt(colors.length)];

            // Určujeme, zda uživatel vyhrál nebo prohrál
            String outcome = result.equals(color) ? "You won!" : "You lost!";

            // Odpověď pro uživatele
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Roulette")
                    .setDescription("You chose: " + color + "\n" +
                            "The result is: " + result + "\n" +
                            "Bet amount: " + betAmount + "\n" +
                            outcome)
                    .setColor(0xfcb603);

            event.replyEmbeds(embed.build()).queue();
        }



        // Ping command
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
            event.reply("There ar.queue");
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
