package fr.norrion.daily_quests.inventory;

import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryItems {
    public static ItemStack getEmpty() {
        return get(Config.QUEST$EMPTY_ITEM.getString());
    }

    public static ItemStack getNext() {
        return get(Config.QUEST$NEXT_PAGE_ITEM.getString());
    }

    public static ItemStack getPrevious() {
        return get(Config.QUEST$PREVIOUS_PAGE_ITEM.getString());
    }

    public static ItemStack get(String s) {
        String[] data = s .split(":");
        Material m = Material.getMaterial(data[0]);
        ItemStack i;
        if (m == null) {
            Logger.WarnMessageToServerConsole(Message.SYSTEM$MATERIAL_DOES_EXIST.get());
            i = new ItemStack(Material.BARRIER);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName("&cUnknown item");
            im.setCustomModelData(-1);
            i.setItemMeta(im);
        }
        else {
            i = new ItemStack(m);
            if (data.length > 1) {
                i.setAmount(Integer.getInteger(data[1]));
            }
            ItemMeta im = i.getItemMeta();
            i.setItemMeta(im);
            if (data.length > 2) {
                im.setDisplayName(data[2]);
            }
            if (data.length > 3) {
                im.setCustomModelData(Integer.getInteger(data[3]));
            }
        }
        return i;
    }
}
