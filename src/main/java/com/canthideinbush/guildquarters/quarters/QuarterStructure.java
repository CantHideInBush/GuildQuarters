package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLConfig;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.world.level.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Map;

@SerializableAs("QuarterStructure")
public class QuarterStructure implements ABSave {



    @YAMLElement
    private String name;
    @YAMLElement
    private String schematic;

    @YAMLElement
    private String undoSchematic;

    @YAMLElement
    private Vector offset;

    public QuarterStructure(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterStructure(String name, String schematicName, BlockVector offset) {
        this.name = name;
        this.schematic = schematicName;
        this.undoSchematic = GuildQ.getInstance().getDataFolder()  + File.separator +  "schematics" + File.separator + "guildq-undo-" + name;
        this.offset = offset;
    }

    public String getSchematic() {
        return schematic;
    }

    BlockVector getOffset() {
        return (BlockVector) offset;
    }

    BlockVector3 getBlockPoint() {
        return BlockVector3.at(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
    }

    public void paste(GuildQuarter quarter) {
        Clipboard clipboard = GuildQ.getInstance().getUtilsProvider().worldEdit.swapAt(quarter.getInitialLocation().add(offset), schematic);
        GuildQ.getInstance().getUtilsProvider().worldEdit.saveClipboard(clipboard, undoSchematic, false);
    }

    public void undo(GuildQuarter quarter) {
        GuildQ.getInstance().getUtilsProvider().worldEdit.pasteAt(quarter.getInitialLocation().add(offset), undoSchematic, false);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "QuarterStructure{" +
                "name='" + name + '\'' +
                ", schematic='" + schematic + '\'' +
                ", undoSchematic='" + undoSchematic + '\'' +
                ", offset=" + offset +
                '}';
    }
}
