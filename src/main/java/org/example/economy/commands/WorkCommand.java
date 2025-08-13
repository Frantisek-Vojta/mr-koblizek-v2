package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.jobs.JobManager;
import org.example.economy.jobs.JobType;

import java.time.Instant;
import java.time.Duration;
import java.util.Random;

public class WorkCommand extends EconomyCommand {
    private final Database database;
    private final JobManager jobManager;
    private final Random random = new Random();

    public WorkCommand(Database database, JobManager jobManager) {
        this.database = database;
        this.jobManager = jobManager;
    }

    @Override
    public String getName() {
        return "work";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        database.updateUserName(userId, event.getUser().getName());
        UserData userData = database.getUserData(event.getUser().getId());
        Instant now = Instant.now();

        if (!canWork(userData, now)) {
            event.reply("You need to wait before working again! Check `/e profile` for cooldown.").setEphemeral(true).queue();
            return;
        }

        JobType job = userData.getJob();
        if (job == null || job == JobType.UNEMPLOYED) {
            event.reply("You don't have a job! Use `/e job list` to find one.").setEphemeral(true).queue();
            return;
        }

        int earnings = calculateEarnings(userData);
        int xpEarned = 5 + random.nextInt(16); // Random XP between 5-20

        userData.addCoins(earnings);
        userData.addXp(xpEarned);
        userData.setLastWork(now);
        database.save();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("💼 Work Completed")
                .setDescription(String.format(
                        "You worked as a **%s** and earned **%,d coins** and **%,d XP**!",
                        job.getDisplayName(), earnings, xpEarned))
                .setColor(0x57F287);

        event.replyEmbeds(embed.build()).queue();
    }

    private boolean canWork(UserData userData, Instant now) {
        Instant lastWork = userData.getLastWork();
        if (lastWork.equals(Instant.MIN)) {
            return true;
        }
        Duration cooldown = jobManager.getCooldown(userData.getJob());
        return now.isAfter(lastWork.plus(cooldown));
    }

    private int calculateEarnings(UserData userData) {
        JobType job = userData.getJob();
        int baseEarnings = job.getBaseSalary();

        double levelBonus = 1 + (userData.getLevel() * 0.05);
        double inventoryBonus = 1.0; // TODO: inventory bonus

        return (int) (baseEarnings * levelBonus * inventoryBonus);
    }
}