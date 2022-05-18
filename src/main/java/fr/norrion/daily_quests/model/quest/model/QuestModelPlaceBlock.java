package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;

public class QuestModelPlaceBlock extends QuestModel {
    private final Material material;

    public QuestModelPlaceBlock(MemorySection memorySection, String key) throws InvalidQuest {
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
