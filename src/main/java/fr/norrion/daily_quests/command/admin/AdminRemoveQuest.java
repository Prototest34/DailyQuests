package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.Logger;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class AdminRemoveQuest implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$REMOVE_QUEST.getPermission())) {
            if (args.length > 2 && NumberUtils.isNumber(args[2])) {
                String playerName = args[1];
                int id = Integer.parseInt(args[2]);
                //todo check uuid
                UUID uuid = QuestData.getPlayerData(playerName).uuid();
                Quest quest = QuestData.getQuestFromID(uuid, id);
                if (quest != null) {
                    QuestData.removeQuest(uuid, quest);
                    commandSender.sendMessage(Message.COMMAND$ADMIN_REMOVEQUEST$SUCCESS.getString().replace("%player%", playerName));
                } else {
                    commandSender.sendMessage(Message.COMMAND$ADMIN_REMOVEQUEST$QUEST_NOT_FOUND.getString());
                }
            } else {
                commandSender.sendMessage(Message.SYSTEM$BAD_COMMAND_USE.getString() +
                        Message.COMMAND$ADMIN_REMOVEQUEST$USE.getString()
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
