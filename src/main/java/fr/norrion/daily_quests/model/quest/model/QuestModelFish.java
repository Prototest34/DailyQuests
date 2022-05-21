package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;

public class QuestModelFish extends QuestModel {
    private final Material itemFish;

    public QuestModelFish(MemorySection memorySection, String key) {
        super(memorySection, key);

        String itemFish = memorySection.getString("item-fish");
        this.itemFish = itemFish != null ? Material.valueOf(itemFish.toUpperCase()) : null;
    }

    public Material getItemFish() {
        return itemFish;
    }

    @Override
    public int getProgressionEnd() {
        return amountNeed;
    }
}
