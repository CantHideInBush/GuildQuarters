package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.ABSave;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public interface ItemGenerator extends ABSave {


    String getId();
    void generate();
    int getAmount();
    int getStartTime();
    void setStartTime(int time);
    int getInterval();

    List<String> available();
    GeneratorItem getItem();
    ItemGenerator clone();


}
