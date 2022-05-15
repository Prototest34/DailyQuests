package fr.norrion.daily_quests.events;

import fr.norrion.daily_quests.fileData.QuestData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class QuestPlayerJoin implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (!QuestData.isPlayerExist(event.getPlayer().getUniqueId())){
            QuestData.addPlayerData(event.getPlayer().getName(), event.getPlayer().getUniqueId());
        }
    }
}
