package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class RoleUpdate extends ListenerAdapter {

    private static final long IGNORED_ROLE_ID = 1337335252728676392L; // role, kterou ignorujeme
    private static final long LOG_CHANNEL_ID = 1338111857541779570L; // kanál pro logy

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        for (Role role : event.getRoles()) {
            if (role.getIdLong() == IGNORED_ROLE_ID) return;

            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.MEMBER_ROLE_UPDATE)
                    .limit(1)
                    .queue(entries -> {
                        AuditLogEntry entry = entries.isEmpty() ? null : entries.get(0);
                        String executorName = (entry != null && entry.getUser() != null) ? entry.getUser().getAsTag() : "Unknown";
                        sendRoleChangeEmbed(event.getGuild(), event.getUser().getName(), role.getName(), executorName, true, event.getUser().getEffectiveAvatarUrl());
                    });
        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        for (Role role : event.getRoles()) {
            if (role.getIdLong() == IGNORED_ROLE_ID) return;

            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.MEMBER_ROLE_UPDATE)
                    .limit(1)
                    .queue(entries -> {
                        AuditLogEntry entry = entries.isEmpty() ? null : entries.get(0);
                        String executorName = (entry != null && entry.getUser() != null) ? entry.getUser().getAsTag() : "Unknown";
                        sendRoleChangeEmbed(event.getGuild(), event.getUser().getName(), role.getName(), executorName, false, event.getUser().getEffectiveAvatarUrl());
                    });
        }
    }

    private void sendRoleChangeEmbed(Guild guild, String targetUser, String roleName, String executorName, boolean added, String avatarUrl) {
        TextChannel logChannel = guild.getTextChannelById(LOG_CHANNEL_ID);
        if (logChannel == null) return;

        String actionLine = String.format("user **%s** has been %s role **%s**",
                targetUser, added ? "given" : "removed", roleName);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("role edited")
                .setDescription(actionLine + "\n" +
                        "at: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n" +
                        "by: " + executorName)
                .setColor(added ? Color.GREEN : Color.RED)
                .setThumbnail(avatarUrl);

        logChannel.sendMessageEmbeds(embed.build()).queue();
    }
}
