package fr.norrion.daily_quests.model.quest.reward;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.model.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
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
                    if (command.startsWith("dqmessage:") && command.split(":").length > 3) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 2; i < command.split(":").length; i++) {
                            sb.append(command.split(":")[i]);
                            if (i + 1 > command.split(":").length) {
                                sb.append(":");
                            }
                        }
                        if (Bukkit.getPlayer(command.split(":")[1].replace("%player%", quest.getPlayerName())) != null)
                            Bukkit.getPlayer(command.split(":")[1].replace("%player%", quest.getPlayerName())).sendMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    } else {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command
                                .replace("%player%", quest.getPlayerName())
                                .replace("%quest_name%", quest.getQuestModel().getName())
                        );
                    }
                }
            }
        }.runTask(Main.getInstance());
    }

    @Override
    public String getRewardString() {
        return null;
    }

    static public QuestRewardCommand createOne(MemorySection memorySection, String key) {
        List<String> commands = memorySection.getStringList("commands");
        return new QuestRewardCommand(commands);
    }
}
