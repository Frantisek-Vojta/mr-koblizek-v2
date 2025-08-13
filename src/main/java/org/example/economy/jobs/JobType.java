package org.example.economy.jobs;

import java.time.Duration;

public enum JobType {
    UNEMPLOYED("Unemployed", 0, Duration.ZERO, 0),
    MINER("Miner", 50, Duration.ofHours(6), 0),
    FISHER("Fisher", 40, Duration.ofHours(6), 100),
    LUMBERJACK("Lumberjack", 45, Duration.ofHours(6), 200),
    PROGRAMMER("Programmer", 80, Duration.ofHours(6), 300),
    CEO("CEO", 200, Duration.ofHours(6), 500);

    private final String displayName;
    private final int baseSalary;
    private final Duration cooldown;
    private final int requiredXp;

    JobType(String displayName, int baseSalary, Duration cooldown, int requiredXp) {
        this.displayName = displayName;
        this.baseSalary = baseSalary;
        this.cooldown = cooldown;
        this.requiredXp = requiredXp;
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

    public int getRequiredXp() {
        return requiredXp;
    }
}