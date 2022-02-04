package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminReload implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$RELOAD.getPermission())) {
            Logger.logDebugMessage("Starting reload");
            long start = System.currentTimeMillis();
            Main.reload();
            commandSender.sendMessage(Message.ADMIN$RELOAD_SUCCESS.getString().replace("%time%", String.valueOf(System.currentTimeMillis() - start)));
        } else {
            Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do /questadmin reload");
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
        }
    }


}
