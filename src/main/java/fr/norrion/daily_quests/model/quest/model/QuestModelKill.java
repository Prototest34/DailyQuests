package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class QuestModelKill extends QuestModel {
    private final EntityType entity;

    public QuestModelKill(MemorySection memorySection, String key) throws InvalidQuest {
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
