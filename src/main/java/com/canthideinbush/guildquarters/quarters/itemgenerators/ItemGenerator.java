package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.ABSave;
import org.bukkit.inventory.InventoryHolder;

public interface ItemGenerator extends ABSave {

    void generate();
    int getAmount();

    int getStartTime();
    void setStartTime(int time);
    int getInterval();
    GeneratorItem getItem();


}
