package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class QuestModelKill extends QuestModel {
    private EntityType entity;
    private int amount;

    public QuestModelKill(String pattern, String key, List<String> description, List<QuestReward> rewards) {
        super(pattern, QuestModelType.KILL, key, description, rewards);

    }

    public EntityType getEntity() {
        return entity;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public int getProgressionEnd() {
        return amount;
    }

    public static QuestModelKill create(MemorySection memorySection, String key) {
        if (memorySection.contains("material")
                && memorySection.contains("entity")
                && memorySection.contains("amount-to-kill")
                && memorySection.contains("rarity")) {
            String material = memorySection.getString("material");
            int amount = memorySection.contains("amount") ? memorySection.getInt("amount") : 1;
            String name = memorySection.contains("name") ? memorySection.getString("name") : "";
            String customModel = (memorySection.contains("custom-model") && NumberUtils.isNumber(key)) ? memorySection.getString("custom-model") : "";
            String pattern = material + ":" + amount + ":" + name + ":" + customModel;
            String entity = memorySection.getString("entity");
            int amountToKill = memorySection.getInt("amount-to-kill");
            List<String> description = memorySection.contains("description") ? memorySection.getStringList("description") : new ArrayList<>();
            QuestModelKill quest = new QuestModelKill(pattern, key, description, QuestReward.create(memorySection, key));
            quest.entity = EntityType.valueOf(entity.toUpperCase());
            quest.amount = amountToKill;
            return quest;
        }
        return null;
    }
}
