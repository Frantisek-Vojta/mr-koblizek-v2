package org.example.economy.jobs;

import org.example.economy.data.Database;
import org.example.economy.data.UserData;

import java.time.Duration;
import java.time.Instant;

public class JobManager {
    private final Database database;

    public JobManager(Database database) {
        this.database = database;
    }

    // Zajistí, že uživatel má vždy alespoň MINER
    public JobType ensureDefaultJob(UserData userData) {
        JobType job = userData.getJob();
        if (job == null || job == JobType.UNEMPLOYED) {
            userData.setJob(JobType.MINER);
            database.save();
            return JobType.MINER;
        }
        return job;
    }

    public boolean canWork(UserData userData) {
        JobType job = ensureDefaultJob(userData);

        Instant lastWorkTime = userData.getLastWork();
        if (lastWorkTime.equals(Instant.MIN)) {
            return true;
        }

        Duration cooldown = getCooldown(job);
        return Instant.now().isAfter(lastWorkTime.plus(cooldown));
    }

    public Duration getCooldown(JobType job) {
        return Duration.ofHours(6);
    }

    public String getCooldownMessage(UserData userData) {
        JobType job = ensureDefaultJob(userData);

        Instant lastWorkTime = userData.getLastWork();
        if (lastWorkTime.equals(Instant.MIN)) {
            return "Ready to work now!";
        }

        Instant nextWork = lastWorkTime.plus(getCooldown(job));
        Duration remaining = Duration.between(Instant.now(), nextWork);
        if (!remaining.isNegative()) {
            long h = remaining.toHoursPart();
            long m = remaining.toMinutesPart();
            return String.format("%dh %dm", h, m);
        }
        return "Ready to work now!";
    }

    public void work(UserData userData) {
        JobType currentJob = ensureDefaultJob(userData);

        int earnings = calculateEarnings(userData);
        userData.addCoins(earnings);
        userData.setLastWork(Instant.now());
        database.save();
    }

    private int calculateEarnings(UserData userData) {
        JobType job = ensureDefaultJob(userData);
        int level = Math.max(1, userData.getLevel());
        return job.getBaseSalary() * level;
    }
}