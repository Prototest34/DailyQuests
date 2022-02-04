package fr.norrion.daily_quests.command.quest;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.inventory.QuestInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuestCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player;
                if (commandSender instanceof Player) {
                    if ((player = (Player) commandSender).hasPermission(MyPermission.QUEST_ACCESS.getPermission())) {
                        if (strings.length == 0) {
                            new QuestInventory(player.getName()).openInventory(player, false);
                        }
                    } else {
                        player.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
                    }
                } else {
                    commandSender.sendMessage(Message.SYSTEM$ONLY_PLAYER_COMMAND.getString());
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
