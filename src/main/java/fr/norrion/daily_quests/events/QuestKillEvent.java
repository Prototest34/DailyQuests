package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelKill;
import fr.norrion.daily_quests.model.quest.model.QuestModelType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestKillEvent implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        Entity victim = event.getEntity();
        EntityType victimeType = victim.getType();
        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(player.getUniqueId()).stream()
                            .filter(quest -> QuestModelType.KILL.equals(quest.getQuestModel().getType()))
                            .filter(quest -> victimeType.equals(((QuestModelKill) quest.getQuestModel()).getEntity()))
                            .forEach(quest -> quest.addProgression(1));
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
