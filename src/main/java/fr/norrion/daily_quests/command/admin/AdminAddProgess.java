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

public class AdminAddProgess implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$ADD_PROGRESS.getPermission())) {
            if (args.length > 3 && NumberUtils.isNumber(args[2]) && NumberUtils.isNumber(args[3])) {
                String playerName = args[1];
                int id = Integer.parseInt(args[2]);
                //todo check uuid
                Quest quest = QuestData.getQuestFromID(QuestData.getPlayerData(playerName).uuid(), id);
                if (quest != null) {
                    quest.addProgression(Math.min(Integer.parseInt(args[3]), quest.getProgressionEnd() - quest.getProgression()));
                    commandSender.sendMessage(Message.COMMAND$ADMIN_ADDPROGRESS$SUCCESS.getString());
                } else {
                    commandSender.sendMessage(Message.COMMAND$ADMIN_ADDPROGRESS$QUEST_NOT_FOUND.getString());
                }
            } else {
                commandSender.sendMessage(Message.SYSTEM$BAD_COMMAND_USE.getString() +
                        Message.COMMAND$ADMIN_ADDPROGRESS$USE.getString()
                                .replace("%model_player%", Message.COMMAND$MODEL$PLAYER.getString())
                                .replace("%model_quest_model%", Message.COMMAND$MODEL$QUEST_MODEL.getString())
                                .replace("%model_number%", Message.COMMAND$MODEL$NUMBER.getString())
                );
            }
        } else {
            Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do /questadmin addprogress");
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
        }
    }
}
