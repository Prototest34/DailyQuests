package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelPlaceBlock;
import fr.norrion.daily_quests.model.quest.model.QuestModelType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestBlockPlaceEvent implements Listener {
    @EventHandler
    public void onPlaceBreak(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            Material materialBlock = event.getBlock().getType();
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(event.getPlayer().getName()).stream()
                            .filter(quest -> QuestModelType.PLACE_BLOCK.equals(quest.getQuestModel().getType()))
                            .filter(quest -> materialBlock.equals(((QuestModelPlaceBlock) quest.getQuestModel()).getMaterial()))
                            .forEach(quest -> quest.addProgression(1));
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
