package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.model.QuestModelHit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestProjectileHitEvent implements Listener {
    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() &&
                event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) &&
                event.getDamager() instanceof Projectile projectile &&
                projectile.getShooter() instanceof Player player) {
            Entity victim = event.getEntity();
            EntityType victimeType = victim.getType();
            Double damage = event.getFinalDamage();
            new BukkitRunnable() {
                @Override
                public void run() {
                    QuestData.getUnfinishedQuest(player.getUniqueId()).stream()
                            .filter(quest -> quest.getQuestModel() instanceof QuestModelHit)
                            .filter(quest -> ((QuestModelHit) quest.getQuestModel()).getEntity() == null || victimeType.equals(((QuestModelHit) quest.getQuestModel()).getEntity()))
                            .filter(quest -> ((QuestModelHit) quest.getQuestModel()).getDamageCause().isIntance(projectile))
                            .forEach(quest -> {
                                if (((QuestModelHit) quest.getQuestModel()).isDealOrHit()) {
                                    quest.addProgressionWithBossBar(damage.intValue(), player);
                                } else {
                                    quest.addProgressionWithBossBar(1, player);
                                }
                            });
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
