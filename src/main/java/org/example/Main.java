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
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Main extends ListenerAdapter {

    private static final String STAFF_ROLE_ID = "1325367756182257674"; // ID role to check
    private static final String APPROVED_ROLE_ID = "1343551943485685760";
    private static final String MUTE_ROLE_ID = "1392606203577630783"; // Replace with your mute role ID // Replace with your mute role ID
    private static final Path IMG_KOBLIZEK = Paths.get("imgs/koblizek"); // path to donuts
    private static final Path IMG_MEME = Paths.get("imgs/meme"); // path to memes
    private static final Path IMG_NITRO = Paths.get("imgs/nitro"); // path to free nitro
    private static final String STAFF_COUNTER_FILE = "staffMessageCount.txt"; // Path to the staff counter file
    private static final Path BRAINROT_FILE = Paths.get("brainrot.txt");
    private int staffMessageCount;

    public Main() {
        loadStaffMessageCount(); //load int from staffMessageCount.txt
    }

    private void loadStaffMessageCount() {
        try {
            if (Files.exists(Paths.get(STAFF_COUNTER_FILE))) {
                BufferedReader reader = new BufferedReader(new FileReader(STAFF_COUNTER_FILE));
                String line = reader.readLine();
                if (line != null) {
                    staffMessageCount = Integer.parseInt(line);
                }
                reader.close();
            } else {
                staffMessageCount = 0; // if file staffMessageCount.txt doesnt exist the count will start from 0
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStaffMessageCount() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(STAFF_COUNTER_FILE));
            writer.write(String.valueOf(staffMessageCount));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                sendRandomImage(channel,  IMG_NITRO, ":rocket: Here is your free nitro :rocket:", "No scam 100% working no virus frfr", event);
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
                    .addField("/rules <category>", "Show rules for specific category", false)
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
        } else if (event.getName().equals("rules")) {
            String category = event.getOption("typ").getAsString(); // např. "minecraft", "valka", "discord"

            if (category.equalsIgnoreCase("minecraft")) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("📜 Minecraft Pravidla")
                        .setDescription("""
                    1. 🔹 Respektuj ostatní hráče  
                    2. 🔹 Zákaz hacků, exploitů, makra  
                    3. 🔹 Nezničuj server lagy nebo griefem  
                    4. 🔹 Nespamuj v chatu  
                    5. 🔹 Negriefuj a nekradni v cizím landu  
                    6. 🔹 Žádný nevhodný obsah (nahota, porno...)  
                    7. 🔹 Žádná toxicita ani šikana  
                    8. 🔹 Admin má poslední slovo  
                    9. 🔹 Používej češtinu nebo angličtinu  
                    10. 🔹 Zákaz auto-farem, gold/iron farm  
                    11. 🔹 Nesmíš claimnout Nether, End nebo Spawn  
                    12. 🔹 Válka jen skrze ticket a formulář  
                    13. 🔹 Reklama je zakázaná  
                    14. 🔹 Zákaz reálných náboženství a jmen  
                    15. 🔹 Stavby jen středověké – žádný redstone vlaky atd.  
                    16. 🔹 Nesmíš blokovat Angel Chest  
                    17. 🔹 Nepinguj admin tým
                    """)
                        .setColor(0x2ECC71); // zelená
                event.replyEmbeds(embed.build()).queue();

            } else if (category.equalsIgnoreCase("valka")) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("⚔️ Pravidla Války")
                        .setDescription("""
                    **Hlavní pravidla:**
                    1. 🔹 Obranné body musí být přístupné  
                    2. 🔹 Bitva musí být domluvena do 24h  
                    3. 🔹 Grief pouze ve War Claimu  
                    4. 🔹 Seznam bojovníků 30 min před bitvou  
                    5. 🔹 Zákaz Proxy Claimu  
                    6. 🔹 Obrana se musí odehrávat ve městě

                    **Zakázané taktiky a stavby:**
                    1. 🔹 Turtle-bunkry (méně než 2 vchody)  
                    2. 🔹 Skybase a levitující mosty  
                    3. 🔹 Lávové a vodní pasti  
                    4. 🔹 TNT, dripstone a výbušniny  
                    5. 🔹 Combat log v cizím claimu
                    """)
                        .setColor(0xe67e22); // oranžová
                event.replyEmbeds(embed.build()).queue();

            } else if (category.equalsIgnoreCase("discord")) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("💬 Discord Pravidla")
                        .setDescription("""
                    1. 🔹 Buď slušný a respektuj ostatní  
                    2. 🔹 Nespamuj zprávy, obrázky ani odkazy  
                    3. 🔹 Reklama je bez povolení zakázaná  
                    4. 🔹 Žádný NSFW nebo nevhodný obsah  
                    5. 🔹 Přezdívky a avatary musí být vhodné  
                    6. 🔹 Nepoužívej @everyone/@here  
                    7. 🔹 Respektuj rozhodnutí adminů  
                    8. 🔹 Piš do správných kanálů  
                    9. 🔹 Používej češtinu nebo angličtinu  
                    10. 🔹 Nesdílej osobní údaje nikoho
                    """)
                        .setColor(0x3498db); // modrá
                event.replyEmbeds(embed.build()).queue();

            } else {
                event.reply("❌ Neznámá kategorie pravidel! Zadej `minecraft`, `valka` nebo `discord`.").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        MessageChannel channel = message.getChannel();

        if (message.getReferencedMessage() != null)
            return;

        AtomicBoolean pingedStaff = new AtomicBoolean(false);
        AtomicBoolean pingedApproved = new AtomicBoolean(false);

        for (User user : message.getMentions().getUsers()) {
            if (user.getId().equals(event.getAuthor().getId()))
                continue;

            event.getGuild().retrieveMemberById(user.getId()).queue(member -> {
                boolean hasStaffRole = member.getRoles().stream().anyMatch(role -> role.getId().equals(STAFF_ROLE_ID));
                boolean hasApprovedRole = member.getRoles().stream().anyMatch(role -> role.getId().equals(APPROVED_ROLE_ID));
                boolean authorIsStaff = event.getMember().getRoles().stream()
                        .anyMatch(role -> role.getId().equals(STAFF_ROLE_ID));

                // Pokud někdo pingne staff člena a není sám staff → warning + 2min mute, zpráva se nemaže
                if (hasStaffRole && !authorIsStaff && pingedStaff.compareAndSet(false, true)) {
                    staffMessageCount++;
                    saveStaffMessageCount();

                    // Zprávu nesmažeme

                    // Mute na 2 minuty
                    event.getGuild().addRoleToMember(event.getMember(),
                                    event.getGuild().getRoleById(MUTE_ROLE_ID))
                            .reason("Pinged staff member")
                            .queueAfter(1, TimeUnit.SECONDS);

                    event.getGuild().removeRoleFromMember(event.getMember(),
                                    event.getGuild().getRoleById(MUTE_ROLE_ID))
                            .reason("Mute expired")
                            .queueAfter(2, TimeUnit.MINUTES);

                    // Warning embed
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("⚠️ Warning ⚠️")
                            .setDescription("Do not ping the **staff** members please :pleading_face:\n\n" +
                                    "You have been muted for 2 minutes")
                            .setFooter("This warning has been sent " + staffMessageCount + " times")
                            .setColor(0xFF0000);

                    channel.sendMessageEmbeds(embed.build())
                            .queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
                }

                // Pokud pingne staff jiného staffa → ignoruj
                if (hasStaffRole && authorIsStaff) {
                    System.out.println("Staff pinged staff – ignored.");
                }

                // Approved ping embed (jen jednou)
                if (hasApprovedRole && pingedApproved.compareAndSet(false, true)) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Ping Approved ✅")
                            .setDescription("Thank you for pinging this user!\nFeel free to ping him/her again \nif needed or not needed just ping again. :smiling_face_with_3_hearts:")
                            .setColor(0x00FF00);
                    channel.sendMessageEmbeds(embed.build()).queue();
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

            event.replyEmbeds(embed.build())
                    .addFile(image)
                    .queue();
        }
    }

    private void sendRandomText(MessageChannel channel, Path filePath, String title, String description, SlashCommandInteractionEvent event) throws IOException {
        if (!Files.exists(filePath)) {
            event.reply("There are no texts available.").queue();
            return;
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) {
            event.reply("The file is empty.").queue();
            return;
        }

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
                    Commands.slash("ping", "Ping"),
                    Commands.slash("guess", "Guess 1 or 2. If you guess correctly, you'll win!")
                            .addOption(OptionType.INTEGER, "number", "Your number", true),
                    Commands.slash("love", "Show how many % they love each other")
                            .addOption(OptionType.USER, "user", "First user", true)
                            .addOption(OptionType.USER, "user2", "Second user", true),
                    Commands.slash("rules", "Show rules of category")
                            .addOption(OptionType.STRING, "typ", "Vyber kategorii pravidel", true)
            ).queue();
        }
    }
}