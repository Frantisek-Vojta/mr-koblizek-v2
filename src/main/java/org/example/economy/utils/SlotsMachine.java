package org.example.economy.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SlotsMachine {
    private static final List<String> SYMBOLS = Arrays.asList("🍒", "🍊", "🍋", "🍇", "🍉", "7️⃣");
    private static final Random random = new Random();

    public List<String> spin() {
        return Arrays.asList(
            SYMBOLS.get(random.nextInt(SYMBOLS.size())),
            SYMBOLS.get(random.nextInt(SYMBOLS.size())),
            SYMBOLS.get(random.nextInt(SYMBOLS.size()))
        );
    }

    public int calculateMultiplier(List<String> result) {
        if (result.get(0).equals("7️⃣") && result.get(1).equals("7️⃣") && result.get(2).equals("7️⃣")) {
            return 10;
        }
        if (result.get(0).equals(result.get(1)) && result.get(1).equals(result.get(2))) {
            return 5;
        }
        if (result.get(0).equals(result.get(1)) || result.get(1).equals(result.get(2))) {
            return 2;
        }
        return 0;
    }
}