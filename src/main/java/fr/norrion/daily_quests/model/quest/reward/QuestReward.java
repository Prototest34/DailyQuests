package fr.norrion.daily_quests.model.quest.reward;

import fr.norrion.daily_quests.model.quest.Quest;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<QuestReward> res = new ArrayList<>();
        String rewardString = memorySection.getString("reward_type");
        if (rewardString != null) {
            Arrays.stream(rewardString.split(",")).distinct().forEach(s -> {
                switch (QuestRewardType.valueOf(s.toUpperCase())) {
                    case COMMAND -> res.add(QuestRewardCommand.createOne(memorySection, key));
                    case ITEMS -> res.add(QuestRewardItems.createOne(memorySection, key));
                    case MONEY -> res.add(QuestRewardMoney.createOne(memorySection, key));
                }
            });
        }
        return res;
    }
}
