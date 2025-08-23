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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoleUpdate extends ListenerAdapter {

    private static final long IGNORED_ROLE_ID = 1337335252728676392L;
    private static final long LOG_CHANNEL_ID = 1338111857541779570L;

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        // Přidáme malé zpoždění pro audit logy
        event.getGuild().retrieveAuditLogs().type(ActionType.MEMBER_ROLE_UPDATE).limit(5).queueAfter(1, TimeUnit.SECONDS, entries -> {
            handleRoleChange(event.getGuild(), event.getRoles(), event.getUser(), true, entries);
        });
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        // Přidáme malé zpoždění pro audit logy
        event.getGuild().retrieveAuditLogs().type(ActionType.MEMBER_ROLE_UPDATE).limit(5).queueAfter(1, TimeUnit.SECONDS, entries -> {
            handleRoleChange(event.getGuild(), event.getRoles(), event.getUser(), false, entries);
        });
    }

    private void handleRoleChange(Guild guild, List<Role> roles, net.dv8tion.jda.api.entities.User targetUser, boolean added, List<AuditLogEntry> auditLogEntries) {
        // Nejprve zkontrolujte ignorované role
        if (roles.stream().anyMatch(role -> role.getIdLong() == IGNORED_ROLE_ID)) {
            return;
        }

        // Zkontrolujte oprávnění pro audit logy
        if (!guild.getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            sendRoleEmbed(guild, targetUser, roles, "Unknown (No Permission)", added);
            return;
        }

        // Najděte nejnovější relevantní audit log entry
        AuditLogEntry relevantEntry = null;
        for (AuditLogEntry entry : auditLogEntries) {
            if (entry.getTargetIdLong() == targetUser.getIdLong()) {
                relevantEntry = entry;
                break;
            }
        }

        String executorName = "Unknown";
        if (relevantEntry != null && relevantEntry.getUser() != null) {
            executorName = relevantEntry.getUser().getAsTag();
        }

        sendRoleEmbed(guild, targetUser, roles, executorName, added);
    }

    private void sendRoleEmbed(Guild guild, net.dv8tion.jda.api.entities.User targetUser, List<Role> roles, String executorName, boolean added) {
        TextChannel logChannel = guild.getTextChannelById(LOG_CHANNEL_ID);
        if (logChannel == null) {
            System.out.println("[ERROR] Log channel not found!");
            return;
        }

        // Zkontrolujte oprávnění pro odesílání zpráv
        if (!guild.getSelfMember().hasPermission(logChannel, Permission.MESSAGE_SEND)) {
            System.out.println("[ERROR] Bot cannot send messages in log channel!");
            return;
        }

        // Vytvořte popis s informacemi o všech změněných rolích
        StringBuilder roleList = new StringBuilder();
        for (Role role : roles) {
            if (role.getIdLong() != IGNORED_ROLE_ID) {
                roleList.append(role.getAsMention()).append(", ");
            }
        }

        // Odstraníme poslední čárku a mezeru
        if (roleList.length() > 0) {
            roleList.setLength(roleList.length() - 2);
        }

        String action = added ? "added to" : "removed from";
        String description = String.format("**Roles %s**: %s\n**User**: %s\n**By**: %s\n**When**: %s",
                action, roleList.toString(), targetUser.getAsMention(), executorName,
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(added ? "✅ Roles Added" : "❌ Roles Removed")
                .setDescription(description)
                .setColor(added ? Color.GREEN : Color.RED)
                .setThumbnail(targetUser.getEffectiveAvatarUrl())
                .setFooter("Guild: " + guild.getName(), guild.getIconUrl())
                .setTimestamp(Instant.now());

        logChannel.sendMessageEmbeds(embed.build()).queue(
                success -> System.out.println("[INFO] Role change logged successfully"),
                error -> System.out.println("[ERROR] Failed to send embed: " + error.getMessage())
        );
    }
}