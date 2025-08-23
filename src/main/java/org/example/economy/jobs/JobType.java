package org.example.economy.jobs;
import java.time.Duration;  // Add this import
public enum JobType {
    UNEMPLOYED("Unemployed", 0, Duration.ZERO),
    MINER("Miner", 50, Duration.ofHours(6)),
    FISHER("Fisher", 40, Duration.ofHours(6)),
    LUMBERJACK("Lumberjack", 45, Duration.ofHours(6)),
    PROGRAMMER("Programmer", 80, Duration.ofHours(6)),
    CEO("CEO", 200, Duration.ofHours(6));

    private final String displayName;
    private final int baseSalary;
    private final Duration cooldown;

    JobType(String displayName, int baseSalary, Duration cooldown) {
        this.displayName = displayName;
        this.baseSalary = baseSalary;
        this.cooldown = cooldown;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBaseSalary() {
        return baseSalary;
    }

    public Duration getCooldown() {
        return cooldown;
    }

    public int getRequiredXp() {return 100;}
    }
