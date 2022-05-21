package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.exeption.InvalidQuest;
import fr.norrion.daily_quests.model.quest.model.*;
import fr.norrion.daily_quests.model.quest.reward.QuestReward;
import fr.norrion.daily_quests.model.quest.reward.QuestRewardItems;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuestModelData {
    private static List<QuestModel> questModels;
    private static Main plugin;
    private static String fileName;
    private static File configFile;
    private static YamlConfiguration config;

    public static void reload() {
        QuestModelData.questModels.clear();
        QuestModelData.plugin = Main.getInstance();
        QuestModelData.fileName = "quest-model-data.yml";
        QuestModelData.configFile = new File(QuestModelData.plugin.getDataFolder(), QuestModelData.fileName);
        QuestModelData.config = QuestModelData.plugin.loadConfiguration(QuestModelData.configFile, QuestModelData.fileName);
        Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.getString().replace("%file%", QuestModelData.fileName));
        MemorySection questModel = (MemorySection) QuestModelData.config.get("questModel");
        if (questModel == null) {
            return;
        }
        for (String key : questModel.getKeys(false)) {
            createModel((MemorySection) questModel.get(key), key);
        }
    }

    private static void createModel(MemorySection memorySection, String key) {
        String type = memorySection.getString("type", null);
        if (type != null && Arrays.stream(QuestModelType.values()).map(Enum::name).toList().contains(type)) {
            try {
                QuestModel questModel = null;
                switch (QuestModelType.valueOf(type.toUpperCase())) {
                    case BREAK_BLOCK -> questModel = new QuestModelBreakBlock(memorySection, key);
                    case PLACE_BLOCK -> questModel = new QuestModelPlaceBlock(memorySection, key);
                    case KILL -> questModel = new QuestModelKill(memorySection, key);
                    case HIT -> questModel = new QuestModelHit(memorySection, key);
                    case BREED -> questModel = new QuestModelBreed(memorySection, key);
                    case SHOOT_ARROW -> questModel = new QuestModelShootArrow(memorySection, key);
                    case FISH -> questModel = new QuestModelFish(memorySection, key);
                }
                questModels.add(questModel);
            } catch (InvalidQuest e) {
                e.printStackTrace();
                Logger.InfoMessageToServerConsole(Message.SYSTEM$BAD_MODEL.getString().replace("%path%", "questModel." + key));
            }
        } else {
            Logger.InfoMessageToServerConsole(Message.SYSTEM$BAD_MODEL.getString().replace("%path%", "questModel." + key));
        }
    }

    public static List<QuestModel> getQuestModels() {
        return new ArrayList<>(questModels);
    }

    static {
        QuestModelData.questModels = new ArrayList<>();
    }

    @Nullable
    public static QuestModel getQuestModel(String modelName) {
        Optional<QuestModel> questModelOptional = QuestModelData.questModels.stream()
                .filter(questModel -> modelName.equalsIgnoreCase(questModel.getKey()))
                .findFirst();
        return questModelOptional.orElse(null);
    }

    public static void saveRewardsItems(QuestModel questModel) {
        QuestModelData.config.set("questModel." + questModel.getKey() + ".items", "");
        for (QuestReward reward : questModel.getRewards()) {
            if (reward instanceof QuestRewardItems) {
                ((QuestRewardItems) reward).save(QuestModelData.config, "questModel." + questModel.getKey() + ".items");
            }
        }
        try {
            QuestModelData.config.save(QuestModelData.configFile);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_SAVE_MODEL.getString());
        }
    }

    @Nullable
    public static QuestModel getRandomQuestModel() {
        if (QuestModelData.questModels.isEmpty()) {
            return null;
        }
        Random random = new Random();

        int value = random.nextInt(QuestModelData.questModels.size() - 1);
        return QuestModelData.questModels.get(value);
    }
}
