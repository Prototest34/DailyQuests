package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.model.Quest;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class QuestData {

    private static HashMap<String, List<Quest>> quests;
    private static Main plugin;
    private static String fileName;
    private static File configFile;
    private static YamlConfiguration config;

    public static void reload() {
        QuestData.quests.clear();
        QuestData.plugin = Main.getInstance();
        QuestData.fileName = "questData.yml";
        QuestData.configFile = new File(QuestData.plugin.getDataFolder(), QuestData.fileName);
        QuestData.config = QuestData.plugin.loadConfiguration(QuestData.configFile, QuestData.fileName);
        Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.get().replace("%file%", QuestData.fileName));
    }

    public static List<Quest> getQuest(String playerName) {
        return quests.get(playerName);
    }
}
