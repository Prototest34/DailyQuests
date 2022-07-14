package fr.norrion.daily_quests.model.quest.model;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class QuestModelFish extends QuestModel {
    private final Material itemFish;

    public QuestModelFish(ConfigurationSection configurationSection, String key) {
        super(configurationSection, key);

        String itemFish = configurationSection.getString("item-fish");
        this.itemFish = itemFish != null ? Material.valueOf(itemFish.toUpperCase()) : null;
    }

    public Material getItemFish() {
        return itemFish;
    }
}
