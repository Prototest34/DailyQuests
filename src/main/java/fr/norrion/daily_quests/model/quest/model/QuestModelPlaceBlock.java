package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

public class QuestModelPlaceBlock extends QuestModel {
    private Material material;
    private int amount;

    public QuestModelPlaceBlock(String pattern, String key, List<String> description, List<QuestReward> rewards) {
        super(pattern, QuestModelType.PLACE_BLOCK, key, description, rewards);
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public int getProgressionEnd() {
        return amount;
    }

    public static QuestModelPlaceBlock create(MemorySection memorySection, String key) {
        if (memorySection.contains("material")
                && memorySection.contains("block-to-place")
                && memorySection.contains("amount-to-place")
                && memorySection.contains("rarity")
                && Material.getMaterial(memorySection.getString("block-to-place").toUpperCase()) != null) { //todo add if raryty existe
            String material = memorySection.getString("material");
            int amount = memorySection.contains("amount") ? memorySection.getInt("amount") : 1;
            String name = memorySection.contains("name") ? memorySection.getString("name") : "";
            String customModel = (memorySection.contains("custom-model") && NumberUtils.isNumber(key)) ? memorySection.getString("custom-model") : "";
            String pattern = material + ":" + amount + ":" + name + ":" + customModel;
            String nameBlockToPlace = memorySection.getString("block-to-place");
            int amountBlockToPlace = memorySection.getInt("amount-to-place");
            List<String> description = memorySection.contains("description") ? memorySection.getStringList("description") : new ArrayList<>();
            QuestModelPlaceBlock quest = new QuestModelPlaceBlock(pattern, key, description, QuestReward.create(memorySection, key));
            quest.material = Material.getMaterial(nameBlockToPlace.toUpperCase());
            quest.amount = amountBlockToPlace;
            return quest;
        }
        return null;
    }
}
