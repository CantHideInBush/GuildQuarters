package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public void register(ItemGenerator generator) {
        Optional.of(get(generator.getId())).ifPresent(this::unregister);
        generators.add(generator);
    }

    public void unregister(ItemGenerator generator) {
        generators.remove(generator);
    }
    public void save() {
        GuildQ.getInstance().getQuartersStorage().set("ItemGenerators", this);
    }


}
