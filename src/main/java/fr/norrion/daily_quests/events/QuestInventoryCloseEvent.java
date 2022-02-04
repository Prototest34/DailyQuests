package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.inventory.QuestInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class QuestInventoryCloseEvent implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Message.QUEST$INVENTORY_NAME.getString())
                || event.getView().getTitle().contains(Message.QUEST$INVENTORY_NAME_OTHER.getString().replace("%player%", ""))) {
            QuestInventory.removeInv(event.getInventory());
        }
    }
}
