package fr.norrion.daily_quests.command;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.inventory.QuestInventory;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class QuestAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        StringBuilder b = new StringBuilder();
        Arrays.stream(args).forEach(s1 -> b.append(s1).append(" "));
        Logger.logDebugMessage("Command executor from: "+commandSender+ " command: "+ command.getLabel()+ "commandString: "+ s+ " args:"+ b);
        if (!(commandSender instanceof Player) || commandSender.hasPermission(MyPermission.RELOAD.getPermission())) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "reload" -> this.reloadCommand(commandSender);
                    case "quest" -> this.questCommand(commandSender, args);
                }
            }
        }
        else {
            Logger.logDebugMessage("Player "+((Player) commandSender).getDisplayName()+" don't have permission to do"+ command.getLabel());
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.get());
        }
        return false;
    }

    private void questCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Message.SYSTEM$ONLY_PLAYER_COMMAND.get());
            return;
        }
        if (args.length > 1 ) {
            new QuestInventory(args[1]).openInventory((Player) commandSender);
        }
        else {
            commandSender.sendMessage(Message.SYSTEM$BAD_COMMAND_USE+Message.COMMAND$ADMIN_QUEST$USE.get());
        }
    }

    private void reloadCommand(CommandSender commandSender) {
        Logger.logDebugMessage("Starting reload");
        long start = System.currentTimeMillis();
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.reload();
                commandSender.sendMessage(Message.ADMIN$RELOAD_SUCCESS.get().replace("%time%", String.valueOf(System.currentTimeMillis()-start)));
            }
        }.runTaskAsynchronously(Main.getInstance());
    }
}
