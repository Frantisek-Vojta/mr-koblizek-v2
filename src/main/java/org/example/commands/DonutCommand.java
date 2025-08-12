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

public class DonutCommand implements Command {
    private static final Path IMG_DONUT = Paths.get("imgs/donut");

    @Override
    public String getName() {
        return "donut";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            sendRandomImage(event, IMG_DONUT, "🍩 FRESH DONUT FOR YOU! 🍩", "Enjoy your donut!");
        } catch (IOException e) {
            event.reply("🚨 Failed to load donut image").queue();
        }
    }

    private void sendRandomImage(SlashCommandInteractionEvent event, Path directory, String title, String description) throws IOException {
        if (!Files.exists(directory) || Files.list(directory).count() == 0) {
            event.reply("🍩 No donuts available right now!").queue();
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
                .addFiles(FileUpload.fromData(Files.readAllBytes(image.toPath()), image.getName()))
                .queue();
    }
}
