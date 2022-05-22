package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.fileData.QuestModelData;
import fr.norrion.daily_quests.model.quest.model.QuestModel;
import fr.norrion.daily_quests.model.quest.reward.QuestRewardType;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestAdminCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                StringBuilder b = new StringBuilder();
                Arrays.stream(args).forEach(s1 -> b.append(s1).append(" "));
                Logger.logDebugMessage("Command executor from: " + commandSender + " command: " + command.getLabel() + "commandString: " + s + " args:" + b);
                if (!(commandSender instanceof Player) || commandSender.hasPermission(MyPermission.ADMIN.getPermission())) {
                    if (args.length > 0) {
                        switch (args[0]) {
                            case "reload" -> new AdminReload().execute(commandSender, args);
                            case "quest" -> new AdminQuest().execute(commandSender, args);
                            case "addQuest" -> new AdminAddQuest().execute(commandSender, args);
                            case "questInfo" -> new AdminQuestInfo().execute(commandSender, args);
                            case "addProgress" -> new AdminAddProgess().execute(commandSender, args);
                            case "setProgress" -> new AdminSetProgress().execute(commandSender, args);
                            case "removeQuest" -> new AdminRemoveQuest().execute(commandSender, args);
                            case "addItemReward" -> new AdminAddItemReward().execute(commandSender, args);
                            case "rarityInfo" -> new AdminRarityInfo().execute(commandSender, args);
                        }
                    }
                } else {
                    Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do" + command.getLabel());
                    commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
                }

            }
        }.runTaskAsynchronously(Main.getInstance());
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1) {
            if (commandSender.hasPermission(MyPermission.ADMIN.getPermission())) {
                list.add("addItemReward");
                list.add("addQuest");
                list.add("addProgress");
                list.add("quest");
                list.add("questInfo");
                list.add("reload");
                list.add("removeQuest");
                list.add("setProgress");
                list.add("rarityInfo");
                list = list.stream().filter(s1 -> s1.startsWith(strings[0])).toList();
            }
        }
        if ((strings[0].equalsIgnoreCase("quest") ||
                strings[0].equalsIgnoreCase("addQuest") ||
                strings[0].equalsIgnoreCase("removeQuest") ||
                strings[0].equalsIgnoreCase("questInfo") ||
                strings[0].equalsIgnoreCase("setProgress") ||
                strings[0].equalsIgnoreCase("addProgress"))
                && strings.length == 2) {
            list.add(Message.COMMAND$MODEL$PLAYER.getString());
            list.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(playerName -> playerName.startsWith(strings[1])).toList());
        }
        if (strings[0].equalsIgnoreCase("addQuest") && strings.length == 3) {
            list.add(Message.COMMAND$MODEL$QUEST_MODEL.getString());
            list.addAll(QuestModelData.getQuestModels().stream().map(QuestModel::getKey).filter(playerName -> playerName.startsWith(strings[2])).toList());
        }
        if (strings[0].equalsIgnoreCase("addItemReward") && strings.length == 2) {
            list.add(Message.COMMAND$MODEL$QUEST_MODEL.getString());
            list.addAll(QuestModelData.getQuestModels().stream()
                    .filter(questModel -> questModel.getRewards().stream().anyMatch(reward -> reward.getType().equals(QuestRewardType.ITEMS)))
                    .map(QuestModel::getKey)
                    .filter(playerName -> playerName.startsWith(strings[1]))
                    .toList());
        }
        if ((strings[0].equalsIgnoreCase("questInfo") ||
                strings[0].equalsIgnoreCase("removeQuest") ||
                strings[0].equalsIgnoreCase("setProgress") ||
                strings[0].equalsIgnoreCase("addProgress")) && strings.length == 3) {
            list.add(Message.COMMAND$MODEL$QUEST_ID.getString());
            //todo check uuid
            list.addAll(QuestData.getQuest(QuestData.getPlayerData(strings[1]).uuid()).stream().map(quest -> String.valueOf(quest.getId())).toList());
        }
        return list;
    }
}
