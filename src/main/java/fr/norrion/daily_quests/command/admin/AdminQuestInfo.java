package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.PlayerData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.Logger;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class AdminQuestInfo implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$QUEST_INFO.getPermission())) {
            if (args.length > 2 && NumberUtils.isNumber(args[2])) {
                String playerName = args[1];
                int id = Integer.parseInt(args[2]);
                PlayerData playerData = QuestData.getPlayerData(playerName);
                if (playerData == null) {
                    commandSender.sendMessage(Message.COMMAND$ADMIN_QUESTINFO$PLAYER_NOT_FOUND.getString());
                    return;
                }
                Quest quest = QuestData.getQuestFromID(playerData.uuid(), id);
                if (quest != null) {
                    List<String> list = Message.COMMAND$ADMIN_QUESTINFO$SUCCESS.getList();
                    for (String line : list) {
                        line = line.replace("%quest_id%", String.valueOf(quest.getId()))
                                .replace("%player%", QuestData.getPlayerName(quest.getUuid()))
                                .replace("%quest_model%", quest.getQuestModel().getKey())
                                .replace("%quest_start%", quest.getStart())
                                .replace("%quest_end%", quest.getEnd())
                                .replace("%quest_progress%", quest.getProgressString())
                                .replace("%quest_complete%", quest.getComplete())
                                .replace("%prefix%", Config.PREFIX_PLUGIN.getString());
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                    }
                } else {
                    commandSender.sendMessage(Message.COMMAND$ADMIN_QUESTINFO$QUEST_NOT_FOUND.getString());
                }
            } else {
                commandSender.sendMessage(Message.SYSTEM$BAD_COMMAND_USE.getString() +
                        Message.COMMAND$ADMIN_QUESTINFO$USE.getString()
                                .replace("%model_player%", Message.COMMAND$MODEL$PLAYER.getString())
                                .replace("%model_quest_id%", Message.COMMAND$MODEL$QUEST_ID.getString()
                                )
                );
            }
        } else {
            Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do /questadmin questinfo");
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
        }
    }
}
