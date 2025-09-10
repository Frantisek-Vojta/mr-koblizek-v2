package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RoleUpdate extends ListenerAdapter {

    private static final long IGNORED_ROLE_ID = 1337335252728676392L;
    private static final long LOG_CHANNEL_ID = 1338111857541779570L;
    private String avatarUrl;

    private void handleRoleChange(Guild guild, List<Role> roles, String userMention, boolean added, String avatarUrl) {
    System.out.println("[ROLE DEBUG] Handling " + (added ? "ADD" : "REMOVE") + " for user: " + userMention);

    for (Role role : roles) {
        System.out.println("[ROLE DEBUG] Processing role: " + role.getName() + " (ID: " + role.getId() + ")");

        if (role.getIdLong() == IGNORED_ROLE_ID) {
            System.out.println("[ROLE DEBUG] Skipping ignored role: " + role.getName());
            continue;
        }

        if (!guild.getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            System.out.println("[ROLE ERROR] Bot lacks VIEW_AUDIT_LOGS permission!");
            // mention role i uživatele, executor bez mention (neznámý)
            sendSimpleLog(guild, userMention, role.getAsMention(), added, avatarUrl, "Unknown (No permission)");
            return;
        }

        guild.retrieveAuditLogs()
                .type(ActionType.MEMBER_ROLE_UPDATE)
                .limit(5)
                .queue(entries -> {
                    String executorMention = "Unknown";
                    for (AuditLogEntry entry : entries) {
                        if (entry.getTargetIdLong() == role.getIdLong() ||
                                System.currentTimeMillis() - entry.getTimeCreated().toInstant().toEpochMilli() < 5000) {
                            if (entry.getUser() != null) {
                                executorMention = entry.getUser().getAsMention(); // MENTION exekutora
                                break;
                            }
                        }
                    }

                    System.out.println("[ROLE DEBUG] Executor found: " + executorMention);
                    sendRoleEmbed(guild, userMention, role.getAsMention(), executorMention, added, avatarUrl);
                }, error -> {
                    System.out.println("[ROLE ERROR] Audit log error: " + error.getMessage());
                    sendSimpleLog(guild, userMention, role.getAsMention(), added, avatarUrl, "Unknown (Error)");
                });
    }
}

private void sendRoleEmbed(Guild guild, String targetUserMention, String roleMention,
                           String executorMention, boolean added, String avatarUrl) {
    TextChannel logChannel = guild.getTextChannelById(LOG_CHANNEL_ID);
    if (logChannel == null) {
        System.out.println("[ROLE ERROR] Log channel not found! ID: " + LOG_CHANNEL_ID);
        return;
    }

    String description = added
            ? String.format("The %s role was added to user %s\nWhen: %s\nBy: %s",
            roleMention, targetUserMention,
            OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
            executorMention)
            : String.format("The %s role was removed from user %s\nWhen: %s\nBy: %s",
            roleMention, targetUserMention,
            OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
            executorMention);

    EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Role Update")
            .setDescription(description)
            .setColor(added ? Color.GREEN : Color.RED)
            .setThumbnail(avatarUrl);

    logChannel.sendMessageEmbeds(embed.build())
            .queue(
                    message -> {
                        System.out.println("[ROLE SUCCESS] Embed sent successfully");
                        addReactions(message, guild);
                    },
                    error -> System.out.println("[ROLE ERROR] Failed to send embed: " + error.getMessage())
            );
}

private void sendSimpleLog(Guild guild, String targetUserMention, String roleMention,
                           boolean added, String avatarUrl, String executorMention) {
    this.avatarUrl = avatarUrl;
    TextChannel logChannel = guild.getTextChannelById(LOG_CHANNEL_ID);
    if (logChannel == null) {
        System.out.println("[ROLE ERROR] Log channel not found for simple log!");
        return;
    }

    String action = added ? "added to" : "removed from";
    String message = String.format("Role %s %s user %s (By: %s)",
            roleMention, action, targetUserMention, executorMention);

    logChannel.sendMessage(message).queue(
            msg -> {
                System.out.println("[ROLE SUCCESS] Simple log sent");
                addReactions(msg, guild);
            },
            error -> System.out.println("[ROLE ERROR] Simple log failed: " + error.getMessage())
    );
}

// Přidá požadované emodži jako reakce k odeslané zprávě
private void addReactions(net.dv8tion.jda.api.entities.Message message, Guild guild) {
    // Unicode reakce
    message.addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("🔥")).queue(); // fire
    message.addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("💀")).queue(); // :skull:
    message.addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("😎")).queue(); // :sunglasses:
    message.addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("🤡")).queue(); // :clown:
    message.addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("🥰")).queue(); // :smiling_face_with_3_hearts:
    message.addReaction(net.dv8tion.jda.api.entities.emoji.Emoji.fromUnicode("🤓")).queue(); // :nerd:


}

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        System.out.println("[ROLE DEBUG] Role ADD event for user: " + event.getUser().getName());
        handleRoleChange(event.getGuild(), event.getRoles(),
            event.getUser().getAsMention(), true, event.getUser().getEffectiveAvatarUrl());
}

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        System.out.println("[ROLE DEBUG] Role REMOVE event for user: " + event.getUser().getName());
        handleRoleChange(event.getGuild(), event.getRoles(),
            event.getUser().getAsMention(), false, event.getUser().getEffectiveAvatarUrl());
}

}