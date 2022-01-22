package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.fileData.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class QuestInventoryEvent implements Listener {
    @EventHandler
    public void interactInventory(InventoryInteractEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Message.QUEST$INVENTORY_NAME.get())){
            e.setCancelled(true);
        }
        else if (e.getView().getTitle().contains(Message.QUEST$INVENTORY_NAME_OTHER.get().replace("%player%", ""))){
            e.setCancelled(true);
        }
    }
}
