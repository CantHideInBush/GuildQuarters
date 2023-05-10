package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.ABSave;
import org.bukkit.inventory.InventoryHolder;

public interface ItemGenerator extends ABSave {

    void generate();
    int getStored();
    int getAmount();
    int getInterval();

    GeneratorItem getItem();

    void take(InventoryHolder holder, int amount);


}
