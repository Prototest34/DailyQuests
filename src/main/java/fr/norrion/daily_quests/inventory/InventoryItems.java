package fr.norrion.daily_quests.inventory;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.Logger;
import fr.norrion.daily_quests.utils.NBTUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryItems {
    public static ItemStack getEmpty() {
        return get(Config.QUEST$EMPTY_ITEM.getString());
    }

    public static ItemStack getNext(int page) {
        ItemStack item = get(Config.QUEST$NEXT_PAGE_ITEM.getString());
        NBTUtils.getCompound(item).setString("QUEST_FUNCTION", "NEXT");
        NBTUtils.getCompound(item).setInteger("QUEST_CURRENT_PAGE", page);
        return item;
    }

    public static ItemStack getPrevious(int page) {
        ItemStack item = get(Config.QUEST$PREVIOUS_PAGE_ITEM.getString());
        NBTUtils.getCompound(item).setString("QUEST_FUNCTION", "PREVIOUS");
        NBTUtils.getCompound(item).setInteger("QUEST_CURRENT_PAGE", page);
        return item;
    }

    public static ItemStack get(String s) {
        String[] data = s.split(":");
        Material m = Material.getMaterial(data[0].toUpperCase());
        ItemStack i;
        if (m == null) {
            Logger.WarnMessageToServerConsole(Message.SYSTEM$MATERIAL_DOES_EXIST.getString().replace("%item%", data[0]));
            i = new ItemStack(Material.BARRIER);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName("§cUnknown item");
            im.setCustomModelData(-1);
            i.setItemMeta(im);
        } else {
            i = new ItemStack(m);
            if (data.length > 1) {
                i.setAmount(Integer.parseInt(data[1]));
            }
            ItemMeta im = i.getItemMeta();
            if (data.length > 2 && !data[2].isEmpty()) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "$".equals(data[2]) ? " " : data[2]));
            }
            if (data.length > 3) {
                im.setCustomModelData(Integer.getInteger(data[3]));
            }
            i.setItemMeta(im);
        }
        return i;
    }

    public static ItemStack getQuest(Quest quest) {
        ItemStack item = get(quest.getQuestModel().getPattern());
        ItemMeta im = item.getItemMeta();
        List<String> list = Config.QUEST$LORE.getList();
        List<String> lore = new ArrayList<>();
        for (String line : list) {
            if ((line.contains("%end%") && quest.isComplete()) ||
                    (line.contains("%complete%") && !quest.isComplete())) {
                continue;
            }
            if (line.contains("%description%")) {
                for (String description : quest.getQuestModel().getDescription()) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', description));
                }
                continue;
            }
            if (line.contains("%rewards%")) {
                for (String rewardString : quest.getQuestModel().getRewardText()) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', rewardString));
                }
                continue;
            }
            String newline = line.replace("%progress%", quest.getProgressString())
                    .replace("%end%", quest.getEnd())
                    .replace("%start%", quest.getStart())
                    .replace("%complete%", quest.getComplete())
                    .replace("%remaining_time%", quest.timeRemainingString())
                    .replace("%separator%", Config.QUEST$SEPARATOR.getString());
            lore.add(ChatColor.translateAlternateColorCodes('&', newline));
        }
        im.setLore(lore);
        item.setItemMeta(im);
        NBTCompound nbtCompound = NBTUtils.getCompound(item);
        nbtCompound.setInteger("QUEST_ID", quest.getId());
        nbtCompound.setUUID("QUEST_PLAYER_UUID", quest.getUuid());
        nbtCompound.setString("QUEST_FUNCTION", "QUEST");
        return item;
    }
}
