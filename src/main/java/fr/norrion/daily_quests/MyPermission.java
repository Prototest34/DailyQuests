package fr.norrion.daily_quests;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public enum MyPermission {
    ADMIN("admin", PermissionDefault.OP),
    ADMIN$RELOAD("admin.reload", PermissionDefault.OP),
    ADMIN$ADD_QUEST("admin.addquest", PermissionDefault.OP),
    ADMIN$ADD_QUEST_OTHER("admin.addquest.other", PermissionDefault.OP),
    ADMIN$ADD_PROGRESS("admin.addprogress", PermissionDefault.OP),
    ADMIN$ADD_ITEM_REWARD("admin.additemreward", PermissionDefault.OP),
    ADMIN$SET_PROGRESS("admin.setprogress", PermissionDefault.OP),
    ADMIN$REMOVE_QUEST("admin.removequest", PermissionDefault.OP),
    ADMIN$QUEST_ACCESS_OTHER("admin.quest.other", PermissionDefault.OP),
    QUEST_ACCESS("quest", PermissionDefault.TRUE),
    ADMIN$QUEST_INFO("quest.info", PermissionDefault.OP),
    ADMIN$RARITY_INFO("admin.rarityinfo", PermissionDefault.OP);

    private final Permission permission;

    MyPermission(String permissionString, PermissionDefault permissionDefault) {
        this.permission = new Permission("daily_quests." + permissionString);
        permission.setDefault(permissionDefault);
    }

    public static void addPermission(PluginManager pluginManager) {
        for (MyPermission p : MyPermission.values()) {
            pluginManager.addPermission(p.permission);
        }
    }

    public Permission getPermission() {
        return permission;
    }
}
