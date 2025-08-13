package org.example.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.economy.data.Database;
import org.example.economy.data.UserData;
import org.example.economy.jobs.JobManager;
import org.example.economy.jobs.JobType;

public class JobCommand extends EconomyCommand {
    private final Database database;
    private final JobManager jobManager;

    public JobCommand(Database database, JobManager jobManager) {
        this.database = database;
        this.jobManager = jobManager;
    }

    @Override
    public String getName() {
        return "job";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        database.updateUserName(userId, event.getUser().getName());

        String subcommand = event.getSubcommandName();

        switch (subcommand) {
            case "list":
                handleJobList(event);
                break;
            case "select":
                handleJobSelect(event);
                break;
            case "leave":
                handleJobLeave(event);
                break;
            default:
                event.reply("❌ Unknown subcommand!").setEphemeral(true).queue();
        }
    }

    private void handleJobList(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("💼 Available Jobs")
                .setColor(0x5865F2);

        UserData userData = database.getUserData(event.getUser().getId());
        for (JobType job : JobType.values()) {
            if (job == JobType.UNEMPLOYED) continue;

            String status = getJobStatus(userData, job);

            embed.addField(
                    job.getDisplayName(),
                    String.format("💰 Salary: %,d coins/6h\n%s",
                            job.getBaseSalary(),
                            status),
                    false
            );
        }

        event.replyEmbeds(embed.build()).queue();
    }

    private String getJobStatus(UserData userData, JobType job) {
        JobType currentJob = userData.getJob();
        if (currentJob != null && currentJob.equals(job)) {
            return "✅ Currently employed";
        }

        if (job == JobType.CEO && userData.getLevel() < 20) {
            return "🔒 Requires Level 20";
        }

        return "🔹 Use `/e job select " + job.name().toLowerCase() + "`";
    }

    private void handleJobSelect(SlashCommandInteractionEvent event) {
        String jobName = event.getOption("job").getAsString().toUpperCase();
        User user = event.getUser();
        UserData userData = database.getUserData(user.getId());

        try {
            JobType newJob = JobType.valueOf(jobName);

            if (newJob == JobType.UNEMPLOYED) {
                event.reply("❌ You can't select 'Unemployed'! Use `/e job leave` instead.").queue();
                return;
            }

            if (newJob == JobType.CEO && userData.getLevel() < 20) {
                event.reply("❌ You need to be at least Level 20 to become a CEO!").queue();
                return;
            }

            userData.setJob(newJob);
            database.save();

            event.reply("✅ You are now working as a **" + newJob.getDisplayName() + "**!").queue();

        } catch (IllegalArgumentException e) {
            event.reply("❌ Invalid job! Use `/e job list` to see available jobs.").queue();
        }
    }

    private void handleJobLeave(SlashCommandInteractionEvent event) {
        UserData userData = database.getUserData(event.getUser().getId());
        JobType currentJob = userData.getJob();

        if (currentJob == null || currentJob == JobType.UNEMPLOYED) {
            event.reply("❌ You don't have a job to leave!").queue();
            return;
        }

        userData.setJob(JobType.UNEMPLOYED);
        database.save();

        event.reply("✅ You left your job. Use `/e job list` to find a new one.").queue();
    }
}
