package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelBreed;
import fr.norrion.daily_quests.model.quest.model.QuestModelType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestEntityBreedEvent implements Listener {
    @EventHandler
    public void onPlayerBreedEvent(EntityBreedEvent event) {
        if (!event.isCancelled() && event.getBreeder() instanceof Player player) {
            Entity entity = event.getEntity();
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(player.getUniqueId()).stream()
                            .filter(quest -> quest.getQuestModel() instanceof QuestModelBreed)
                            .filter(quest -> entity.getType().equals(((QuestModelBreed) quest.getQuestModel()).getEntity()))
                            .forEach(quest -> quest.addProgressionWithBossBar(1, player));
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
