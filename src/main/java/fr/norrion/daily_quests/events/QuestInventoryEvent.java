package fr.norrion.daily_quests.events;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.inventory.QuestInventory;
import fr.norrion.daily_quests.utils.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class QuestInventoryEvent implements Listener {
    @EventHandler
    public void interactInventory(@NotNull InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getView().getTitle().equals(Message.QUEST$INVENTORY_NAME.getString())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        globalAsynchronously(e);
                    }
                }.runTaskAsynchronously(Main.getInstance());
                e.setCancelled(true);
            } else if (e.getView().getTitle().matches(Message.QUEST$INVENTORY_NAME_OTHER.getString().replace("%player%", ".*"))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        globalAsynchronously(e);
                        adminAsynchronously(e);
                    }
                }.runTaskAsynchronously(Main.getInstance());
                e.setCancelled(true);
            }
        }
    }

    private void adminAsynchronously(@NotNull InventoryClickEvent e) {
        if (e.getCurrentItem() != null && NBTUtils.isDailyQuestItem(e.getCurrentItem())) {
            Integer id = NBTUtils.getCompound(e.getCurrentItem()).getInteger("QUEST_ID");
            String playerName = NBTUtils.getCompound(e.getCurrentItem()).getString("QUEST_PLAYER");
            if (id != null && playerName != null) {
                Player p = (Player) e.getWhoClicked();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.performCommand("questadmin questinfo " + playerName + " " + id);
                    }
                }.runTask(Main.getInstance());
            }
        }
    }

    private void globalAsynchronously(@NotNull InventoryClickEvent e) {
        if (e.getCurrentItem() != null && NBTUtils.isDailyQuestItem(e.getCurrentItem())) {
            NBTCompound nbtCompound = NBTUtils.getCompound(e.getCurrentItem());
            if ("NEXT".equals(nbtCompound.getString("QUEST_FUNCTION"))) {
                int page = nbtCompound.getInteger("QUEST_CURRENT_PAGE") + 1;
                QuestInventory questInventory = QuestInventory.getQuestInventory(e.getInventory());
                if (questInventory != null) {
                    questInventory.createInventory(e.getInventory(), page);
                }
            }
            if ("PREVIOUS".equals(nbtCompound.getString("QUEST_FUNCTION"))) {
                int page = nbtCompound.getInteger("QUEST_CURRENT_PAGE") - 1;
                QuestInventory questInventory = QuestInventory.getQuestInventory(e.getInventory());
                if (questInventory != null) {
                    questInventory.createInventory(e.getInventory(), page);
                }
            }
        }
    }
}
