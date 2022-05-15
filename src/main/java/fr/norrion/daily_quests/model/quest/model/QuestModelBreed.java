package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class QuestModelBreed extends QuestModel {
    private EntityType entity;
    private int amount;

    public QuestModelBreed(String pattern, String key, List<String> description, List<QuestReward> rewards, List<String> rewardText) {
        super(pattern, QuestModelType.BREED, key, description, rewards, rewardText);

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

    public static QuestModelBreed create(MemorySection memorySection, String key) {
        if (memorySection.contains("material")
                && memorySection.contains("entity")
                && memorySection.contains("amount-to-breed")
                && memorySection.contains("rarity")) {
            String material = memorySection.getString("material");
            int amount = memorySection.contains("amount") ? memorySection.getInt("amount") : 1;
            String name = memorySection.contains("name") ? memorySection.getString("name") : "";
            String customModel = (memorySection.contains("custom-model") && NumberUtils.isNumber(key)) ? memorySection.getString("custom-model") : "";
            String pattern = material + ":" + amount + ":" + name + ":" + customModel;
            String entity = memorySection.getString("entity");
            int amountToKill = memorySection.getInt("amount-to-breed");
            List<String> description = memorySection.contains("description") ? memorySection.getStringList("description") : new ArrayList<>();
            List<String> rewardText = new ArrayList<>();
            if (memorySection.contains("reward-text") && memorySection.isList("reward-text")) {
                rewardText = memorySection.getStringList("reward-text");
            }
            QuestModelBreed quest = new QuestModelBreed(pattern, key, description, QuestReward.create(memorySection, key), rewardText);
            quest.entity = EntityType.valueOf(entity.toUpperCase());
            quest.amount = amountToKill;
            return quest;
        }
        return null;
    }
}
