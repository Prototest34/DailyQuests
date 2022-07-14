package fr.norrion.daily_quests.command.admin;

import fr.norrion.daily_quests.MyPermission;
import fr.norrion.daily_quests.command.QuestCommand;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.fileData.QuestRarityData;
import fr.norrion.daily_quests.model.quest.PlayerData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.model.quest.QuestRarity;
import fr.norrion.daily_quests.utils.Logger;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class AdminRarityInfo implements QuestCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission(MyPermission.ADMIN$QUEST_INFO.getPermission())) {
            for (QuestRarity questRarity: QuestRarityData.getQuestRarities()) {
                List<String> list = Message.COMMAND$ADMIN_RARITYINFO$SUCCESS.getList();
                for (String line : list) {
                    line = Config.PREFIX_PLUGIN.getString() + " " + line.replace("%rarity_color%", questRarity.getColor())
                            .replace("%rarity_name%", questRarity.getKey())
                            .replace("%rarity_percentage%", BigDecimal.valueOf(questRarity.getRealPercentage()).setScale(2, RoundingMode.HALF_UP).toPlainString());
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
            }
        } else {
            Logger.logDebugMessage("Player " + ((Player) commandSender).getDisplayName() + " don't have permission to do /questadmin questinfo");
            commandSender.sendMessage(Message.SYSTEM$NO_PERMISSION.getString());
        }
    }
}
