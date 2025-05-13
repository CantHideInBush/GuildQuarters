package com.canthideinbush.guildquarters.utils;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.worldgen.EmptyGenerator;
import org.bukkit.*;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.logging.Level;

public class GuildUtils {

    public static void createGuildWorld() {
        String name = GuildQ.getInstance().getConfig().getString("GuildWorldName", "guildworld");
        WorldCreator creator = new WorldCreator(name);
        if (!doGuildWorldExists()) {
        GuildQ.getInstance().getLogger().log(Level.INFO, "Generating guild world using name: '" + name + "'");
        }
        creator.generator(new EmptyGenerator());
        creator.createWorld();
    }


    public static boolean doGuildWorldExists() {
        return new File(Bukkit.getWorldContainer() + File.separator + GuildQ.getInstance().getConfig().getString("GuildWorldName", "guildworld")).exists();
    }
    public static World getGuildWorld() {
        return Bukkit.getWorld(GuildQ.getInstance().getConfig().getString("GuildWorldName", "guildworld"));
    }


    public static int getQuarterSize() {
        return GuildQ.getInstance().getConfig().getInt("Quarters.ChunkSize");
    }

    public static String getSchematicName() {
        return GuildQ.getInstance().getConfig().getString("Quarters.schematic-name");
    }

    public static Vector getSpawnOffset() {
        return GuildQ.getInstance().getConfig().getVector("Quarters.SpawnOffset", new Vector());
    }






}
