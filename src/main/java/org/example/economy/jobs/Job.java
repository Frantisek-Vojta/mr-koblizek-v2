package org.example.economy.jobs;

import java.time.Duration;

public class Job {
    private final JobType type;
    private int level;
    private int xp;

    public Job(JobType type) {
        this.type = type;
        this.level = 1;
        this.xp = 0;
    }

    public int calculateEarnings() {
        return (int) (type.getBaseSalary() * (1 + (level * 0.05)));
    }

    public void addXp(int amount) {
        xp += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int xpNeeded = 100 * level;
        if (xp >= xpNeeded) {
            level++;
            xp -= xpNeeded;
        }
    }

    // Getters
    public JobType getType() { return type; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
}