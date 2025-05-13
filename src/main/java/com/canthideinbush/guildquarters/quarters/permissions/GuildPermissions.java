package com.canthideinbush.guildquarters.quarters.permissions;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuildPermissions implements ABSave {

    public GuildPermissions(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    @YAMLElement
    private List<String> registeredPermissions = new ArrayList<>();

    public GuildPermissions() {

    }

    public boolean registerPermission(String permission) {
        if (containsPermission(permission)) return false;
        registeredPermissions.add(permission);
        return true;
    }

    public boolean unregisterPermission(String permission) {
        if (!containsPermission(permission)) return false;
        registeredPermissions.remove(permission);
        return true;
    }

    public List<String> getRegisteredPermissions() {
        return registeredPermissions;
    }

    public boolean containsPermission(String permission) {
        return registeredPermissions.stream().anyMatch(perm -> perm.equalsIgnoreCase(permission));
    }

    public void purgePermissions(Player player) {
        for (String permission : registeredPermissions) {
            GuildQ.getInstance().getPermissions().playerRemove(player, permission);
        }
    }
}
