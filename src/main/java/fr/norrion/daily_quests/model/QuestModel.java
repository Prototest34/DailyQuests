package fr.norrion.daily_quests.model;

import org.bukkit.Material;

public class QuestModel {
    private final String name;
    private final Material material;
    private final QuestType type;

    public QuestModel(String name, Material material, QuestType type) {
        this.name = name;
        this.material = material;
        this.type = type;
    }
}
