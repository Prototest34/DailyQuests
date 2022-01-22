package fr.norrion.daily_quests.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NBTUtils {
    public static NBTCompound getCompound(final ItemStack item) {
        NBTItem nbtItem = new NBTItem(item, true);
        return nbtItem.getCompound("DAILY_QUESTS")!=null?nbtItem.getCompound("DAILY_QUESTS"):nbtItem.addCompound("DAILY_QUESTS");
    }

    //todo delete
    public static ItemStack getSpawnerToGive(EntityType spawnerentity) {
        final ItemStack spawnertogive = createItem(Material.SPAWNER, 1, getSpawnerName(spawnerentity.toString()));
        final NBTCompound esgui = NBTUtils.getCompound(spawnertogive);
        esgui.setString("SpawnerType", spawnerentity.toString());
        return spawnertogive;
    }

    private static ItemStack createItem(Material matierial, int amount, String name) {
        final ItemStack itemStack = new ItemStack(matierial);
        itemStack.setAmount(amount);
        final ItemMeta mitemStack = itemStack.getItemMeta();
        if (mitemStack != null) {
            mitemStack.setDisplayName(name);
            itemStack.setItemMeta(mitemStack);
        }
        return itemStack;
    }

    //todo delete
    private static String getSpawnerName(String spawnertype) {
        final String[] arr = spawnertype.toLowerCase().replace("_", " ").split(" ");
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
        }
        return ChatColor.DARK_GRAY + "[" + ChatColor.RED + sb.toString().trim() + ChatColor.RESET + " spawner" + ChatColor.DARK_GRAY + "]";
    }

    //todo delete
    public static String getSpawnerType(ItemStack itemInHand) {
        final NBTCompound compound = getCompound(itemInHand);
        if (!compound.hasKey("SpawnerType") || compound.getString("SpawnerType").isEmpty()) {
            return null;
        }
        return compound.getString("SpawnerType");
    }
}
