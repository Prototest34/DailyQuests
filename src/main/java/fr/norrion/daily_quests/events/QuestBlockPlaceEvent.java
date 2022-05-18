package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelPlaceBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestBlockPlaceEvent implements Listener {
    @EventHandler
    public void onPlaceBreak(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            Material materialBlock = event.getBlock().getType();
            Player player = event.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(event.getPlayer().getUniqueId()).stream()
                            .filter(quest -> quest.getQuestModel() instanceof QuestModelPlaceBlock)
                            .filter(quest -> materialBlock.equals(((QuestModelPlaceBlock) quest.getQuestModel()).getMaterial()))
                            .forEach(quest -> quest.addProgressionWithBossBar(1, player));
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
