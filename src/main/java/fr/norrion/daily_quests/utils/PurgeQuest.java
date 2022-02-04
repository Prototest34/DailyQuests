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

public class PurgeQuest implements Job {
    private static Scheduler myScheduler;

    public static void createPurge() {
        try {
            JobDetail purgeQuest = JobBuilder.newJob(PurgeQuest.class)
                    .withIdentity("jobPurgeQuest", "DailyQuest").build();
            CronTrigger myCron = newTrigger()
                    .withIdentity("triggerPurgeQuest", "DailyQuest")
                    .withSchedule(cronSchedule(Config.PURGECRON.getString()))
                    .forJob(purgeQuest)
                    .build();

            myScheduler = new StdSchedulerFactory().getScheduler();
            myScheduler.start();
            myScheduler.scheduleJob(purgeQuest, myCron);
            Logger.InfoMessageToServerConsole(Message.SYSTEM$PURGECRON_ENABLE.getString());
        } catch (Exception e) {
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$PURGECRON_INVALID.getString());
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
                QuestData.purge();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }
}
