package fr.norrion.daily_quests.utils;

import fr.norrion.daily_quests.Main;
import fr.norrion.daily_quests.fileData.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultDependency {
    private static Economy econ = null;

    public static void setupEconomy() {
        if (Main.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public static void giveMoney(OfflinePlayer player, double amount) {
        if (econ != null)
            econ.depositPlayer(player, amount);
        else
            Logger.ErrorMessageToServerConsole(Message.SYSTEM$VAULT_NOT_FOUND.getString());
    }
}
