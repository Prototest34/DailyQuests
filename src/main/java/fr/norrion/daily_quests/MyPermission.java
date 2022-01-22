package fr.norrion.daily_quests;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public enum MyPermission {
    ADMIN("admin", PermissionDefault.OP),
    RELOAD("admin.reload", PermissionDefault.OP),
    ADD_QUEST("admin.add", PermissionDefault.OP),
    ADD_QUEST_OTHER("admin.add.other", PermissionDefault.OP),
    QUEST_ACCESS("quest", PermissionDefault.TRUE);

    private final Permission permission;
    MyPermission(String permissionString, PermissionDefault permissionDefault) {
        this.permission = new Permission("daily_quests."+permissionString);
        permission.setDefault(permissionDefault);
    }

    public static void addPermission(PluginManager pluginManager) {
        for (MyPermission p: MyPermission.values()) {
            pluginManager.addPermission(p.permission);
        }
    }

    public Permission getPermission() {
        return permission;
    }
}
