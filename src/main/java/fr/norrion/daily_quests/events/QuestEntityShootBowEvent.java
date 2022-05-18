package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelPlaceBlock;
import fr.norrion.daily_quests.model.quest.model.QuestModelShootArrow;
import fr.norrion.daily_quests.model.quest.model.QuestModelType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class QuestEntityShootBowEvent implements Listener {
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player player && event.getProjectile() instanceof Arrow arrow) {
            List<PotionEffectType> list = arrow.getCustomEffects().stream().map(PotionEffect::getType).collect(Collectors.toList());
            PotionEffectType potionEffectType = arrow.getBasePotionData().getType().getEffectType();
            list.add(potionEffectType);
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(player.getUniqueId()).stream()
                            .filter(quest -> quest.getQuestModel() instanceof QuestModelShootArrow)
                            .filter(quest -> ((QuestModelShootArrow) quest.getQuestModel()).getPotionEffectType() == null || list.contains(((QuestModelShootArrow) quest.getQuestModel()).getPotionEffectType()))
                            .forEach(quest -> quest.addProgressionWithBossBar(1, player));
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
