package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public enum Message {
    SYSTEM$FILE_LOADED_SUCCESS("%file% file was successfully loaded.", false),
    SYSTEM$CANNOT_FIND_LANGUAGE_FILE("Can not find file %file%!", false),
    SYSTEM$ADD_KEY_LANG("Unable to find %key% key in %file% file. Added the default key",false),
    SYSTEM$SOMETHING_IS_WRONG("Something is wrong. Code: %code%", false),
    SYSTEM$NO_PERMISSION("&cYou don't have permission to execute this command", true),
    SYSTEM$ONLY_PLAYER_COMMAND("Only executable by players", false),
    SYSTEM$MATERIAL_DOES_EXIST("The material %item% does not exist!", false),
    SYSTEM$BAD_COMMAND_USE("&bUse: ", true),

    COMMAND$ADMIN_RELOAD$HELP("&aReload the plugin", false),
    COMMAND$ADMIN_QUEST$HELP("&aSee other player quest", false),
    COMMAND$ADMIN_QUEST$USE("/questadmin quest <player>", false),
    COMMAND$QUEST$HELP("&aSee your quest", false),
    COMMAND$QUEST$USE("/quest", false),

    ADMIN$RELOAD_SUCCESS("&aReload successful! Time: &b%time%ms", true),

    QUEST$INVENTORY_NAME("&aQuest List", false),
    QUEST$INVENTORY_NAME_OTHER("&b%player% &aQuest List", false),

    SYSTEM$DISABLING_PLUGIN("Disabling Plugin", false);

    private final String message;
    private static String fileName;
    private static File configFile;
    private static FileConfiguration config;
    private static Main plugin;
    private static HashMap<String, String> messages;

    Message(String message, boolean prefix) {
        String PREFIX = prefix?"&7[&aDaily Quests&7] ":"";
        this.message = PREFIX +message;
    }

    @Override
    public String toString() {
        return message;
    }

    public static void reload() {
        messages.clear();
        Message.plugin = Main.getInstance();
        Message.fileName = Config.LANGUAGE_FILE.getString();
        Message.configFile = new File(Message.plugin.getDataFolder() + File.separator + "Language", Message.fileName);
        Message.config = Message.plugin.loadConfiguration(Message.configFile, Message.fileName);
        if (Message.fileName != null) {
            Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.get().replace("%file%", Message.fileName));
        }
        else {
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_FIND_LANGUAGE_FILE.get().replace("%file%", Message.fileName));
        }
    }

    private String getKey() {
        return this.name().toLowerCase()
                .replace("_", "-")
                .replace("$", ".")
                .replace("0", ",");
    }
    public String get() {
        final String key = this.getKey();
        if (Message.messages.containsKey(key)) {
            return Message.messages.get(key);
        }
        String value;
        try {
            value = Message.config.getString(key);
            if (value == null) {
                value = this.message;
                Message.config.set(key, this.message);
                Message.config.save(Message.configFile);
                Logger.WarnMessageToServerConsole(Message.SYSTEM$ADD_KEY_LANG.toString().replace("%key%", key).replace("%file%", Message.fileName));
            }
        }
        catch (NullPointerException| IOException npe) {
            value = this.message;
            npe.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "1"));
        }
        value = ChatColor.translateAlternateColorCodes('&', value);
        Message.messages.put(key, value);
        return value;
    }

    static {
        Message.messages = new HashMap<>();
    }
}
