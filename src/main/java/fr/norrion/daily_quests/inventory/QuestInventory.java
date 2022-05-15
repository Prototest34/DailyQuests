package fr.norrion.daily_quests.inventory;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestInventory {
    private final UUID uuid;
    private final List<Quest> questList;

    private static final HashMap<Inventory, QuestInventory> inventoryList = new HashMap<>();

    public static void launchRefreshInv() {
        int time = Config.QUEST$REFRESH_TIME.getInt();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Inventory inv : inventoryList.keySet()) {
                    if (inv.getViewers().isEmpty()) {
                        QuestInventory.removeInv(inv);
                    } else {
                        for (ItemStack item : inv.getStorageContents()) {
                            if (item != null && NBTUtils.isDailyQuestItem(item) && "QUEST".equals(NBTUtils.getCompound(item).getString("QUEST_FUNCTION"))) {
                                Quest quest = QuestData.getQuestFromID(NBTUtils.getCompound(item).getUUID("QUEST_PLAYER_UUID"), NBTUtils.getCompound(item).getInteger("QUEST_ID"));
                                inv.setItem(inv.first(item), InventoryItems.getQuest(quest));
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), time, time);
    }

    @Nullable
    public static QuestInventory getQuestInventory(Inventory inv) {
        return inventoryList.getOrDefault(inv, null);
    }

    public static void removeInv(Inventory inventory) {
        inventoryList.remove(inventory);
    }

    public QuestInventory(UUID uuid) {
        this.uuid = uuid;
        this.questList = QuestData.getQuest(uuid);
    }

    public boolean openInventory(Player player, boolean admin) {
        List<String> pattern = Config.QUEST$PATTERN.getList();
        Inventory inv = Bukkit.createInventory(player,
                pattern.size() * 9,
                admin ? Message.QUEST$INVENTORY_NAME_OTHER.getString().replace("%player%", QuestData.getPlayerName(uuid)) :
                        Message.QUEST$INVENTORY_NAME.getString()
        );
        createInventory(inv, 0);
        inventoryList.put(inv, this);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inv);
            }
        }.runTask(Main.getInstance());
        return true;
    }

    public void createInventory(Inventory inv, int page) {
        List<String> pattern = Config.QUEST$PATTERN.getList();
        int index = 0;
        int nbQuestSlot = 0;
        for (String line : pattern) {
            for (String slot : line.split(",")) {
                if ("quest".equals(slot)) {
                    nbQuestSlot++;
                }
            }
        }
        int indexQuests = page * nbQuestSlot;
        boolean havePreviousPage = page > 0;
        boolean haveNextPage = this.questList.size() > (page + 1) * nbQuestSlot;
        for (String line : pattern) {
            List<String> slots = Arrays.asList(line.split(","));
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack;
                if (i >= slots.size() || "empty".equalsIgnoreCase(slots.get(i))) {
                    itemStack = InventoryItems.getEmpty();
                } else if ("quest".equalsIgnoreCase(slots.get(i))) {
                    if (indexQuests < this.questList.size())
                        itemStack = InventoryItems.getQuest(this.questList.get(indexQuests));
                    else
                        itemStack = InventoryItems.get("AIR");
                    indexQuests++;
                } else if ("previous".equalsIgnoreCase(slots.get(i))) {
                    if (havePreviousPage) {
                        itemStack = InventoryItems.getPrevious(page);
                    } else {
                        itemStack = InventoryItems.getEmpty();
                    }
                } else if ("next".equalsIgnoreCase(slots.get(i))) {
                    if (haveNextPage) {
                        itemStack = InventoryItems.getNext(page);
                    } else {
                        itemStack = InventoryItems.getEmpty();
                    }
                } else {
                    itemStack = InventoryItems.get(slots.get(i));
                }
                inv.setItem(i + index, itemStack);
            }
            index += 9;
        }
    }
}
