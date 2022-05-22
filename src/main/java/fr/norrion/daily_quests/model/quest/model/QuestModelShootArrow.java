package fr.norrion.daily_quests.model.quest.model;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

public class QuestModelShootArrow extends QuestModel {
    private final EntityType entity;
    private final PotionEffectType potionEffectType;

    public QuestModelShootArrow(ConfigurationSection configurationSection, String key) {
        super(configurationSection, key);

        String entity = configurationSection.getString("entity", null);
        this.entity = entity != null ? EntityType.valueOf(entity.toUpperCase()) : null;

        String potion = configurationSection.getString("potion-type", null);
        this.potionEffectType = potion != null ? PotionEffectType.getByName(potion.toUpperCase()) : null;
    }


    public EntityType getEntity() {
        return entity;
    }

    public int getAmount() {
        return amountNeed;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    @Override
    public int getProgressionEnd() {
        return amountNeed;
    }
}
