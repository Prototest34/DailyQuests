package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

public class QuestModelBreakBlock extends QuestModel {
    private Material material;

    public QuestModelBreakBlock(MemorySection memorySection, String key) throws InvalidQuest {
        super(memorySection, key);

        String material = memorySection.getString("material");
        if (material == null) {
            throw new InvalidQuest();
        }
        this.material = Material.getMaterial(material.toUpperCase());
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public int getProgressionEnd() {
        return amountNeed;
    }
}
