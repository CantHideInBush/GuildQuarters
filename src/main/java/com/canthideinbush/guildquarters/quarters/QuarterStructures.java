package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
    private ArrayList<QuarterStructure> structures = new ArrayList<>();

    public boolean addStructure(String name, String schematic, Vector vector) {
        if (getByName(name) == null) {
            structures.add(new QuarterStructure(name, schematic, vector.toBlockVector()));
            return true;
        }
        return false;
    }

    public boolean removeStructure(String name) {
        QuarterStructure structure = structures.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findAny().orElse(null);
        if (structure == null) return false;
        structures.remove(structure);
        return true;
    }

    public void apply(GuildQuarter quarter) {
        structures.forEach(structure -> structure.paste(quarter));
    }

    public void undo(GuildQuarter quarter) {
        structures.forEach(structure -> structure.undo(quarter));
    }

    public void apply(GuildQuarter quarter, String name) {
        List<QuarterStructure> structures = this.structures.stream().filter(
          structure -> structure.getName().equalsIgnoreCase(name)).collect(Collectors.toList())
        ;
        structures.forEach(structure -> structure.paste(quarter));
    }
    public void undo(GuildQuarter quarter, String name) {
        List<QuarterStructure> structures = this.structures.stream().filter(
          structure -> structure.getName().equalsIgnoreCase(name)).collect(Collectors.toList())
        ;
        structures.forEach(structure -> structure.undo(quarter));
    }

    public QuarterStructure getByName(String name) {
        return structures.stream().filter(s -> s.getName().equalsIgnoreCase(name)).findAny().orElse(null);

    }

    @Override
    public String toString() {
        return "QuarterStructures{" +
                "structures=" + structures +
                '}';
    }
}
