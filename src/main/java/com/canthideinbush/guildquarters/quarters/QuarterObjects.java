package com.canthideinbush.guildquarters.quarters;


import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class containing all kinds of modifiable objects for quarter
 */
public class QuarterObjects implements ABSave {

    public QuarterObjects(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterObjects() {}

    private GuildQuarter quarter;

    public void initialize(GuildQuarter quarter) {
        this.quarter = quarter;
        quarterStructures.forEach(qS -> qS.initialize(quarter));
    }

    @YAMLElement
    private List<String> quarterSchematics = new ArrayList<>();

    @YAMLElement
    private List<QuarterStructure> quarterStructures = new ArrayList<>();






    public List<String> getStructureIds() {
        return quarterStructures.stream().map(QuarterStructure::getId).collect(Collectors.toList());
    }




    public boolean hasStructure(String id) {
        return quarterStructures.stream().anyMatch(s -> s.getId().equalsIgnoreCase(id));
    }

    public QuarterStructure getStructure(String id) {
        return quarterStructures.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findAny().orElse(null);
    }

    public boolean hasSchematic(String name) {
        return quarterSchematics.stream().anyMatch(s -> s.equalsIgnoreCase(name));
    }



    public void placeStructure(QuarterStructure structure) {
        quarterStructures.add(structure);
        for (String schem : structure.getSchematics()) {
            placeSchematic(schem);
        }
        structure.initialize(quarter);
    }

    public void removeStructure(QuarterStructure structure) {
        quarterStructures.remove(structure);
        for (String schem : structure.getSchematics()) {
            removeSchematic(schem);
        }
    }

    public void removeStructure(String structureId) {
        quarterStructures.removeIf(q -> {

            if  (q.getId().equalsIgnoreCase(structureId)) {
                for (String schem : q.getSchematics()) {
                    removeSchematic(schem);
                }
                return true;
            }
            return false;
        });
    }







    public void placeSchematic(String name) {
        QuarterSchematic schematic;
        if ((schematic = GuildQ.getInstance().getQuarterSchematics().getByName(name)) == null) {
            throw new IllegalArgumentException("Quarter schematic with given name does not exist!");
        }

        placeSchematic(schematic);
    }


    public void placeSchematic(@NotNull QuarterSchematic schematic) {
        schematic.paste(quarter);

        quarterSchematics.add(schematic.getName());
    }

    public void removeSchematic(@NotNull QuarterSchematic schematic) {
        if (!quarterSchematics.contains(schematic.getName())) return;
        schematic.undo(quarter);

        quarterSchematics.remove(schematic.getName());
    }

    public void removeSchematic(String name) {
        if (!quarterSchematics.contains(name)) return;

        QuarterSchematic schematic;
        if ((schematic = GuildQ.getInstance().getQuarterSchematics().getByName(name)) == null) {
            throw new IllegalArgumentException("Quarter schematic with given name does not exist!");
        }

        schematic.undo(quarter);

        quarterSchematics.remove(name);
    }



    int second = 0;
    public void tick() {
        for (QuarterStructure structure : quarterStructures) {
            structure.tickGenerators(second);
        }
        second++;
    }






}
