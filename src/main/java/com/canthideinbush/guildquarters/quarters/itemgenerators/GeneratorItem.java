package com.canthideinbush.guildquarters.quarters.itemgenerators;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GeneratorItem {


    String getId();
    ItemStack getItem();

    ArrayList<GeneratorItem> registeredItems = new ArrayList<>();

    static GeneratorItem get(String id) {
        return registeredItems
                .stream().filter(g -> g.getId().equals(id))
                .findAny().orElse(null);
    }

    static void register(GeneratorItem item) {
        Optional.of(get(item.getId())).ifPresent(GeneratorItem::unregister);
        registeredItems.add(item);
    }

    static void unregister(GeneratorItem item) {
        registeredItems.remove(item);
    }

    static List<String> getIds() {
        return registeredItems.stream().map(GeneratorItem::getId).collect(Collectors.toList());
    }



}
