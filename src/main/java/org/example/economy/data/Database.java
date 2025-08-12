package org.example.economy.data;

import org.example.economy.jobs.JobType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final String DATA_FILE = "economy_data.json";
    private Map<String, UserData> users;

    public Database() {
        this.users = new HashMap<>();
    }

    public void initialize() {
        try {
            Path path = Paths.get(DATA_FILE);
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                JSONObject json = new JSONObject(content);
                JSONArray usersArray = json.getJSONArray("users");
                
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userJson = usersArray.getJSONObject(i);
                    UserData user = UserData.fromJson(userJson);
                    users.put(user.getUserId(), user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading economy data: " + e.getMessage());
        }
    }

    public void save() {
        try {
            JSONObject json = new JSONObject();
            JSONArray usersArray = new JSONArray();
            
            for (UserData user : users.values()) {
                usersArray.put(user.toJson());
            }
            
            json.put("users", usersArray);
            Files.write(Paths.get(DATA_FILE), json.toString().getBytes());
        } catch (IOException e) {
            System.err.println("Error saving economy data: " + e.getMessage());
        }
    }

    public UserData getUserData(String userId) {
        return users.computeIfAbsent(userId, k -> new UserData(userId));
    }

    public Map<String, UserData> getAllUsers() {
        return new HashMap<>(users);
    }
}