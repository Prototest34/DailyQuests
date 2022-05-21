package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelFish;
import fr.norrion.daily_quests.model.quest.model.QuestModelKill;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestPlayerFishing implements Listener {
    @EventHandler
    public void onPlayerFishing(PlayerFishEvent event) {
        if (!event.isCancelled() && PlayerFishEvent.State.CAUGHT_FISH.equals(event.getState())) {
            Player player = event.getPlayer();
            Item caught = (Item) event.getCaught();
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(player.getUniqueId()).stream()
                            .filter(quest -> quest.getQuestModel() instanceof QuestModelFish)
                            .filter(quest -> ((QuestModelFish) quest.getQuestModel()).getItemFish() == null || isItemFish(caught, ((QuestModelFish) quest.getQuestModel())))
                            .forEach(quest -> quest.addProgressionWithBossBar(1, player));
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }

    private boolean isItemFish(Item itemFish, QuestModelFish questModel) {
        return itemFish.getItemStack().getType().equals(questModel.getItemFish());
    }
}
