package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.exeption.InvalidQuest;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class QuestModelBreakBlock extends QuestModel {
    private Material material;

    public QuestModelBreakBlock(ConfigurationSection configurationSection, String key) throws InvalidQuest {
        super(configurationSection, key);

        String material = configurationSection.getString("material");
        if (material == null) {
            throw new InvalidQuest();
        }
        this.material = Material.getMaterial(material.toUpperCase());
    }

    public Material getMaterial() {
        return material;
    }
}
