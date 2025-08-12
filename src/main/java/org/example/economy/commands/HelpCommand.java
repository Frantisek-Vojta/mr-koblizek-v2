package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.economy.data.Database;
import org.example.economy.jobs.JobType;

import java.awt.*;

public class HelpCommand extends EconomyCommand {

    public HelpCommand(Database database) {
        super();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("💰 Economy System Help")
                .setDescription("Here's how to use the economy system:")
                .setColor(new Color(88, 101, 242))
                .setThumbnail("https://i.imgur.com/7WdehG0.png");

        // Jobs Section
        StringBuilder jobsDesc = new StringBuilder();
        for (JobType job : JobType.values()) {
            if (job != JobType.UNEMPLOYED) {
                jobsDesc.append(String.format("• %s **%s**: %,d coins/6h\n",
                        "✅", job.getDisplayName(), job.getBaseSalary()));
            }
        }

        embed.addField("🚀 Getting Started",
                "1. First select a job: `/e job select <job>` " + "✅\n" +
                        "2. Work every 6 hours: `/e work` " + "✅\n" +
                        "3. Check your progress: `/e profile` " + "✅", false);

        embed.addField("🔧 Available Jobs", jobsDesc.toString(), false);

        embed.addField("📊 Profile Commands",
                "`/e profile` - View your economy profile " + "✅\n" +
                        "`/e balance` - Check your current balance " + "✅\n" +
                        "`/e baltop` - See server's richest players " + "✅", false);

        embed.addField("🎰 Gambling Commands",
                "`/e slots <bet>` - Play slot machines " + "✅\n" +
                        "`/e coinflip <bet> <heads/tails>` - 50/50 chance " + "🚧 (Soon)", false);

        embed.addField("🛒 Shop Commands",
                "`/e shop` - View available items " + "✅\n" +
                        "`/e buy <item>` - Purchase an item " + "🚧", false);

        embed.addField("⚙️ Job Management",
                "`/e job list` - View all jobs " + "✅\n" +
                        "`/e job select <job>` - Choose a job " + "🚧\n" +
                        "`/e job leave` - Quit your current job " + "🚧", false);

        embed.addField("💸 Money Making",
                "`/e work` - Earn from your job (6h cooldown) " + "✅\n" +
                        "`/e daily` - Daily reward " + "🚧\n" +
                        "`/e weekly` - Weekly reward " + "🚧", false);

        event.replyEmbeds(embed.build())
                .addActionRow(
                        Button.link("https://discord.gg/8rvVrX4xZh", "🔗 Support Server"),
                        Button.secondary("help:more", "❓ More Help")
                )
                .queue();
    }
}