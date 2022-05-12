package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Message {
    SYSTEM$ADD_KEY_LANG("Unable to find %key% key in %file% file. Added the default key", false),
    SYSTEM$BAD_COMMAND_USE("&cUse: ", true),
    SYSTEM$BAD_MODEL("Missing parameter in the following model: %path%", false),
    SYSTEM$CANNOT_FIND_LANGUAGE_FILE("Can not find file %file%!", false),
    SYSTEM$CANNOT_SAVE_QUEST("Cannot save quest !", false),
    SYSTEM$CANNOT_SAVE_MODEL("Cannot save model !", false),
    SYSTEM$FILE_LOADED_SUCCESS("%file% file was successfully loaded.", false),
    SYSTEM$QUEST_ERROR_LOAD("Unable to load next quest: %path%", false),
    SYSTEM$MATERIAL_DOES_EXIST("The material %item% does not exist!", false),
    SYSTEM$NO_PERMISSION("&cYou don't have permission to execute this command", true),
    SYSTEM$ONLY_PLAYER_COMMAND("Only executable by players", false),
    SYSTEM$SOMETHING_IS_WRONG("Something is wrong. Code: %code%", false),
    SYSTEM$PURGECRON_ENABLE("PurgeQuest cron is enable", false),
    SYSTEM$PURGECRON_IN_PROGRESS("Purging ongoing quests", false),
    SYSTEM$PURGECRON_SUCCESS("&aDeleting %number% outdated quest", false),
    SYSTEM$PURGECRON_INVALID("&cInvalid cron!", false),
    SYSTEM$VAULT_NOT_FOUND("&cCouldn't give the cash reward! Vault is not found!", false),

    DATE$MONTHS("month%s%", false),
    DATE$DAYS("day%s%", false),
    DATE$HOURS("hour%s%", false),
    DATE$MINUTES("minute%s%", false),
    DATE$SECONDS("second%s%", false),
    DATE$MONTHS_SHORT("mo", false),
    DATE$DAYS_SHORT("d", false),
    DATE$HOURS_SHORT("h", false),
    DATE$MINUTES_SHORT("m", false),
    DATE$SECONDS_SHORT("s", false),

    COMMAND$MODEL$PLAYER("<player>", false),
    COMMAND$MODEL$QUEST_ID("<quest id>", false),
    COMMAND$MODEL$QUEST_MODEL("<quest model>", false),
    COMMAND$MODEL$NUMBER("<number>", false),
    COMMAND$ADMIN_RELOAD$HELP("&aReload the plugin", false),
    COMMAND$ADMIN_QUEST$HELP("&aSee other player quest", false),
    COMMAND$ADMIN_QUEST$USE("/questadmin quest %model_player%", false),
    COMMAND$ADMIN_ADDQUEST$HELP("&aAdd quest to a player", false),
    COMMAND$ADMIN_ADDQUEST$USE("/questadmin addquest %model_player% %model_quest_model%", false),
    COMMAND$ADMIN_ADDQUEST$SUCCESS("&aQuest successfully added to &b%player%", true),
    COMMAND$ADMIN_ADDQUEST$MODEL_NOT_FOUND("&cCannot find the model %model%", true),
    COMMAND$ADMIN_ADDITEMREWARD$HELP("&aAdd a item reward to a model", false),
    COMMAND$ADMIN_ADDITEMREWARD$USE("/questadmin additemreward %model_quest_model%", false),
    COMMAND$ADMIN_ADDITEMREWARD$SUCCESS("&aItem successfully added.", true),
    COMMAND$ADMIN_ADDITEMREWARD$MODEL_NOT_FOUND("&cCannot find the model %model%, or model reward is not item type. Check you config.", true),
    COMMAND$ADMIN_ADDITEMREWARD$NO_ITEM("&cYou have to hold an item.", true),
    COMMAND$ADMIN_REMOVEQUEST$HELP("&aRemove quest to a player", false),
    COMMAND$ADMIN_REMOVEQUEST$USE("/questadmin removequest %model_player% %model_quest_id%", false),
    COMMAND$ADMIN_REMOVEQUEST$SUCCESS("&aQuest successfully remove to &b%player%", true),
    COMMAND$ADMIN_REMOVEQUEST$QUEST_NOT_FOUND("&cCannot find the quest", true),
    COMMAND$ADMIN_QUESTINFO$HELP("&aDisplay quest information", false),
    COMMAND$ADMIN_QUESTINFO$USE("/questadmin questinfo %model_player% %model_quest_id%", false),
    COMMAND$ADMIN_QUESTINFO$SUCCESS(Arrays.asList(
            "%prefix%ID: %quest_id%",
            "%prefix%Player name:  %player%",
            "%prefix%Quest model: %quest_model%",
            "%prefix%Start: %quest_start%",
            "%prefix%End: %quest_end%",
            "%prefix%Complete: %quest_complete%",
            "%prefix%Progress: %quest_progress%")),
    COMMAND$ADMIN_QUESTINFO$QUEST_NOT_FOUND("&cCannot find the quest", true),
    COMMAND$ADMIN_ADDPROGRESS$HELP("&aAdvances a player's quest", false),
    COMMAND$ADMIN_ADDPROGRESS$USE("/questadmin addprogress %model_player% %model_quest_model% %model_number% ", false),
    COMMAND$ADMIN_ADDPROGRESS$SUCCESS("&aQuest progress successfully modified"),
    COMMAND$ADMIN_ADDPROGRESS$QUEST_NOT_FOUND("&cCannot find the quest", true),
    COMMAND$ADMIN_SETPROGRESS$HELP("&aDefines the progress of a player's quest", false),
    COMMAND$ADMIN_SETPROGRESS$USE("/questadmin setprogress %model_player% %model_quest_model% %model_number% ", false),
    COMMAND$ADMIN_SETPROGRESS$SUCCESS("&aQuest progress successfully modified"),
    COMMAND$ADMIN_SETPROGRESS$QUEST_NOT_FOUND("&cCannot find the quest", true),
    COMMAND$QUEST$HELP("&aSee your quest", false),
    COMMAND$QUEST$USE("/quest", false),

    ADMIN$RELOAD_SUCCESS("&aReload successful! Time: &b%time%ms", true),

    QUEST$INVENTORY_NAME("&aQuest List", false),
    QUEST$INVENTORY_NAME_OTHER("&b%player% &aQuest List", false),
    QUEST$COMPLETED("&aCompleted", false),
    QUEST$FAILED("&cFailed", false),

    SYSTEM$DISABLING_PLUGIN("Disabling Plugin", false);

    private final Object message;
    private static String fileName;
    private static File configFile;
    private static FileConfiguration config;
    private static Main plugin;
    private static HashMap<String, Object> messages;

    Message(String message, boolean prefix) {
        String PREFIX = prefix ? Config.PREFIX_PLUGIN.getString() : "";
        this.message = PREFIX + message;
    }

    Message(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (String) message;
    }

    public static void reload() {
        messages.clear();
        Message.plugin = Main.getInstance();
        Message.fileName = Config.LANGUAGE_FILE.getString();
        Message.configFile = new File(Message.plugin.getDataFolder() + File.separator + "Language", Message.fileName);
        Message.config = Message.plugin.loadConfiguration(Message.configFile, Message.fileName);
        if (Message.fileName != null) {
            Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.getString().replace("%file%", Message.fileName));
        } else {
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$CANNOT_FIND_LANGUAGE_FILE.getString().replace("%file%", Message.fileName));
        }
        Arrays.stream(Message.values()).forEach(message -> message.get());
    }

    private String getKey() {
        return this.name().toLowerCase()
                .replace("_", "-")
                .replace("$", ".")
                .replace("0", ",");
    }

    private Object get() {
        final String key = this.getKey();
        if (Message.messages.containsKey(key)) {
            return Message.messages.get(key);
        }
        Object value;
        try {
            value = Message.config.get(key);
            if (value == null) {
                value = this.message;
                Message.config.set(key, this.message);
                Message.config.save(Message.configFile);
                Logger.WarnMessageToServerConsole(Message.SYSTEM$ADD_KEY_LANG.toString().replace("%key%", key).replace("%file%", Message.fileName));
            }
        } catch (NullPointerException | IOException npe) {
            value = this.message;
            npe.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "1"));
        }
        Message.messages.put(key, value);
        return value;
    }

    public String getString() {
        return ChatColor.translateAlternateColorCodes('&', (String) get());
    }

    public <T> List<T> getList() {
        return (List<T>) get();
    }

    static {
        Message.messages = new HashMap<>();
    }
}
