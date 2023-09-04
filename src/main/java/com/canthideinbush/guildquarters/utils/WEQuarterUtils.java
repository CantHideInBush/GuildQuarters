package com.canthideinbush.guildquarters.utils;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematics;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.worldedit.SessionProgressTracker;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class WEQuarterUtils {
    public static void pasteDefaultSchematic(Location location, CommandSender observer, Runnable runnable) {

        //self-explanatory
        boolean shouldCreateReference = GuildQ.getInstance().getQuartersManager().referenceTable == null;

        //Starts tracking paste of quarter schematic in location of quarter, and after it's complete runs given runnable. If observer is present, tracking/progress info is sent to him
        SessionProgressTracker progressTracker = GuildQ.getInstance().getUtilsProvider().worldEdit.trackPasteAt(location, GuildUtils.getSchematicName(), (progress) -> {
            if (observer != null) GuildQ.getInstance().getUtilsProvider().chat.sendConfigMessage("common.quarter-pasting-progress", observer, ChatColor.GREEN, progress.getPercentageProgress() + "%");
            if (progress.isComplete()) {
                Bukkit.getScheduler().runTask(GuildQ.getInstance(), runnable);
            }
        }, 20 * 5, GuildQ.getInstance().getQuartersManager().referenceTable);


        //If there is no reference table created yet, create new one and assign it to referenceTable variable
        if (shouldCreateReference) {
            progressTracker.createReferenceTracker((referenceTable) -> GuildQ.getInstance().getQuartersManager().referenceTable = referenceTable);
        }
    }

    public static void updateQuarterSchematic(GuildQuarter quarter, CommandSender observer) {
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> {

            //Get build quarter region, convert to WE and copy it's content to clipboard
            ProtectedCuboidRegion protectedRegion = quarter.getRegion().region();
            CuboidRegion region = new CuboidRegion(protectedRegion.getMinimumPoint(), protectedRegion.getMaximumPoint());
            Clipboard clipboard = new BlockArrayClipboard(region);
            copyBlocks(clipboard, region);


            saveRegionBackup(clipboard, quarter);

            pasteDefaultSchematic(quarter.getInitialLocation(), observer,() -> {
                if (observer != null) {
                    GuildQ.getInstance().getUtilsProvider().chat.sendConfigMessage(
                            "common.quarter-pasting-complete", observer, ChatColor.GREEN
                    );
                }

                rePasteQuarterSchematics(quarter);

                pasteRegionBackup(clipboard, protectedRegion);
            });





        });
    }


    private static void copyBlocks(Clipboard clipboard, CuboidRegion region) {
        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(BukkitAdapter.adapt(GuildUtils.getGuildWorld()),
                region, clipboard, region.getMinimumPoint());
        try {
            Operations.complete(forwardExtentCopy);
        } catch (
                WorldEditException e) {
            throw new RuntimeException(e);
        }
    }


    private static void saveRegionBackup(Clipboard clipboard, GuildQuarter quarter) {
        GuildQ.getInstance().getUtilsProvider().worldEdit.saveClipboard(clipboard,
                quarter.getShortId() + REGION_BACKUP_CLIPBOARD_SUFFIX,
                false);
    }

    private static void rePasteQuarterSchematics(GuildQuarter quarter) {
        QuarterSchematics quarterSchematics = GuildQ.getInstance().getQuarterSchematics();

        //Iterates over quarter schematics, and pastes all of them
        for (String name : quarter.getQuarterObjects().getSchematics()) {
            QuarterSchematic quarterSchematic;
            if ((quarterSchematic = quarterSchematics.getByName(name)) != null) {
                quarterSchematic.paste(quarter);
            }
        }

        //Iterates over quarter structures and their schematics, effectively pasting all of them
        for (QuarterStructure structure : quarter.getQuarterObjects().getStructures()) {
            for (String name : structure.getSchematics()) {
                QuarterSchematic quarterSchematic;
                if ((quarterSchematic = quarterSchematics.getByName(name)) != null) {
                    quarterSchematic.paste(quarter);
                }
            }
        }

    }

    private static void pasteRegionBackup(Clipboard clipboard, ProtectedCuboidRegion protectedRegion) {
        BlockVector3 minPoint = protectedRegion.getMinimumPoint();
        GuildQ.getInstance().getUtilsProvider().worldEdit.pasteAt(new Location(GuildUtils.getGuildWorld(), minPoint.getBlockX(), minPoint.getBlockY(), minPoint.getBlockZ()), clipboard);
    }

    public static final String REGION_BACKUP_CLIPBOARD_SUFFIX = "_region_backup";

}
