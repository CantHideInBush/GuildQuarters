package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;


@SerializableAs("QuarterStructures")
public class QuarterStructures implements ABSave {





    public QuarterStructures(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterStructures() {
    }

    @YAMLElement
    private HashMap<String, Vector> structures = new HashMap<>();

    public void addStructure(String schematic, Vector vector) {
        structures.put(schematic, vector);
    }

    public void removeStructure(String schematic) {
        structures.remove(schematic);
    }

    public void apply(GuildQuarter quarter) {
        structures.forEach((schematic, vector) -> {
            Clipboard clipboard = GuildQ.getInstance().getUtilsProvider().worldEdit.swapAt(quarter.getInitialLocation().add(vector), schematic);
            GuildQ.getInstance().getUtilsProvider().worldEdit.saveClipboard(
                    clipboard, "savedclip.schem"
            );
        });
    }

    public void undo(GuildQuarter quarter) {
        structures.forEach((schematic, vector) -> {
            GuildQ.getInstance().getUtilsProvider().worldEdit.inversePasteAt(quarter.getInitialLocation().add(vector), schematic);
        });
    }


}
