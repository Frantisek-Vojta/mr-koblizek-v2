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

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        handleRoleChange(event.getGuild(), event.getRoles(),
                event.getUser().getName(), true, event.getUser().getEffectiveAvatarUrl());
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        handleRoleChange(event.getGuild(), event.getRoles(),
                event.getUser().getName(), false, event.getUser().getEffectiveAvatarUrl());
    }

    private void handleRoleChange(Guild guild, List<Role> roles, String userName, boolean added, String avatarUrl) {
        for (Role role : roles) {
            if (role.getIdLong() == IGNORED_ROLE_ID) return;

            if (!guild.getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
                System.out.println("[ERROR] Bot lacks VIEW_AUDIT_LOGS permission!");
                return;
            }

            guild.retrieveAuditLogs()
                    .type(ActionType.MEMBER_ROLE_UPDATE)
                    .limit(1)
                    .queue(entries -> {
                        AuditLogEntry entry = entries.isEmpty() ? null : entries.get(0);
                        String executorName = (entry != null && entry.getUser() != null)
                                ? entry.getUser().getEffectiveName()
                                : "System";
                        sendRoleEmbed(guild, userName, role.getName(), executorName, added, avatarUrl);
                    }, error -> {
                        System.out.println("[ERROR] Failed to fetch audit logs: " + error.getMessage());
                    });
        }
    }

    private void sendRoleEmbed(Guild guild, String targetUser, String roleName,
                               String executorName, boolean added, String avatarUrl) {
        TextChannel logChannel = guild.getTextChannelById(LOG_CHANNEL_ID);
        if (logChannel == null) {
            System.out.println("[ERROR] Log channel not found!");
            return;
        }

        String description = added
                ? String.format("The **%s** role was added to user **%s**\nWhen: %s\nBy: %s",
                roleName, targetUser,
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                executorName)
                : String.format("The **%s** role was removed from user **%s**\nWhen: %s\nBy: %s",
                roleName, targetUser,
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                executorName);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Rank Movement")
                .setDescription(description)
                .setColor(added ? Color.GREEN : Color.RED)
                .setThumbnail(avatarUrl);

        logChannel.sendMessageEmbeds(embed.build())
                .queue(success -> {}, error -> {
                    System.out.println("[ERROR] Failed to send embed: " + error.getMessage());
                });
    }
}
