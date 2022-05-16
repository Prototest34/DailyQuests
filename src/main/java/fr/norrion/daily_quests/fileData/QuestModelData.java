package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
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
        for (String key : ((MemorySection) QuestModelData.config.get("questModel")).getKeys(false)) {
            createModel((MemorySection) QuestModelData.config.get("questModel." + key), key);
        }
    }

    private static void createModel(MemorySection memorySection, String key) {
        if (memorySection.contains("type") && Arrays.stream(QuestModelType.values()).map(Enum::name).toList().contains(memorySection.getString("type"))) {
            QuestModel questModel = null;
            switch (QuestModelType.valueOf(memorySection.getString("type").toUpperCase())) {
                case BREAK_BLOCK -> questModel = QuestModelBreakBlock.create(memorySection, key);
                case PLACE_BLOCK -> questModel = QuestModelPlaceBlock.create(memorySection, key);
                case KILL -> questModel = QuestModelKill.create(memorySection, key);
                case HIT -> questModel = QuestModelHit.create(memorySection, key);
                case BREED -> questModel = QuestModelBreed.create(memorySection, key);
                case SHOOT_ARROW -> questModel = QuestModelShootArrow.create(memorySection, key);
            }
            if (questModel != null) {
                questModels.add(questModel);
            } else {
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
        QuestModelData.config.set("questModel."+questModel.getKey()+".items", "");
        for (QuestReward reward: questModel.getRewards()) {
            if (reward instanceof QuestRewardItems) {
                ((QuestRewardItems)reward).save(QuestModelData.config, "questModel."+questModel.getKey()+".items");
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
        if (QuestModelData.questModels.isEmpty()){
            return null;
        }
        Random random = new Random();

        int value = random.nextInt(QuestModelData.questModels.size()-1);
        return QuestModelData.questModels.get(value);
    }
}
