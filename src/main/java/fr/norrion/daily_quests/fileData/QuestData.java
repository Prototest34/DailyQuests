package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.model.quest.PlayerData;
import fr.norrion.daily_quests.model.quest.Quest;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuestData {

    private static List<PlayerData> playerDataList;
    private static Main plugin;
    private static String fileName;
    private static File configFile;
    private static YamlConfiguration config;

    public static void reload() {
        QuestData.playerDataList.clear();
        QuestData.plugin = Main.getInstance();
        QuestData.fileName = "quest-data.yml";
        QuestData.configFile = new File(QuestData.plugin.getDataFolder(), QuestData.fileName);
        QuestData.config = QuestData.plugin.loadConfiguration(QuestData.configFile, QuestData.fileName);
        Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.getString().replace("%file%", QuestData.fileName));
        for (String UUIDString : QuestData.config.getKeys(false)) {
            UUID uuid = UUID.fromString(UUIDString);
            List<Quest> questList = new ArrayList<>();
            for (String key : ((MemorySection) QuestData.config.get(UUIDString)).getKeys(false)) {
                try {
                    questList.add(new Quest(Integer.parseInt(key), (MemorySection) QuestData.config.get(UUIDString + "." + key), uuid));
                } catch (Exception e) {
                    Logger.ErrorMessageToServerConsole(Message.SYSTEM$QUEST_ERROR_LOAD.getString().replace("%path%", UUIDString + "." + key));
                }
            }
            String playerName = Bukkit.getOfflinePlayer(uuid).getName();
            QuestData.playerDataList.add(new PlayerData(questList, playerName, uuid));
        }
    }

    public static List<Quest> getQuest(UUID uuid) {
        return playerDataList.stream()
                .filter(playerData -> uuid.equals(playerData.uuid()))
                .map(PlayerData::listQuest)
                .findFirst()
                .orElse(new ArrayList<>());
    }

    @Nullable
    public static PlayerData getPlayerData(String playerName) {
        return playerDataList.stream()
                .filter(playerData -> playerName.equalsIgnoreCase(playerData.name()))
                .findFirst()
                .orElse(null);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return playerDataList.stream()
                .filter(playerData -> uuid.equals(playerData.uuid()))
                .findFirst()
                .orElse(addPlayerData(Bukkit.getOfflinePlayer(uuid).getName(), uuid));
    }

    public static PlayerData addPlayerData(String name, UUID uuid) {
        if (QuestData.isPlayerExist(uuid)) {
            return QuestData.getPlayerData(uuid);
        }
        PlayerData playerData = new PlayerData(new ArrayList<>(), name, uuid);
        playerDataList.add(playerData);
        playerData.save(QuestData.config);
        return playerData;
    }

    public static boolean isPlayerExist(UUID uuid) {
        return playerDataList.stream().map(PlayerData::uuid).anyMatch(uuid::equals);
    }

    public static void addQuest(UUID uuid, Quest quest) {
        PlayerData playerData = getPlayerData(uuid);
        playerData.listQuest().add(quest);
        try {
            saveOneQuest(quest);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_QUEST.getString());
        }
    }

    public static void removeQuest(UUID uuid, Quest quest) {
        getQuest(uuid).remove(quest);
        try {
            QuestData.savePlayer(uuid);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_QUEST.getString());
        }
    }

    @Nullable
    public static Quest getQuestFromID(UUID uuid, Integer quest_id) {
        Optional<Quest> questOptional = getQuest(uuid).stream().filter(quest -> quest.getId() == quest_id).findFirst();
        return questOptional.orElse(null);
    }

    public static void saveOneQuest(Quest quest) throws IOException {
        quest.save(QuestData.config);
        QuestData.config.save(QuestData.configFile);
    }

    public static void savePlayer(UUID uuid) throws IOException {
        QuestData.config.set(uuid.toString(), null);
        PlayerData playerData = getPlayerData(uuid);
        if (playerData != null) {
            playerData.save(QuestData.config);
        }
        QuestData.config.save(QuestData.configFile);
    }

    static {
        QuestData.playerDataList = new ArrayList<>();
    }

    //TODO change to UUID
    public static List<Quest> getUnfinishedQuest(UUID uuid) {
        return getQuest(uuid).stream().filter(quest -> !quest.isFailed() && !quest.isComplete()).toList();
    }

    public static int getNextIdQuest(UUID uuid) {
        if (isPlayerExist(uuid)) {
            List<Quest> questList = getQuest(uuid);
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
        for (PlayerData player : playerDataList) {
            List<Quest> questList = player.listQuest();
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
                player.save(QuestData.config);
                questDelete += needToBeRemove.size();
            }
        }
        Logger.InfoMessageToServerConsole(Message.SYSTEM$PURGECRON_SUCCESS.getString().replace("%number%", String.valueOf(questDelete)));
    }

    public static void giveNormalQuest() {
    }

    public static String getPlayerName(UUID uuid) {
        return playerDataList.stream()
                .filter(playerData -> playerData.uuid() == uuid)
                .map(PlayerData::name)
                .findFirst()
                .orElse("");
    }
}
