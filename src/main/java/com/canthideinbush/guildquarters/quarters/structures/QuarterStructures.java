package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematics;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuarterStructures implements ABSave {

    public QuarterStructures(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterStructures() {

    }

    @YAMLElement
    private List<QuarterStructure> quarterStructures = new ArrayList<>();

    public void addStructure(QuarterStructure structure) {
        if (exists(structure.getId())) {
            return;
        }
        quarterStructures.add(structure);
    }

    public void removeStructure(QuarterStructure structure) {
        quarterStructures.remove(structure);
        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            if (quarter.getQuarterObjects().hasStructure(structure.getId())) {
                quarter.getQuarterObjects().removeStructure(structure.getId());
            }
        }
    }

    public void removeStructure(String quarterStructure) {
        if (!exists(quarterStructure)) return;
        removeStructure(findById(quarterStructure));
    }


    public boolean exists(String id) {
        return quarterStructures.stream().anyMatch(q -> q.getId().equalsIgnoreCase(id));
    }

    private QuarterStructure findById(String id) {
        return quarterStructures.stream().filter(q -> q.getId().equalsIgnoreCase(id)).findAny().orElse(null);
    }

    public List<String> getIds() {
        return quarterStructures.stream().map(QuarterStructure::getId).collect(Collectors.toList());
    }

    public QuarterStructure create(String id) {
        if (!exists(id)) return null;
        return (QuarterStructure) findById(id).clone();
    }

    public void save() {
        GuildQ.getInstance().getQuartersStorage().set("Structures", this);
    }





}
