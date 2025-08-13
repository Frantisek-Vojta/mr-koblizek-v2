package org.example.economy.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SlotsMachine {
    private static final List<String> SYMBOLS = Arrays.asList(
            "🍒", "🍒",  // Méně časté symboly
            "🍊",
            "🍋",
            "🍇",
            "🍉",
            "7️⃣"  // Vzácnější jackpot symbol
    );
    private static final Random random = new Random();

    public List<List<String>> spin() {
        return Arrays.asList(
                Arrays.asList(getRandomSymbol(), getRandomSymbol(), getRandomSymbol()),
                Arrays.asList(getRandomSymbol(), getRandomSymbol(), getRandomSymbol()),
                Arrays.asList(getRandomSymbol(), getRandomSymbol(), getRandomSymbol())
        );
    }

    private String getRandomSymbol() {
        return SYMBOLS.get(random.nextInt(SYMBOLS.size()));
    }

    public int calculateMultiplier(List<List<String>> grid) {
        // Jackpot - všechny 7️⃣ v hlavní diagonále
        if (grid.get(0).get(0).equals("7️⃣") &&
                grid.get(1).get(1).equals("7️⃣") &&
                grid.get(2).get(2).equals("7️⃣")) {
            return 15;
        }

        // Kontrola řádků
        for (List<String> row : grid) {
            if (row.stream().allMatch(s -> s.equals("7️⃣"))) {
                return 10;
            }
            if (row.stream().allMatch(s -> s.equals(row.get(0)))) {
                return row.get(0).equals("🍒") ? 3 : 5;  // Menší výhra za třešně
            }
        }

        // Kontrola sloupců
        for (int col = 0; col < 3; col++) {
            if (grid.get(0).get(col).equals(grid.get(1).get(col)) &&
                    grid.get(1).get(col).equals(grid.get(2).get(col))) {
                return grid.get(0).get(col).equals("🍒") ? 2 : 4;
            }
        }

        // Kontrola diagonál
        if ((grid.get(0).get(0).equals(grid.get(1).get(1)) &&
                grid.get(1).get(1).equals(grid.get(2).get(2)))) {
            return 3;
        }
        if ((grid.get(0).get(2).equals(grid.get(1).get(1)) &&
                grid.get(1).get(1).equals(grid.get(2).get(0)))) {
            return 3;
        }

        // Dva stejné symboly vedle sebe (horizontálně)
        for (List<String> row : grid) {
            if ((row.get(0).equals(row.get(1)) ||
                    row.get(1).equals(row.get(2))) &&
                    !row.get(0).equals("🍒")) {  // Bez odměny za dvě třešně
                return 1;
            }
        }

        return 0;
    }
}