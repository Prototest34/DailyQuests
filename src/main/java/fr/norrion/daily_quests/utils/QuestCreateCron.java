package fr.norrion.daily_quests.utils;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import org.bukkit.scheduler.BukkitRunnable;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuestCreateCron implements Job {
    private static Scheduler myScheduler;

    public static void createCron() {
        try {
            if (myScheduler != null && myScheduler.isStarted()) {
                myScheduler.shutdown();
            }
            JobDetail createCron = JobBuilder.newJob(QuestCreateCron.class)
                    .withIdentity("jobCreateQuest", "DailyQuest").build();
            CronTrigger myCron = newTrigger()
                    .withIdentity("triggerCreateQuest", "DailyQuest")
                    .withSchedule(cronSchedule(Config.CRON_NORMAL.getString()))
                    .forJob(createCron)
                    .build();

            myScheduler = new StdSchedulerFactory().getScheduler();
            myScheduler.start();
            myScheduler.scheduleJob(createCron, myCron);
            Logger.InfoMessageToServerConsole(Message.SYSTEM$CREATE_CRON_ENABLE.getString());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CREATE_CRON_INVALID.getString());
        }
    }

    public static void disable() {
        try {
            if (myScheduler != null)
                myScheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        new BukkitRunnable() {
            @Override
            public void run() {
                QuestData.giveQuest();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }
}
