package fr.norrion.daily_quests.model.quest;

import org.bukkit.configuration.ConfigurationSection;

public class QuestRarity {
    private final String key;
    private final String color;
    private final double percentage;
    private final String name;
    private double realPercentage;

    public QuestRarity(ConfigurationSection configurationSection, String key) {
        this.key = key;
        this.color = configurationSection.getString("color", "&f");
        this.percentage = configurationSection.getDouble("percentage", 0);
        this.name = configurationSection.getString("name", "");
    }

    public String getKey() {
        return key;
    }

    public String getColor() {
        return color;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getRealPercentage() {
        return realPercentage;
    }

    public void setRealPercentage(double realPercentage) {
        this.realPercentage = realPercentage;
    }

    public String getName() {
        return name;
    }
}
