package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main extends ListenerAdapter {

    private static final String STAFF_ROLE_ID = "1325367756182257674"; // ID role to check
    private static final Path IMG_KOBLIZEK = Paths.get("imgs/koblizek"); // path to donuts
    private static final Path IMG_MEME = Paths.get("imgs/meme"); // path to memes
    private static final Path IMG_NITRO = Paths.get("imgs/nitro"); // path to free nitro
    private static final String COUNTER_FILE = "messageCount.txt"; // Path to the counter file
    private static final Path BRAINROT_FILE = Paths.get("brainrot.txt");
    private int messageCount;

    public Main() {
        loadMessageCount(); //load int from messageCount.txt
    }

    private void loadMessageCount() {
        try {
            if (Files.exists(Paths.get(COUNTER_FILE))) {
                BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE));
                String line = reader.readLine();
                if (line != null) {
                    messageCount = Integer.parseInt(line);
                }
                reader.close();
            } else {
                messageCount = 0; // if file messageCount.txt doesnt exist the count will start from 0 logicky I don't know how to say in english xd
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMessageCount() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE));
            writer.write(String.valueOf(messageCount));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // comment to proof that i have use comments


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        MessageChannel channel = event.getMessageChannel();

        if (command.equals("koblizek")) {
            try {
                sendRandomImage(channel, IMG_KOBLIZEK, "🍩 Random photo of koblizek 🍩", "Here is your random koblizek image!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (command.equals("brainrot")) {
            try {
                sendRandomText(channel, BRAINROT_FILE, "Random brainrot shit", "Here is your brainrot skibidi shit!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (command.equals("meme")) {
            try {
                sendRandomImage(channel, IMG_MEME, "😂 Random meme 😂", "Here is your random meme!", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (command.equals("freenitro")) {
            try {
                sendRandomImage(channel, IMG_NITRO, ":rocket: Here is your free nitro :rocket:", "No scam 100% working no virus frfr", event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (command.equals("botinfo")) {
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
        } else if (command.equals("help")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("====== Help ======")
                    .addField("/koblizek", "Show random image of donut", false)
                    .addField("/help", "Show this", false)
                    .addField("/botinfo", "Show info about me", false)
                    .addField("/idk", "Show: ¯\\_(ツ)_/¯", false)
                    .addField("/freenitro", "Give you free nitro frfr noscam 100% working", false)
                    .addField("/meme", "Show random very funny meme", false)
                    .addField("/guess <your number>", "Guess 1 or 2. If you guess correctly, you'll win!", false)
                    .addField("/brainrot", "Show random brainrot shit", false)
                    .addField("/love <user1> <user2>", "Show how many % they love each other", false)
                    .setColor(0xfcb603)
                    .setFooter("I don't know what to add :( DM me for tips plz");
            event.replyEmbeds(embed.build()).queue();

        }   else if (event.getName().equals("guess")) {
                // Získání vybraného čísla (1 nebo 2)
                int guess = Objects.requireNonNull(event.getOption("number")).getAsInt();

                // Náhodné číslo 1 nebo 2
                int random = (int) (Math.random() * 2) + 1;

                // Vytvoření embed zprávy
                EmbedBuilder embed = new EmbedBuilder();

                if (guess == random) {
                    embed.setDescription("🎉 **You won good boooy!** 🎉")
                            .setColor(0x00FF00);  // zelená barva
                } else {
                    embed.setDescription("😔 **You lost lil bro** 😔")
                            .setColor(0xFF0000);  // červená barva
                }

                // Odpověď uživateli
                event.replyEmbeds(embed.build()).queue();


        } else if (command.equals("ping")) {
            long latency = event.getJDA().getGatewayPing();
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Ping")
                    .setDescription("Latency: " + latency + " ms")
                    .setColor(0xfcb603);

            event.replyEmbeds(embed.build()).queue();
        } else if (event.getName().equals("love")) {
                User user1 = Objects.requireNonNull(event.getOption("user")).getAsUser();
                User user2 = Objects.requireNonNull(event.getOption("user2")).getAsUser();

                int lovePercent = ThreadLocalRandom.current().nextInt(0, 101);

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(":smiling_face_with_3_hearts: Love percentage :smiling_face_with_3_hearts:")
                        .setDescription(user1.getAsMention() + " and " + user2.getAsMention() + " love " + lovePercent + "%")
                        .setColor(0xfcb603);

                event.replyEmbeds(embed.build()).queue();
            }

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return; //if person pinged the role but the person is bot it will dont show the message

        Message message = event.getMessage();
        MessageChannel channel = message.getChannel();


        if (message.getReferencedMessage() != null) { //if its reply ping it will dont count
            return;
        }


        AtomicBoolean pingedStaff = new AtomicBoolean(false); // if in message was someobdy pinged

        // will load the persons who has been pinged
        for (net.dv8tion.jda.api.entities.User user : message.getMentions().getUsers()) {
            // if person pinged himself it will not count lmao
            if (user.getId().equals(event.getAuthor().getId())) {
                continue;
            }

            // if person has role and the role has the correct ID
            event.getGuild().retrieveMemberById(user.getId()).queue(member -> {
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals(STAFF_ROLE_ID))) {
                    if (pingedStaff.compareAndSet(false, true)) {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("Warning")
                                .setDescription("Do not ping the **staff** members please :pleading_face:")
                                .setFooter("This message was sent: " + messageCount + " times")
                                .setColor(0xFF0000); // red color for warnign xd

                        channel.sendMessageEmbeds(embed.build()).queue();
                    }
                }

                if (pingedStaff.get()) {
                    messageCount++;
                    saveMessageCount(); // count +1 for the count
                }
            });
        }
    }


    private void sendRandomImage(MessageChannel channel, Path directory, String title, String description, SlashCommandInteractionEvent event) throws IOException {
        if (!Files.exists(directory) || Files.list(directory).count() == 0) {
            event.reply("There are no images available.").queue();
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

            // Use addFile instead of addFiles
            event.replyEmbeds(embed.build())
                    .addFile(image) // Use addFile for a single file
                    .queue();
        }
    }


    private void sendRandomText(MessageChannel channel, Path filePath, String title, String description, SlashCommandInteractionEvent event) throws IOException {
        if (!Files.exists(filePath)) {
            event.reply("There are no texts available.").queue();
            return;
        }

        // idk but its working
        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            event.reply("The file is empty.").queue();
            return;
        }


        // komentar abych mel commity protoze mam rad commity a potrebuju stats frfr


        Random random = new Random();
        String randomText = lines.get(random.nextInt(lines.size()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setDescription(randomText)
                .setColor(0xfcb603);

        event.replyEmbeds(embed.build()).queue();
    }





    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault("MTM0NjkwODA1MTU5MjE4NzkxNA.GJtfeW.cDpxk07XfLi6xRY70hdJ22PPy5btXARtvBFj4w")
                .addEventListeners(new Main())
                .build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot je online a registruje slash příkazy...");

        for (Guild server : event.getJDA().getGuilds()) {
            server.updateCommands().addCommands(
                    Commands.slash("koblizek", "Show random image of donut"),
                    Commands.slash("help", "Show this"),
                    Commands.slash("botinfo", "Show info about me"),
                    Commands.slash("idk", "Show: ¯\\_(ツ)_/¯"),
                    Commands.slash("freenitro", "Give you free nitro frfr noscam 100% working"),
                    Commands.slash("meme", "Show random very funny meme"),
                    Commands.slash("guess", "Guess 1 or 2. If you guess correctly, you'll win!")
                            .addOption(OptionType.INTEGER, "number", "Your number", true),

                    // Přidáno: LOVE command s dvěma uživateli
                    Commands.slash("love", "Show how many % they love each other")
                            .addOption(OptionType.USER, "user", "First user", true)
                            .addOption(OptionType.USER, "user2", "Second user", true)
            ).queue(); // <- DŮLEŽITÉ
        }
    }

}
