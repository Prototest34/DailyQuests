package fr.norrion.daily_quests.model.quest.reward;

import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.VaultDependency;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

public class QuestRewardMoney extends QuestReward {
    private final double money;

    public QuestRewardMoney(double money) {
        super(QuestRewardType.MONEY);
        this.money = money;
    }

    @Override
    public void execute(Quest quest) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(quest.getUuid());
        VaultDependency.giveMoney(player, money);
    }

    static public QuestRewardMoney createOne(ConfigurationSection configurationSection, String key) {
        double money = configurationSection.getDouble("money");
        return new QuestRewardMoney(money);
    }
}
