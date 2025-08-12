package org.example.economy.shop;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    private final List<ShopItem> shopItems;

    public ShopManager() {
        this.shopItems = new ArrayList<>();
        initializeShop();
    }

    private void initializeShop() {
        shopItems.add(new ShopItem(
            "pickaxe", 
            "Better Pickaxe", 
            "Increases mining earnings by 20% for 3 days", 
            500, 
            3
        ));
        
        shopItems.add(new ShopItem(
            "lucky_charm", 
            "Lucky Charm", 
            "Increases slot machine win chance by 15% for 7 days", 
            1000, 
            7
        ));
    }

    public List<ShopItem> getAvailableItems() {
        return new ArrayList<>(shopItems);
    }

    public ShopItem getItemById(String id) {
        return shopItems.stream()
            .filter(item -> item.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
}