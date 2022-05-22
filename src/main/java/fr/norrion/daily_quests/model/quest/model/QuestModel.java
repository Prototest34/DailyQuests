package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.fileData.QuestRarityData;
import fr.norrion.daily_quests.model.quest.QuestRarity;
import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import fr.norrion.daily_quests.utils.QuestSound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestModel {
    protected final int amountNeed;
    private final String pattern;
    private final String key;
    private final List<String> description;
    private final List<QuestReward> rewards;
    private final List<String> rewardText;
    private final QuestSound sound;
    private final QuestRarity rarity;

    protected QuestModel(ConfigurationSection configurationSection, String key) {
        String material = configurationSection.getString("material", "STONE");
        int amount = configurationSection.getInt("material-amount", 1);
        String customModel = configurationSection.getString("custom-model", "");
        this.key = key;
        this.rarity = QuestRarityData.getRarity(configurationSection.getString("rarity", null));
        String name = this.rarity.getColor() + configurationSection.getString("name", key).replace("%rarity_color%", this.rarity.getColor());
        this.pattern = material + ":" + amount + ":" + name + ":" + customModel;
        this.amountNeed = configurationSection.getInt("amount-need");
        this.description = (List<String>) configurationSection.getList("description", new ArrayList<>());
        this.rewards = QuestReward.create(configurationSection, key);
        this.rewardText = (List<String>) configurationSection.getList("reward-text", new ArrayList<>());
        if (configurationSection.contains("sound")) {
            this.sound = new QuestSound(configurationSection.getConfigurationSection("sound"));
        } else {
            this.sound = null;
        }
    }

    public String getPattern() {
        return pattern;
    }

    public String getKey() {
        return key;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public abstract int getProgressionEnd();

    public List<QuestReward> getRewards() {
        return rewards;
    }

    public String getName() {
        return pattern.split(":").length > 2 ? pattern.split(":")[2] : "";
    }

    public List<String> getRewardText() {
        return rewardText;
    }

    public void playSound(Player player) {
        if (this.sound != null)
            this.sound.play(player);
    }

    public QuestRarity getRarity() {
        return rarity;
    }
}
