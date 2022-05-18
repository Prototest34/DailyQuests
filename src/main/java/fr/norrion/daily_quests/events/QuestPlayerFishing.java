package fr.norrion.daily_quests.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class QuestPlayerFishing implements Listener {
    @EventHandler
    public void onPlayerFishing(PlayerFishEvent playerFishEvent) {
        if (!playerFishEvent.isCancelled()) {

        }

    }
}
