package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.YAMLElement;

import java.util.Map;

public class ConstantItemGenerator implements ItemGenerator {


    public ConstantItemGenerator(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public ConstantItemGenerator(String id, String itemId, int amount, int interval) {
        this.itemId = itemId;
        this.amount = amount;
        this.interval = interval;
        this.id = id;
    }


    @YAMLElement
    int amount;

    @YAMLElement
    private int interval;

    @YAMLElement
    private String itemId;

    @YAMLElement
    private String id;

    @Override
    public String getId() {
        return id;
    }

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

    @Override
    public ItemGenerator clone() {
        return new ConstantItemGenerator(id, itemId, amount, interval);
    }
}
