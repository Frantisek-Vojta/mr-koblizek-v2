package org.example.economy.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class EmbedUtils {
    public static EmbedBuilder createEconomyEmbed(User user) {
        return new EmbedBuilder()
                .setColor(0x5865F2)
                .setFooter(user.getName(), user.getEffectiveAvatarUrl())
                .setTimestamp(java.time.Instant.now());
    }

    public static EmbedBuilder createSuccessEmbed(User user, String title) {
        return createEconomyEmbed(user)
                .setTitle("✅ " + title)
                .setColor(0x57F287);
    }

    public static EmbedBuilder createErrorEmbed(User user, String title) {
        return createEconomyEmbed(user)
                .setTitle("❌ " + title)
                .setColor(0xED4245);
    }
}