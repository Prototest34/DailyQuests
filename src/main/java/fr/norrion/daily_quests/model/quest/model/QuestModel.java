package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.fileData.QuestRarityData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.model.quest.QuestRarity;
import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import fr.norrion.daily_quests.utils.QuestSound;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class QuestModel {
    protected final int amountNeedMin;
    protected final int amountNeedMax;
    private final String key;
    private final Material material;
    private final String materialAmount;
    private final String name;
    private final int customModel;
    private final List<String> description;
    private final List<QuestReward> rewards;
    private final List<String> rewardText;
    private final QuestSound sound;
    private final QuestRarity rarity;

    protected QuestModel(ConfigurationSection configurationSection, String key) {
        this.key = key;
        this.rarity = QuestRarityData.getRarity(configurationSection.getString("rarity", null));
        this.material = Material.getMaterial(configurationSection.getString("material", "STONE"));
        if (configurationSection.contains("amount-need")) {
            this.amountNeedMax = configurationSection.getInt("amount-need");
            this.amountNeedMin = this.amountNeedMax;
        } else {
            this.amountNeedMax = configurationSection.getInt("amount-need-max");
            this.amountNeedMin = configurationSection.getInt("amount-need-min");
            if (this.amountNeedMin > this.amountNeedMax) {
                throw new ArithmeticException("amount-need-min must be inferior to amount-need-max");
            }
        }
        if (configurationSection.isInt("material-amount")) {
            this.materialAmount = String.valueOf(configurationSection.getInt("material-amount", 1));
        }
        else {
            this.materialAmount = configurationSection.getString("material-amount", "");
        }
        this.customModel = configurationSection.getInt("custom-model", 0);
        this.name = this.rarity.getColor() + configurationSection.getString("name", key).replace("%rarity_color%", this.rarity.getColor());
        this.description = (List<String>) configurationSection.getList("description", new ArrayList<>());
        this.rewards = QuestReward.create(configurationSection, key);
        this.rewardText = (List<String>) configurationSection.getList("reward-text", new ArrayList<>());
        if (configurationSection.contains("sound")) {
            this.sound = new QuestSound(configurationSection.getConfigurationSection("sound"));
        } else {
            this.sound = null;
        }
    }

    public String getKey() {
        return key;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public int getProgressionEnd() {
        Random random = new Random();
        return random.nextInt(this.amountNeedMax -  this.amountNeedMin) + this.amountNeedMin;
    }

    public List<QuestReward> getRewards() {
        return rewards;
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

    public Material getMaterial() {
        return material;
    }

    public String getMaterialAmount() {
        return materialAmount;
    }

    public String getName(Quest quest) {
        return name.replace("%amount-need%", String.valueOf(quest.getProgressionEnd()));
    }

    public int getCustomModel() {
        return customModel;
    }
}
