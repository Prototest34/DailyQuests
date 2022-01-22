package fr.norrion.daily_quests.inventory;

import fr.norrion.daily_quests.exeption.PlayerQuestDataNotFound;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.Quest;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class QuestInventory {
    private final String playerName;
    private final List<Quest> questList;

    public QuestInventory(String playerName) {
        this.playerName = playerName;
        this.questList = QuestData.getQuest(playerName);
    }

    public boolean openInventory(Player player) {
        if (this.questList == null){
            Logger.logDebugMessage("No quest found for player "+ playerName);
            return false;
        }
        player.openInventory(createInventory(player));
        return true;
    }

    private Inventory createInventory(Player player) {
        List<String> pattern = Config.QUEST$PATTERN.getList();
        Inventory inv = Bukkit.createInventory(player,
                pattern.size()*9,
                playerName.equalsIgnoreCase(player.getName())?Message.QUEST$INVENTORY_NAME.get():
                        Message.QUEST$INVENTORY_NAME_OTHER.get().replace("%player%", playerName)
        );
        int index = 0;
        for (String line: pattern) {
            List<String> slots = Arrays.asList(line.split(","));
            for (int i = 0; i < 9; i++){
                ItemStack itemStack;
                if (i >= slots.size() || "empty".equalsIgnoreCase(slots.get(i))) {
                    itemStack = InventoryItems.getEmpty();
                }
                else if ("previous".equalsIgnoreCase(slots.get(i))) {
                    itemStack = InventoryItems.getPrevious();
                }
                else if ("next".equalsIgnoreCase(slots.get(i))) {
                    itemStack = InventoryItems.getNext();
                }
                else {
                    itemStack = InventoryItems.get(slots.get(i));
                }
                inv.setItem(i+index, itemStack);
            }
            index += 9;

        }
        return inv;
    }
}
