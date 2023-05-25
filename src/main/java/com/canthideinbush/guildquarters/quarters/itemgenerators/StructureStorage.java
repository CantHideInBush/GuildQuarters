package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.ABSave;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;

public interface StructureStorage extends ABSave {

    void take(GeneratorItem item, InventoryHolder to, int amount);

    void store(ItemGenerator generator);

    List<String> getAvailable();

    int getAmount(GeneratorItem item);

}
