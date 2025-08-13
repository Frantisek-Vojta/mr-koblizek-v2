package org.example.economy.data;

import org.example.economy.jobs.JobType;
import org.json.JSONObject;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    private final String userId;
    private String userName;
    private long coins;
    private JobType job;
    private int xp;
    private int level;
    private Instant lastWork;
    private Map<String, Integer> inventory;

    public UserData(String userId) {
        this.userId = userId;
        this.userName = "Unknown";
        this.coins = 1000;
        this.job = JobType.UNEMPLOYED;
        this.xp = 0;
        this.level = 1;
        this.lastWork = Instant.MIN;
        this.inventory = new HashMap<>();
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public long getCoins() { return coins; }
    public void setCoins(long coins) { this.coins = coins; }
    public JobType getJob() { return job; }
    public void setJob(JobType job) { this.job = job; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public Instant getLastWork() { return lastWork; }
    public void setLastWork(Instant lastWork) { this.lastWork = lastWork; }
    public Map<String, Integer> getInventory() { return inventory; }

    public void addCoins(long amount) {
        this.coins += amount;
    }

    public boolean removeCoins(long amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }

    public static UserData fromJson(JSONObject json) {
        UserData user = new UserData(json.getString("userId"));
        user.setUserName(json.optString("userName", "Unknown"));
        user.setCoins(json.getLong("coins"));
        user.setJob(JobType.valueOf(json.getString("job")));
        user.setXp(json.getInt("xp"));
        user.setLevel(json.getInt("level"));
        user.setLastWork(Instant.parse(json.getString("lastWork")));

        JSONObject inventoryJson = json.getJSONObject("inventory");
        for (String key : inventoryJson.keySet()) {
            user.getInventory().put(key, inventoryJson.getInt(key));
        }

        return user;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("userName", userName);
        json.put("coins", coins);
        json.put("job", job.name());
        json.put("xp", xp);
        json.put("level", level);
        json.put("lastWork", lastWork.toString());

        JSONObject inventoryJson = new JSONObject();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            inventoryJson.put(entry.getKey(), entry.getValue());
        }
        json.put("inventory", inventoryJson);

        return json;
    }

    public void addXp(int xpEarned) {
    }
}