package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.commands.CHIBCommandsRegistry;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ToGuildWorldCommand extends InternalCommand  {

    @Override
    public boolean execute(Player sender, String[] args) {

        World w = GuildUtils.getGuildWorld();

        if (w == null) {
            sendConfigErrorMessage(sender, "guildq-world-nonexistent");
            return false;
        }
        else {
            if (!sender.getWorld().equals(w)) {
                sendConfigSuccessMessage(sender, "guildq-world-enter");
                sender.teleport(w.getSpawnLocation());
            }
            else {
                sendConfigSuccessMessage(sender, "guildq-world-exit");
                sender.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
        }

        return true;
    }

    @Override
    public String getName() {
        return "toguildworld";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }


    @Override
    public String getPermission() {
        return "toguildworld";
    }

    @Override
    public List<String> complete(String[] args) {
        return Collections.emptyList();
    }
}
