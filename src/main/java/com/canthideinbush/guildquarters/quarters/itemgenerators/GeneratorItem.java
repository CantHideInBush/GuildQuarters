package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.ABSave;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GeneratorItem extends ABSave {


    String getId();
    ItemStack getItem();

    List<GeneratorItem> registeredItems = new ArrayList<>();

    static GeneratorItem get(String id) {
        return registeredItems
                .stream().filter(g -> g.getId().equals(id))
                .findAny().orElse(null);
    }

    static void save() {
        GuildQ.getInstance().getItemsStorage().set("GeneratorItems", registeredItems);
    }

    static void load() {
        registeredItems.addAll((List<GeneratorItem>) GuildQ.getInstance().getItemsStorage().getList("GeneratorItems", Collections.emptyList()));
    }

    static void register(GeneratorItem item) {
        Optional.ofNullable(get(item.getId())).ifPresent(GeneratorItem::unregister);
        registeredItems.add(item);
    }

    static void unregister(GeneratorItem item) {
        registeredItems.remove(item);
    }

    static List<String> getIds() {
        return registeredItems.stream().map(GeneratorItem::getId).collect(Collectors.toList());
    }





}
