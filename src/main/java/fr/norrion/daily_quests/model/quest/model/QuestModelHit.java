package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.utils.enumeration.ListProjectile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class QuestModelHit extends QuestModel {
    private final ListProjectile damageCause;
    private final EntityType entity;
    private final boolean dealOrHit;

    public QuestModelHit(ConfigurationSection configurationSection, String key) {
        super(configurationSection, key);

        String entity = configurationSection.getString("entity");
        this.entity = entity != null ? EntityType.valueOf(entity.toUpperCase()) : null;

        String damageCause = configurationSection.getString("damage-cause");
        this.damageCause = damageCause != null ? ListProjectile.valueOf(damageCause.toUpperCase()) : null;

        this.dealOrHit = configurationSection.getBoolean("deal", false);
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
}
