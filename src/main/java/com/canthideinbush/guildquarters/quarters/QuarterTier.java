package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.Map;


@SerializableAs("QuarterTier")
public class QuarterTier implements ABSave, Keyed<Object> {

    public QuarterTier(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterTier(int index) {
        this.index = index;
    }

    @YAMLElement
    private int index;


    @YAMLElement
    public QuarterStructures structures = new QuarterStructures();


    @Override
    public Object getKey() {
        return index;
    }

    public void apply(GuildQuarter quarter) {
        structures.apply(quarter);
    }

    public void undo(GuildQuarter quarter) {
        structures.undo(quarter);
    }

    public void addStructure(String schematic, Vector vector) {
        structures.addStructure(schematic, vector);
    }

    public void removeStructure(String schematic) {
        structures.removeStructure(schematic);
    }


}
