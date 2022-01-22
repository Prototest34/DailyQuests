package fr.norrion.daily_quests.utils;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.Config;

import java.util.logging.Level;

public class Logger {

    public static void logDebugMessage(final String message) {
        if (Config.DEBUG.getBoolean()) {
            Main.getInstance().getLogger().info("[DEBUG] "+message);
        }
    }

    public static void InfoMessageToServerConsole(final String message) {
        Main.getInstance().getLogger().info(message);
    }

    public static void WarnMessageToServerConsole(final String message) {
        Main.getInstance().getLogger().warning(message);
    }

    public static void ErrorMessageToServerConsole(final String message) {
        Main.getInstance().getLogger().severe(message);
    }
}
