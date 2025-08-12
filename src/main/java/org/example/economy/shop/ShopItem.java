package org.example.economy.shop;

public class ShopItem {
    private final String id;
    private final String name;
    private final String description;
    private final int price;
    private final int durationDays; // Pro dočasné efekty

    public ShopItem(String id, String name, String description, int price, int durationDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getDurationDays() { return durationDays; }
}