package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

public class QuestModelBreakBlock extends QuestModel {
    private Material material;
    private int amount;

    public QuestModelBreakBlock(String pattern, String key, List<String> description, List<QuestReward> rewards, List<String> rewardText) {
        super(pattern, QuestModelType.BREAK_BLOCK, key, description, rewards, rewardText);
    }

    public static QuestModelBreakBlock create(MemorySection memorySection, String key) {
        if (memorySection.contains("material")
                && memorySection.contains("block-to-break")
                && memorySection.contains("amount-to-break")
                && memorySection.contains("rarity")
                && Material.getMaterial(memorySection.getString("block-to-break").toUpperCase()) != null) { //todo add if raryty existe
            String material = memorySection.getString("material");
            int amount = memorySection.contains("amount") ? memorySection.getInt("amount") : 1;
            String name = memorySection.contains("name") ? memorySection.getString("name") : "";
            String customModel = (memorySection.contains("custom-model") && NumberUtils.isNumber(key)) ? memorySection.getString("custom-model") : "";
            String pattern = material + ":" + amount + ":" + name + ":" + customModel;
            String nameBlockToBreak = memorySection.getString("block-to-break");
            int amountBlockToBreak = memorySection.getInt("amount-to-break");
            List<String> description = memorySection.contains("description") ? memorySection.getStringList("description") : new ArrayList<>();
            List<String> rewardText = new ArrayList<>();
            if (memorySection.contains("reward-text") && memorySection.isList("reward-text")) {
                rewardText = memorySection.getStringList("reward-text");
            }
            QuestModelBreakBlock quest = new QuestModelBreakBlock(pattern, key, description, QuestReward.create(memorySection, key), rewardText);
            quest.amount = amountBlockToBreak;
            quest.material = Material.getMaterial(nameBlockToBreak.toUpperCase());
            return quest;
        }
        return null;
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
}
