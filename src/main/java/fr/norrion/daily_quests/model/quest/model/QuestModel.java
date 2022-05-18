package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestModel {
    private final String pattern;
    private final String key;
    private final List<String> description;
    private final List<QuestReward> rewards;
    private final List<String> rewardText;
    protected final int amountNeed;

    protected QuestModel(MemorySection memorySection, String key) {
        String material = memorySection.getString("material", "STONE");
        int amount = memorySection.getInt("material-amount", 1);
        String name = memorySection.getString("name", key);
        String customModel = memorySection.getString("custom-model", "");
        this.amountNeed = memorySection.getInt("amount-need");
        this.key = key;
        this.pattern = material + ":" + amount + ":" + name + ":" + customModel;
        this.description = (List<String>) memorySection.getList("description", new ArrayList<>());
        this.rewards = QuestReward.create(memorySection, key);
        this.rewardText = (List<String>) memorySection.getList("reward-text", new ArrayList<>());
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
}
