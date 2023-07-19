package com.canthideinbush.guildquarters.utils;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.utils.worldgen.EmptyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
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


    public static void pasteGuildSchematic(Location location, Runnable runnable) {
        GuildQ.getInstance().getUtilsProvider().worldEdit.pasteAt(location, getSchematicName(), runnable);
    }

    public static int getQuarterSize() {
        return GuildQ.getInstance().getConfig().getInt("Quarters.ChunkSize");
    }

    public static String getSchematicName() {
        return GuildQ.getInstance().getConfig().getString("Quarters.schematic-name");
    }

    public static boolean contains(GuildQuarter quarter, Location location) {
        return contains(quarter, location.toVector());
    }
    public static boolean contains(GuildQuarter quarter, Vector vector) {
        Location min = quarter.getInitialLocation().add(((getQuarterSize() * -16) | 15) - 15, 0, ((getQuarterSize() * -16) | 15) - 15);
        min.setY(getGuildWorld().getMinHeight());
        Location max = quarter.getInitialLocation().add((getQuarterSize() * 16) | 15, 0, (getQuarterSize() * 16) | 15);
        max.setY(getGuildWorld().getMaxHeight());


        return (vector.getX() <= max.getX() && vector.getX() >= min.getX())
                && (vector.getY() <= max.getY() && vector.getY() >= min.getY())
                && (vector.getZ() <= max.getZ() && vector.getZ() >= min.getZ());
    }

    public static Vector getSpawnOffset() {
        return GuildQ.getInstance().getConfig().getVector("Quarters.SpawnOffset", new Vector());
    }


}
