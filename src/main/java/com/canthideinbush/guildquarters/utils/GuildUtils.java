package com.canthideinbush.guildquarters.utils;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.worldgen.EmptyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.logging.Level;

public class GuildUtils {

    public static void createGuildWorld() {
        if (getGuildWorld() != null) return;
        String name = GuildQ.getInstance().getConfig().getString("GuildWorldName", "guildworld");
        GuildQ.getInstance().getLogger().log(Level.INFO, "Generating guild world using name: '" + name + "'");
        WorldCreator creator = new WorldCreator(name);
        creator.generator(new EmptyGenerator());
        creator.createWorld();
    }

    public static World getGuildWorld() {
        return Bukkit.getWorld(GuildQ.getInstance().getConfig().getString("GuildWorldName", "guildworld"));
    }


    public static int getQuarterSize() {
        return GuildQ.getInstance().getConfig().getInt("Quarters.ChunkSize");
    }



}
