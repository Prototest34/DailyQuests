package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.model.quest.QuestRarity;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class QuestRarityData {
    private static List<QuestRarity> questRarities;
    private static Main plugin;
    private static String fileName;
    private static File configFile;
    private static YamlConfiguration config;

    static {
        QuestRarityData.questRarities = new ArrayList<>();
    }

    public static void reload() {
        QuestRarityData.questRarities.clear();
        QuestRarityData.plugin = Main.getInstance();
        QuestRarityData.fileName = "quest-rarity-data.yml";
        QuestRarityData.configFile = new File(QuestRarityData.plugin.getDataFolder(), QuestRarityData.fileName);
        QuestRarityData.config = QuestRarityData.plugin.loadConfiguration(QuestRarityData.configFile, QuestRarityData.fileName);
        Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.getString().replace("%file%", QuestRarityData.fileName));
        ConfigurationSection questModel = QuestRarityData.config.getConfigurationSection("questRarity");
        if (questModel == null) {
            return;
        }
        for (String key : questModel.getKeys(false)) {
            questRarities.add(new QuestRarity(questModel.getConfigurationSection(key), key));
        }
        AtomicReference<Double> total = new AtomicReference<>((double) 0);
        questRarities.forEach(questRarity -> total.set(total.get()+questRarity.getPercentage()));

        calculatePercentage(total.get());
    }

    public static QuestRarity getRarity(String rarity) {
        return questRarities.stream().filter(questRarity -> questRarity.getKey().equalsIgnoreCase(rarity)).findAny().orElse(null);
    }

    private static void calculatePercentage(Double total) {
        for (QuestRarity questRarity: questRarities) {
            questRarity.setRealPercentage(questRarity.getPercentage()/total*100);
        }
    }

    public static List<QuestRarity> getQuestRarities() {
        return questRarities;
    }

    public static QuestRarity getRandomRarity() {
        AtomicReference<Double> total = new AtomicReference<>((double) 0);
        questRarities
                .forEach(questRarity -> total.set(total.get()+questRarity.getPercentage()));

        Random random = new Random();

        double value = random.nextDouble()*total.get();
        QuestRarity res = questRarities.get(questRarities.size()-1);
        for (QuestRarity questRarity: questRarities) {
            value -= questRarity.getPercentage();
            if (value < 0) {
                res = questRarity;
                break;
            }
        }
        return res;
    }
}
