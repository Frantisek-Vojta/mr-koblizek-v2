package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.jobs.JobManager;
import org.example.economy.utils.EmbedUtils;

import java.time.Duration;
import java.time.Instant;

public class ProfileCommand extends EconomyCommand {
    private final Database database;

    public ProfileCommand(Database database, JobManager jobManager) {
        this.database = database;
    }

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        database.updateUserName(userId, event.getUser().getName());

        User user = event.getOption("user") != null ? 
            event.getOption("user").getAsUser() : event.getUser();
        
        UserData userData = database.getUserData(user.getId());
        
        String cooldown = getWorkCooldown(userData);
        
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("📊 " + user.getName() + "'s Profile")
            .setThumbnail(user.getEffectiveAvatarUrl())
            .setColor(0x5865F2)
            .addField("💰 Balance", String.format("%,d coins", userData.getCoins()), true)
            .addField("🔨 Job", userData.getJob().getDisplayName(), true)
            .addField("📊 Level", String.valueOf(userData.getLevel()), true)
            .addField("✨ XP", String.format("%,d/%,d XP", 
                userData.getXp(), getXpForNextLevel(userData.getLevel())), true)
            .addField("⏳ Next Work", cooldown, false);
        
        event.replyEmbeds(embed.build()).queue();
    }

    private String getWorkCooldown(UserData userData) {
        Instant now = Instant.now();
        Instant lastWork = userData.getLastWork();
        
        if (lastWork.equals(Instant.MIN)) {
            return "Available now!";
        }
        
        Instant nextWork = lastWork.plus(Duration.ofHours(6));
        if (now.isAfter(nextWork)) {
            return "Available now!";
        }
        
        Duration remaining = Duration.between(now, nextWork);
        return String.format("%dh %dm", 
            remaining.toHours(), 
            remaining.toMinutesPart());
    }

    private int getXpForNextLevel(int currentLevel) {
        return (int) (100 * Math.pow(1.2, currentLevel - 1));
    }
}