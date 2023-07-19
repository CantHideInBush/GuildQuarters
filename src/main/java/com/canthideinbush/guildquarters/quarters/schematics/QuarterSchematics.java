package com.canthideinbush.guildquarters.quarters.schematics;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;


@SerializableAs("QuarterStructures")
public class QuarterSchematics implements ABSave {





    public QuarterSchematics(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterSchematics() {
    }

    @YAMLElement
    private ArrayList<QuarterSchematic> schematics = new ArrayList<>();

    public boolean addSchematic(String name, String schematic, Vector vector) {
        if (getByName(name) == null) {
            schematics.add(new QuarterSchematic(name, schematic, vector.toBlockVector()));
            return true;
        }
        return false;
    }

    public boolean removeSchematic(String name) {
        QuarterSchematic schem = schematics.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findAny().orElse(null);
        if (schem == null) return false;
        GuildQ.getInstance().getQuartersManager().getObjects().forEach(
                schem::undo
        );
        schem.deleteUndo();
        schematics.remove(schem);
        return true;
    }


    public QuarterSchematic getByName(String name) {
        return schematics.stream().filter(s -> s.getName().equalsIgnoreCase(name)).findAny().orElse(null);

    }

    public void save() {
        GuildQ.getInstance().getQuartersStorage().set("Schematics", this);
    }

    @Override
    public String toString() {
        return "QuarterStructures{" +
                "structures=" + schematics +
                '}';
    }

    public List<String> getNames() {
        return schematics.stream().map(QuarterSchematic::getName).collect(Collectors.toList());
    }
}
