package fr.norrion.daily_quests.utils;

import fr.norrion.daily_quests.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BossBarUtils {

    private static List<BossBarData> data = new ArrayList<>();

    public static KeyedBossBar getBossBar(String key, String title, BarColor barColor, BarStyle barStyle) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.getInstance(), key);
        KeyedBossBar bossBar = Bukkit.getBossBar(namespacedKey);
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(namespacedKey, title, barColor, barStyle);
        }
        bossBar.setTitle(ChatColor.translateAlternateColorCodes('&', title));
        bossBar.setColor(barColor);
        bossBar.setStyle(barStyle);
        return bossBar;
    }

    public static void resetDelay(KeyedBossBar bossBar) {
        BossBarData bossBarData = data.stream()
                .filter(bossBarData1 -> bossBarData1.bossBar.getKey().equals(bossBar.getKey()))
                .findFirst()
                .orElse(null);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                data = data.stream().filter(bossBarData1 -> !bossBarData1.bossBar.equals(bossBar)).collect(Collectors.toList());
                bossBar.removeAll();
                Bukkit.removeBossBar(bossBar.getKey());
            }
        };
        if (bossBarData == null) {
            BukkitTask task = runnable.runTaskLaterAsynchronously(Main.getInstance(), 100L);
            bossBarData = new BossBarData(bossBar, task);
            data.add(bossBarData);
        } else {
            bossBarData.task.cancel();
            bossBarData.task = runnable.runTaskLaterAsynchronously(Main.getInstance(), 100L);
        }
    }

    private static class BossBarData {
        private final KeyedBossBar bossBar;
        private BukkitTask task;

        private BossBarData(KeyedBossBar bossBar, BukkitTask task) {
            this.bossBar = bossBar;
            this.task = task;
        }
    }
}
