package fr.norrion.daily_quests.utils;

import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class QuestSound {
    private final String sound;
    private final SoundCategory soundCategory;
    private final int volume;
    private final int pitch;
    public QuestSound(ConfigurationSection sound) {
        this.sound = sound.getString("sound", "");
        String soundCategory = sound.getString("sound-category", null);
        this.soundCategory = soundCategory != null ? SoundCategory.valueOf(soundCategory.toUpperCase()) : null;
        this.volume = sound.getInt("volume", 1);
        this.pitch = sound.getInt("pitch", 1);
    }

    public void play(Player player) {
        if (this.soundCategory != null) {
            player.playSound(player.getLocation(), this.sound, this.soundCategory, this.volume, this.pitch);
        } else {
            player.playSound(player.getLocation(), this.sound, this.volume, this.pitch);
        }
    }
}
