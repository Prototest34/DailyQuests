package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.utils.enumeration.ListProjectile;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

public class QuestModelHit extends QuestModel {
    private final ListProjectile damageCause;
    private final EntityType entity;
    private final boolean dealOrHit;

    public QuestModelHit(MemorySection memorySection, String key) {
        super(memorySection, key);

        String entity = memorySection.getString("entity");
        this.entity = entity != null ? EntityType.valueOf(entity.toUpperCase()) : null;

        String damageCause = memorySection.getString("damage-cause");
        this.damageCause = damageCause != null ? ListProjectile.valueOf(damageCause.toUpperCase()) : null;

        this.dealOrHit = memorySection.getBoolean("deal", false);
    }

    public ListProjectile getDamageCause() {
        return damageCause;
    }

    public EntityType getEntity() {
        return entity;
    }

    public boolean isDealOrHit() {
        return dealOrHit;
    }

    @Override
    public int getProgressionEnd() {
        return amountNeed;
    }
}
