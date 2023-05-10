package com.canthideinbush.guildquarters.quarters.itemgenerators;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public interface GeneratorItem {


    String getId();
    ItemStack getItem();

    ArrayList<GeneratorItem> registeredItems = new ArrayList<>();

    static GeneratorItem get(String id) {
        return registeredItems
                .stream().filter(g -> g.getId().equals(id))
                .findAny().orElse(null);
    }

}
