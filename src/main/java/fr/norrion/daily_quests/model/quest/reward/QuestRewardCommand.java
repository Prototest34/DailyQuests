package fr.norrion.daily_quests.model.quest.reward;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class QuestRewardCommand extends QuestReward {
    public final List<String> commands;

    public QuestRewardCommand(List<String> commands) {
        super(QuestRewardType.COMMAND);
        this.commands = commands;
    }

    @Override
    public void execute(Quest quest) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String command : commands) {
                    String playerName = QuestData.getPlayerName(quest.getUuid());
                    if (command.startsWith("dqmessage:") && command.split(":").length > 3) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 2; i < command.split(":").length; i++) {
                            sb.append(command.split(":")[i]);
                            if (i + 1 > command.split(":").length) {
                                sb.append(":");
                            }
                        }
                        if (Bukkit.getPlayer(command.split(":")[1].replace("%player%", playerName)) != null)
                            Bukkit.getPlayer(command.split(":")[1].replace("%player%", playerName)).sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    } else {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command
                                .replace("%player%", playerName)
                                .replace("%quest_name%", quest.getQuestModel().getName())
                        );
                    }
                }
            }
        }.runTask(Main.getInstance());
    }

    static public QuestRewardCommand createOne(ConfigurationSection configurationSection, String key) {
        List<String> commands = configurationSection.getStringList("commands");
        return new QuestRewardCommand(commands);
    }
}
