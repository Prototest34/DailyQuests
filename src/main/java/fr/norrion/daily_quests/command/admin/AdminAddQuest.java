package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.fileData.QuestModelData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.model.quest.model.QuestModel;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class AdminAddQuest implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$ADD_QUEST_OTHER.getPermission())) {
            if (args.length > 2) {
                String playerName = args[1];
                String modelName = args[2];
                QuestModel questModel = QuestModelData.getQuestModel(modelName);
                if (questModel != null) {
                    QuestData.addQuest(playerName, new Quest(QuestData.getNextIdQuest(playerName), playerName, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, questModel));
                    commandSender.sendMessage(Message.COMMAND$ADMIN_ADDQUEST$SUCCESS.getString().replace("%player%", playerName));
                } else {
                    commandSender.sendMessage(Message.COMMAND$ADMIN_ADDQUEST$MODEL_NOT_FOUND.getString().replace("%model%", modelName));
                }
            } else {
                commandSender.sendMessage(Message.SYSTEM$BAD_COMMAND_USE.getString() +
                        Message.COMMAND$ADMIN_QUESTINFO$USE.getString()
                                .replace("%model_player%", Message.COMMAND$MODEL$PLAYER.getString())
                                .replace("%model_quest_model%", Message.COMMAND$MODEL$QUEST_MODEL.getString()
                                )
                );
            }
        } else {
            Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do /questadmin addquest");
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
        }
    }
}
