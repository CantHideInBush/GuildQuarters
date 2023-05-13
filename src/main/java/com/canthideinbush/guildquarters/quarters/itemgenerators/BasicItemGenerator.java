package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BasicItemGenerator implements ItemGenerator{


    public BasicItemGenerator(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public BasicItemGenerator(String itemId, int amount, int interval) {
        this.itemId = itemId;
        this.amount = amount;
        this.interval = interval;

    }


    @YAMLElement
    int amount;

    @YAMLElement
    private int interval;

    @YAMLElement
    private String itemId;


    @Override
    public void generate() {
    }


    @Override
    public int getAmount() {
        return amount;
    }

    @YAMLElement
    private int startTime;

    @Override
    public int getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(int time) {
        this.startTime = time;
    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public GeneratorItem getItem() {
        return GeneratorItem.get(itemId);
    }

}
