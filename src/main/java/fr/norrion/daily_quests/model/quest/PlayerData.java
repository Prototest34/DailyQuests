package fr.norrion.daily_quests.model.quest;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record PlayerData(List<Quest> listQuest, String name, UUID uuid) {
    public PlayerData(List<Quest> listQuest, String name, @NotNull UUID uuid) {
        this.listQuest = listQuest;
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    @NotNull
    public UUID uuid() {
        return uuid;
    }

    public void save(ConfigurationSection configurationSection) {
        configurationSection.set(uuid + ".uuid", uuid.toString());
        configurationSection.set(uuid + ".name", name);
        for (Quest q : listQuest) {
            q.save(configurationSection);
        }
    }
}
