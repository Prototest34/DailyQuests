package fr.norrion.daily_quests.model.quest.reward;

import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.model.quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuestRewardItems extends QuestReward {
    private final List<ItemStack> itemStacks;

    public QuestRewardItems(List<ItemStack> itemStacks) {
        super(QuestRewardType.ITEMS);
        this.itemStacks = itemStacks;
    }

    @Override
    public void execute(Quest quest) {
        Player player = Bukkit.getPlayer(QuestData.getPlayerName(quest.getUuid()));
        for (ItemStack item : itemStacks) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                final Location loc = player.getLocation();
                final World world = player.getWorld();
                world.dropItem(loc, item);
            }
        }
    }

    static public QuestRewardItems createOne(ConfigurationSection configurationSection, String key) {
        List<ItemStack> list = new ArrayList<>();
        if (configurationSection.contains("items") && configurationSection.isConfigurationSection("items")) {
            for (String key2 : configurationSection.getConfigurationSection("items").getKeys(false)) {
                if (configurationSection.isItemStack("items." + key2)) {
                    list.add(configurationSection.getItemStack("items." + key2));
                }
            }
        }
        return new QuestRewardItems(list);
    }

    public void addItem(ItemStack mainHand) {
        this.itemStacks.add(mainHand);
    }

    public void save(ConfigurationSection config, String path) {
        int index = 0;
        for (ItemStack item : itemStacks) {
            config.set(path + "." + index, item);
            index++;
        }
    }
}
