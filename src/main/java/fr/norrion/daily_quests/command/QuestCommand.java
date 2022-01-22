package fr.norrion.daily_quests.command;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.exeption.PlayerQuestDataNotFound;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.inventory.QuestInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player;
        if (commandSender instanceof Player) {
            if ((player = (Player) commandSender).hasPermission(MyPermission.QUEST_ACCESS.getPermission())) {
                if (strings.length == 0) {
                   new QuestInventory(player.getName()).openInventory(player);
                }
            }
            else {
                player.sendMessage(Message.SYSTEM$NO_PERMISSION.get());
            }
        }
        else {
            commandSender.sendMessage(Message.SYSTEM$ONLY_PLAYER_COMMAND.get());
        }
        return false;
    }
}
