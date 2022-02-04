package fr.norrion.daily_quests.fileData;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.utils.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Config {
    LANGUAGE_FILE("lang-en.yml"),
    PREFIX_PLUGIN("&7[&aDaily Quests&7] "),
    PURGECRON("0 0 * * * ?"),
    PURGE_OLD_HOUR(24),
    QUEST$PATTERN(Arrays.asList(
            "empty,empty,empty,empty,empty,empty,empty,empty,empty",
            "empty,quest,quest,quest,quest,quest,quest,quest,empty",
            "empty,quest,quest,quest,quest,quest,quest,quest,empty",
            "empty,quest,quest,quest,quest,quest,quest,quest,empty",
            "empty,empty,empty,empty,empty,empty,empty,empty,empty",
            "previous,empty,empty,empty,empty,empty,empty,empty,next"
    )),
    QUEST$EMPTY_ITEM("black_stained_glass_pane:1:$"),
    QUEST$NEXT_PAGE_ITEM("arrow:1:&cNext"),
    QUEST$PREVIOUS_PAGE_ITEM("arrow:1:&cPrevious"),
    QUEST$LORE(Arrays.asList(
            "%separator%",
            "%progress%",
            "%separator%",
            "&3Start: &a%start%",
            "&3End: &a%end%",
            "&3Complete: &a%complete%",
            "%separator%",
            "&3Time remaining: %remaining_time%",
            "%separator%",
            "%rewards%",
            "%separator%",
            "%description%"
    )),
    QUEST$SEPARATOR("&7✩&7&l&m━━━━━━━━━━━━━━━━━━━━━━&r&7✩"),
    QUEST$PROGRESS_BAR$PATTERN("%prefix%%progress-bar-character%%suffix%"),
    QUEST$PROGRESS_BAR$PREFIX("&8["),
    QUEST$PROGRESS_BAR$SUFFIX("&8]"),
    QUEST$PROGRESS_BAR$CHARACTER("|"),
    QUEST$PROGRESS_BAR$CHARACTER_NUMBER(40),
    QUEST$PROGRESS_BAR$VALUE_IN_MIDDLE(true),
    QUEST$PROGRESS_BAR$VALUE_IN_MIDDLE_PATTERN("&8(&b%progress-value%/%progress-end%&8)"),
    QUEST$PROGRESS_BAR$GREEN("&a"),
    QUEST$PROGRESS_BAR$RED("&c"),
    QUEST$REFRESH(true),
    QUEST$REFRESH_TIME(20),
    DEBUG(false);

    private static YamlConfiguration config;
    private static Main plugin;
    private static String fileName;
    private static File configFile;
    private final Object value;
    private static HashMap<String, Object> configMap;

    Config(Object o) {
        this.value = o;
    }

    public static void reload() {
        Config.configMap.clear();
        Config.plugin = Main.getInstance();
        Config.fileName = "config.yml";
        Config.configFile = new File(Config.plugin.getDataFolder(), Config.fileName);
        Config.config = Config.plugin.loadConfiguration(Config.configFile, Config.fileName);
        Logger.InfoMessageToServerConsole(Message.SYSTEM$FILE_LOADED_SUCCESS.toString().replace("%file%", Config.fileName));
        Config.QUEST$PATTERN.get();
    }

    private String getKey() {
        return this.name().toLowerCase()
                .replace("_", "-")
                .replace("$", ".")
                .replace("0", ",");
    }

    private Object get() {
        final String key = this.getKey();
        if (Config.configMap.containsKey(key)) {
            return Config.configMap.get(key);
        }
        Object value;
        try {
            value = Config.config.get(key);
            if (value == null) {
                value = this.value;
                Config.config.set(key, this.value);
                Config.config.save(Config.configFile);
                Logger.WarnMessageToServerConsole(Message.SYSTEM$ADD_KEY_LANG.toString().replace("%key%", key).replace("%file%", Config.fileName));
            }
        } catch (NullPointerException | IOException npe) {
            value = this.value;
            npe.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "6"));
        }
        Config.configMap.put(key, value);
        return value;
    }

    public boolean getBoolean() {
        return (boolean) get();
    }

    public String getString() {
        return (String) get();
    }

    public int getInt() {
        return (int) get();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList() {
        return (List<T>) get();
    }

    static {
        Config.configMap = new HashMap<>();
    }
}
