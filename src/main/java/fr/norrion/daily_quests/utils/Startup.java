package fr.norrion.daily_quests.utils;

import fr.norrion.daily_quests.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Startup {
    public void checkIfFilesExist() {
        if (!new File(Main.getInstance().getDataFolder() + File.separator + "Language", "lang-en.yml").exists()) {
            Main.getInstance().saveResource("Language" + File.separator + "lang-en.yml", false);
        }
        if (!new File(Main.getInstance().getDataFolder(), "config.yml").exists()) {
            Main.getInstance().saveResource("config.yml", false);
        }
    }
}
