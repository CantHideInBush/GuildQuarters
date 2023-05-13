package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.ABSave;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public interface StructureStorage extends ABSave {

    void take(GeneratorItem item, InventoryHolder to, int amount);

    void store(ItemGenerator generator);

}
