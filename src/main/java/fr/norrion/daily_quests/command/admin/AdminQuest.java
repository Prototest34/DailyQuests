package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.inventory.QuestInventory;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminQuest implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$QUEST_ACCESS_OTHER.getPermission())) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Message.SYSTEM$ONLY_PLAYER_COMMAND.getString());
                return;
            }
            if (args.length > 1) {
                new QuestInventory(args[1]).openInventory((Player) commandSender, true);
            } else {
                commandSender.sendMessage(Message.SYSTEM$BAD_COMMAND_USE.getString() + Message.COMMAND$ADMIN_QUEST$USE.getString());
            }
        } else {
            Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do /questadmin quest");
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
        }
    }
}
