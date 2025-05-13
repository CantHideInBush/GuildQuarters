package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemGenerators implements ABSave {

    public ItemGenerators() {
        generators = new ArrayList<>();
    }

    public ItemGenerators(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    @YAMLElement
    private List<ItemGenerator> generators;

    public ItemGenerator get(String id) {
        return generators
                .stream().filter(g -> g.getId().equals(id))
                .findAny().map(ItemGenerator::clone).orElse(null);
    }

    public List<String> getIds() {
        return generators
                .stream().map(ItemGenerator::getId).collect(Collectors.toList());
    }

    public void register(ItemGenerator generator) {
        Optional.ofNullable(get(generator.getId())).ifPresent(this::unregister);
        generators.add(generator);
    }

    public void unregister(ItemGenerator generator) {
        generators.remove(generator);
        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            for (QuarterStructure structure : quarter.getQuarterObjects().getStructures()) {
                structure.removeGenerator(generator);
            }
        }
    }
    public void save() {
        GuildQ.getInstance().getQuartersStorage().set("ItemGenerators", this);
    }


    public List<ItemGenerator> getGenerators() {
        return generators;
    }
}
