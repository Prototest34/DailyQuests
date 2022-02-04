package fr.norrion.daily_quests.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {
    public static NBTCompound getCompound(final ItemStack item) {
        NBTItem nbtItem = new NBTItem(item, true);
        return nbtItem.getCompound("DAILY_QUESTS") != null ? nbtItem.getCompound("DAILY_QUESTS") : nbtItem.addCompound("DAILY_QUESTS");
    }

    public static boolean isDailyQuestItem(final ItemStack item) {
        NBTItem nbtItem = new NBTItem(item, true);
        return nbtItem.getCompound("DAILY_QUESTS") != null;
    }
}
