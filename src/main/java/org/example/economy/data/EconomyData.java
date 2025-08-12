package org.example.economy.data;

import org.example.economy.jobs.JobType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class EconomyData {
    private long coins;
    private JobType currentJob;
    private Instant lastWorkTime;
    private Map<String, Integer> inventory;

    public EconomyData() {
        this.coins = 1000; // Startovací peníze
        this.currentJob = JobType.UNEMPLOYED;
        this.lastWorkTime = Instant.MIN;
        this.inventory = new HashMap<>();
    }

    // Getters and setters
    public long getCoins() { return coins; }
    public void setCoins(long coins) { this.coins = coins; }
    public JobType getCurrentJob() { return currentJob; }
    public void setCurrentJob(JobType currentJob) { this.currentJob = currentJob; }
    public Instant getLastWorkTime() { return lastWorkTime; }
    public void setLastWorkTime(Instant lastWorkTime) { this.lastWorkTime = lastWorkTime; }
    public Map<String, Integer> getInventory() { return inventory; }
}