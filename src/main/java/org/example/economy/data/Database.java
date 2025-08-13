package org.example.economy.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final String DATA_FILE = "economy_data.json";
    private final Map<String, UserData> users;

    public Database() {
        this.users = new HashMap<>();
        initialize();
    }

    /**
     * Načte data ze souboru při startu bota
     */
    public void initialize() {
        try {
            Path path = Paths.get(DATA_FILE);
            if (Files.exists(path)) {
                String content = Files.readString(path);
                JSONObject json = new JSONObject(content);
                JSONArray usersArray = json.getJSONArray("users");

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userJson = usersArray.getJSONObject(i);
                    UserData user = UserData.fromJson(userJson);
                    users.put(user.getUserId(), user);
                }
                System.out.println("[Economy] Načteno " + users.size() + " uživatelů");
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání dat: " + e.getMessage());
        }
    }

    /**
     * Uloží data do souboru
     */
    public void save() {
        try {
            JSONObject json = new JSONObject();
            JSONArray usersArray = new JSONArray();

            for (UserData user : users.values()) {
                usersArray.put(user.toJson());
            }

            json.put("users", usersArray);
            Files.writeString(Paths.get(DATA_FILE), json.toString(2));
            System.out.println("[Economy] Uloženo " + users.size() + " uživatelů");
        } catch (IOException e) {
            System.err.println("Chyba při ukládání dat: " + e.getMessage());
        }
    }

    /**
     * Získá data uživatele
     * @param userId Discord ID uživatele
     * @return UserData objekt
     */
    public UserData getUserData(String userId) {
        return users.computeIfAbsent(userId, k -> {
            UserData newUser = new UserData(userId);
            save(); // Uloží nového uživatele
            return newUser;
        });
    }

    /**
     * Aktualizuje jméno uživatele
     * @param userId Discord ID uživatele
     * @param userName Nové jméno
     */
    public void updateUserName(String userId, String userName) {
        UserData user = getUserData(userId);
        if (!user.getUserName().equals(userName)) {
            user.setUserName(userName);
            save();
        }
    }

    /**
     * @return Všechny uživatele v ekonomice
     */
    public Map<String, UserData> getAllUsers() {
        return new HashMap<>(users);
    }

    /**
     * Přidá peníze uživateli
     */
    public void addCoins(String userId, long amount) {
        UserData user = getUserData(userId);
        user.addCoins(amount);
        save();
    }

    /**
     * Odebere peníze uživateli
     * @return true pokud měl dostatek peněz
     */
    public boolean removeCoins(String userId, long amount) {
        UserData user = getUserData(userId);
        boolean success = user.removeCoins(amount);
        if (success) save();
        return success;
    }
}