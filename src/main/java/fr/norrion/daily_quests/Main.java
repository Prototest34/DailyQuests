package fr.norrion.daily_quests;

import fr.norrion.daily_quests.command.admin.QuestAdminCommand;
import fr.norrion.daily_quests.command.quest.QuestCommand;
import fr.norrion.daily_quests.events.*;
import fr.norrion.daily_quests.fileData.Config;
import fr.norrion.daily_quests.fileData.Message;
import fr.norrion.daily_quests.fileData.QuestData;
import fr.norrion.daily_quests.fileData.QuestModelData;
import fr.norrion.daily_quests.inventory.QuestInventory;
import fr.norrion.daily_quests.utils.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends JavaPlugin {
    private static Main instance;
    private final Startup startupReload;

    public Main() {
        Main.instance = this;
        this.startupReload = new Startup();
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        PurgeQuest.disable();
        QuestCreateCron.disable();
        Logger.InfoMessageToServerConsole(Message.SYSTEM$DISABLING_PLUGIN.getString());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        reload();
        VaultDependency.setupEconomy();
        MyPermission.addPermission(Bukkit.getPluginManager());
        this.getCommand("quest").setExecutor(new QuestCommand());
        this.getCommand("quest").setTabCompleter(new QuestCommand());
        this.getCommand("questadmin").setExecutor(new QuestAdminCommand());
        this.getCommand("questadmin").setTabCompleter(new QuestAdminCommand());
        this.getServer().getPluginManager().registerEvents(new QuestInventoryEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestBlockBreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestBlockPlaceEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestKillEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestProjectileHitEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new QuestEntityBreedEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestEntityShootBowEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuestPlayerFishing(), this);
        if (Config.QUEST$REFRESH.getBoolean()) {
            this.getServer().getPluginManager().registerEvents(new QuestInventoryCloseEvent(), this);
            QuestInventory.launchRefreshInv();
        }
    }

    public static void reload() {
        instance.startupReload.checkIfFilesExist();
        Config.reload();
        Message.reload();
        QuestModelData.reload();
        QuestData.reload();
        PurgeQuest.createPurge();
        QuestCreateCron.createCron();
    }

    public YamlConfiguration loadConfiguration(final File file, final String fileName) {
        Validate.notNull(file, "Cannot load " + fileName + " config file.");
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
            return config;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "2"));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "3"));
        } catch (InvalidConfigurationException e2) {
            Logger.ErrorMessageToServerConsole("Cannot read " + fileName + " config because it is mis-configured, use a online Yaml parser with the error underneath here to find out the cause of the problem and to solve it. If you cannot find the cause yourself, join our discord support server.");
            e2.printStackTrace();
            this.disablePlugin();
        }
        return null;
    }

    public YamlConfiguration loadConfiguration(final BufferedReader reader, final String fileName) {
        Validate.notNull(reader, "Cannot load " + fileName + " config file.");
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(reader);
            return config;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "4"));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$SOMETHING_IS_WRONG.toString().replace("%code%", "5"));
        } catch (InvalidConfigurationException e2) {
            Logger.ErrorMessageToServerConsole("Cannot read " + fileName + " config because it is mis-configured, use a online Yaml parser with the error underneath here to find out the cause of the problem and to solve it. If you cannot find the cause yourself, join our discord support server.");
            e2.printStackTrace();
            this.disablePlugin();
        }
        return null;
    }

    private void disablePlugin() {
        this.getServer().getPluginManager().disablePlugin(this);
    }
}
