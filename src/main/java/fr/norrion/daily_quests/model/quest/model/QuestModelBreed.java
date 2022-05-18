package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

public class QuestModelBreed extends QuestModel {
    private final EntityType entity;

    public QuestModelBreed(MemorySection memorySection, String key) throws InvalidQuest {
        super(memorySection, key);

        String entity = memorySection.getString("entity");
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
