package fr.norrion.daily_quests.model.quest.reward;

import fr.norrion.daily_quests.model.quest.Quest;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestReward {
    private final QuestRewardType type;

    protected QuestReward(QuestRewardType type) {
        this.type = type;
    }

    public QuestRewardType getType() {
        return type;
    }

    public abstract void execute(Quest quest);

    static public List<QuestReward> create(MemorySection memorySection, String key) {
        List<String> list = new ArrayList<>();
        List<QuestReward> res = new ArrayList<>();
        for (String reward : memorySection.getString("reward_type").split(",")) {
            if (list.stream().noneMatch(s -> s.equals(reward))) {
                list.add(reward);
            }
        }
        for (String reward : list) {
            switch (QuestRewardType.valueOf(reward.toUpperCase())) {
                case COMMAND -> res.add(QuestRewardCommand.createOne(memorySection, key));
                case ITEMS -> res.add(QuestRewardItems.createOne(memorySection, key));
                case MONEY -> res.add(QuestRewardMoney.createOne(memorySection, key));
            }
            ;
        }
        return res;
    }

    public abstract String getRewardString();
}
