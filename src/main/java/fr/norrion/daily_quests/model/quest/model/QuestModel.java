package fr.norrion.daily_quests.model.quest.model;

import fr.norrion.daily_quests.model.quest.reward.QuestReward;

import java.util.List;

public abstract class QuestModel {
    private final String pattern;
    private final QuestModelType type;
    private final String key;
    private final List<String> description;
    private final List<QuestReward> rewards;

    public QuestModel(String pattern, QuestModelType type, String key, List<String> description, List<QuestReward> rewards) {
        this.pattern = pattern;
        this.type = type;
        this.key = key;
        this.description = description;
        this.rewards = rewards;
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

    public QuestModelType getType() {
        return type;
    }

    public abstract int getProgressionEnd();

    public List<QuestReward> getRewards() {
        return rewards;
    }

    public String getName() {
        return pattern.split(":").length > 2 ? pattern.split(":")[2] : "";
    }
}
