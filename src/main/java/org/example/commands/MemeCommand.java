package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class MemeCommand implements Command {
    private static final Path IMG_MEME = Paths.get("imgs/meme");

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            sendRandomImage(event, IMG_MEME, "😂 RANDOM MEME INCOMING! 😂", "Enjoy the meme!");
        } catch (IOException e) {
            event.reply("🚨 Failed to load meme").queue();
        }
    }

    private void sendRandomImage(SlashCommandInteractionEvent event, Path directory, String title, String description) throws IOException {
        if (!Files.exists(directory) || Files.list(directory).count() == 0) {
            event.reply("😂 No memes available!").queue();
            return;
        }

        File[] files = directory.toFile().listFiles();
        File image = files[new Random().nextInt(files.length)];

        event.replyEmbeds(new EmbedBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setColor(0xfcb603)
                        .setImage("attachment://" + image.getName())
                        .build())
                .addFile(image)
                .queue();

    }
}
