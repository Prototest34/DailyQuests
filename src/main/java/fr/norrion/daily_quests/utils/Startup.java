package fr.norrion.daily_quests.utils;

import fr.norrion.daily_quests.Main;

import java.io.File;

public class Startup {
    public void checkIfFilesExist() {
        if (!new File(Main.getInstance().getDataFolder() + File.separator + "Language", "lang-en.yml").exists()) {
            Main.getInstance().saveResource("Language" + File.separator + "lang-en.yml", false);
        }
        if (!new File(Main.getInstance().getDataFolder(), "config.yml").exists()) {
            Main.getInstance().saveResource("config.yml", false);
        }
        if (!new File(Main.getInstance().getDataFolder(), "quest-rarity-data.yml").exists()) {
            Main.getInstance().saveResource("quest-rarity-data.yml", false);
        }
        if (!new File(Main.getInstance().getDataFolder(), "quest-model-data.yml").exists()) {
            Main.getInstance().saveResource("quest-model-data.yml", false);
        }
        if (!new File(Main.getInstance().getDataFolder(), "quest-data.yml").exists()) {
            Main.getInstance().saveResource("quest-data.yml", false);
        }
    }
}
