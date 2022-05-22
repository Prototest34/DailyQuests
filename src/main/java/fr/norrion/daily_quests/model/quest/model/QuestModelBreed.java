package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class QuestModelBreed extends QuestModel {
    private final EntityType entity;

    public QuestModelBreed(ConfigurationSection configurationSection, String key) throws InvalidQuest {
        super(configurationSection, key);

        String entity = configurationSection.getString("entity");
        if (entity == null) {
            throw new InvalidQuest();
        }
        this.entity = EntityType.valueOf(entity.toUpperCase());
    }

    public EntityType getEntity() {
        return entity;
    }

    @Override
    public int getProgressionEnd() {
        return amountNeed;
    }
}
