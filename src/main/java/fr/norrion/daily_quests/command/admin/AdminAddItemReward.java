package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestModelData;
import fr.norrion.daily_quests.model.quest.model.QuestModel;
import fr.norrion.daily_quests.model.quest.reward.QuestRewardItems;
import fr.norrion.daily_quests.model.quest.reward.QuestRewardType;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminAddItemReward implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission(MyPermission.ADMIN$ADD_ITEM_REWARD.getPermission())) {
                if (args.length > 1) {
                    String modelName = args[1];
                    QuestModel questModel = QuestModelData.getQuestModel(modelName);
                    if (questModel != null && questModel.getRewards().stream().anyMatch(reward -> reward.getType().equals(QuestRewardType.ITEMS))) {
                        if (!Material.AIR.equals(player.getInventory().getItemInMainHand().getType())) {
                            QuestRewardItems reward = (QuestRewardItems) questModel.getRewards().stream().filter(reward2 -> reward2.getType().equals(QuestRewardType.ITEMS)).findFirst().get();
                            reward.addItem(player.getInventory().getItemInMainHand());
                            QuestModelData.saveRewardsItems(questModel);
                            player.sendMessage(Message.COMMAND$ADMIN_ADDITEMREWARD$SUCCESS.getString());
                        } else {
                            player.sendMessage(Message.COMMAND$ADMIN_ADDITEMREWARD$NO_ITEM.getString());
                        }
                    } else {
                        player.sendMessage(Message.COMMAND$ADMIN_ADDITEMREWARD$MODEL_NOT_FOUND.getString().replace("%model%", modelName));
                    }
                } else {
                    player.sendMessage(Message.SYSTEM$BAD_COMMAND_USE.getString() +
                            Message.COMMAND$ADMIN_ADDITEMREWARD$USE.getString()
                                    .replace("%model_quest_model%", Message.COMMAND$MODEL$QUEST_MODEL.getString()
                                    )
                    );
                }
            } else {
                Logger.logDebugMessage("Player " + player.getDisplayName() + " don't have permission to do /questadmin addquest");
                commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
            }
        } else {
            commandSender.sendMessage(Message.SYSTEM$ONLY_PLAYER_COMMAND.getString());
        }
    }
}
