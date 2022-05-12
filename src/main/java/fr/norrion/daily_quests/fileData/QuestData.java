package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class QuestData {

    private static HashMap<String, List<Quest>> quests;
    private static Main plugin;
    private static String fileName;
    private static File configFile;
    private static YamlConfiguration config;

    public static void reload() {
        QuestData.quests.clear();
        QuestData.plugin = Main.getInstance();
        QuestData.fileName = "quest-data.yml";
        QuestData.configFile = new File(QuestData.plugin.getDataFolder(), QuestData.fileName);
        QuestData.config = QuestData.plugin.loadConfiguration(QuestData.configFile, QuestData.fileName);
        Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.getString().replace("%file%", QuestData.fileName));
        for (String playerName : QuestData.config.getKeys(false)) {
            List<Quest> questList = new ArrayList<>();
            for (String key : ((MemorySection) QuestData.config.get(playerName)).getKeys(false)) {
                try {
                    questList.add(new Quest(Integer.parseInt(key), (MemorySection) QuestData.config.get(playerName + "." + key), playerName));
                } catch (Exception e) {
                    Logger.ErrorMessageToServerConsole(Message.SYSTEM$QUEST_ERROR_LOAD.getString().replace("%path%", playerName + "." + key));
                }
            }
            QuestData.quests.put(playerName, questList);
        }
    }

    public static List<Quest> getQuest(String playerName) {
        if (!quests.containsKey(playerName)) {
            return new ArrayList<>();
        }
        return quests.get(playerName);
    }

    @Nullable
    public static Quest getQuestFromID(String playerName, Integer quest_id) {
        Optional<Quest> questOptional = getQuest(playerName).stream().filter(quest -> quest.getId() == quest_id).findFirst();
        return questOptional.orElse(null);
    }

    public static void addQuest(String playerName, Quest quest) {
        if (!quests.containsKey(playerName)) {
            quests.put(playerName, new ArrayList<>());
        }
        quests.get(playerName).add(quest);
        try {
            saveOneQuest(quest);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_QUEST.getString());
        }
    }

    public static void removeQuest(String playerName, Quest quest) {
        if (!quests.containsKey(playerName)) {
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "7"));
            throw new RuntimeException();
        }
        quests.get(playerName).remove(quest);
        try {
            QuestData.savePlayer(playerName);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_QUEST.getString());
        }
    }

    public static void saveOneQuest(Quest quest) throws IOException {
        quest.save(QuestData.config);
        QuestData.config.save(QuestData.configFile);
    }

    public static void savePlayer(String playerName) throws IOException {
        if (!quests.containsKey(playerName)) {
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "7"));
            throw new RuntimeException();
        }
        QuestData.config.set(playerName, null);
        for (Quest quest : quests.get(playerName)) {
            quest.save(QuestData.config);
        }
        QuestData.config.save(QuestData.configFile);
    }

    static {
        QuestData.quests = new HashMap<>();
    }

    //TODO change to UUID
    public static List<Quest> getUnfinishedQuest(String playerName) {
        return getQuest(playerName).stream().filter(quest -> !quest.isFailed() && !quest.isComplete()).toList();
    }

    public static int getNextIdQuest(String playerName) {
        if (quests.containsKey(playerName)) {
            List<Quest> questList = quests.get(playerName);
            for (int i = 0; i < 5000000; i++) {
                int finalI = i;
                if (questList.stream().filter(quest -> quest.getId() == finalI).findFirst().isEmpty()) {
                    return i;
                }
            }
        }
        return 0;
    }

    public static void purge() {
        Logger.InfoMessageToServerConsole(Message.SYSTEM$PURGECRON_IN_PROGRESS.getString());
        int questDelete = 0;
        for (String player : quests.keySet()) {
            List<Quest> questList = quests.get(player);
            List<Quest> needToBeRemove = new ArrayList<>();
            for (Quest quest : questList) {
                if (quest.needToBePurge()) {
                    needToBeRemove.add(quest);
                }
            }
            for (Quest quest : needToBeRemove) {
                questList.remove(quest);
            }
            if (!needToBeRemove.isEmpty()) {
                try {
                    savePlayer(player);
                    questDelete += needToBeRemove.size();
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_QUEST.getString());
                }
            }
        }
        Logger.InfoMessageToServerConsole(Message.SYSTEM$PURGECRON_SUCCESS.getString().replace("%number%", String.valueOf(questDelete)));
    }
}
