package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.ChanceMap;
import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomItemGenerator implements ItemGenerator {



    @YAMLElement
    private String id;
    @YAMLElement
    private int interval;

    public RandomItemGenerator(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public RandomItemGenerator(String id) {
        this.id = id;
    }

    public RandomItemGenerator(String id, int interval, ChanceMap<String> map, Map<String, ChanceMap<Integer>> amountMap) {
        this.id = id;
        this.interval = interval;
        this.itemMap = map;
        this.amountMap = amountMap;
    }

    public void addItem(String id, double chance) {
        itemMap.add(id, chance);
    }

    public void removeItem(String id) {
        itemMap.remove(id);
    }

    @YAMLElement
    private ChanceMap<String> itemMap = new ChanceMap<>();

    @YAMLElement
    private Map<String, ChanceMap<Integer>> amountMap = new HashMap<>();

    @Override
    public String getId() {
        return id;
    }

    private String currentItem = null;
    private int amount = 0;

    @Override
    public void generate() {
        currentItem = itemMap.getRandom();
        ChanceMap<Integer> amountMap = this.amountMap.get(currentItem);
        if (amountMap == null) amount = 1;
        else amount = amountMap.getRandomOrDefault(1);
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @YAMLElement
    private int startTime = 0;

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
    public List<String> available() {
        return new ArrayList<>(itemMap.getKeys());
    }

    @Override
    public GeneratorItem getItem() {
        return GeneratorItem.get(currentItem);
    }

    @Override
    public RandomItemGenerator clone() {
        return new RandomItemGenerator(id, interval, itemMap, amountMap);
    }

    public ChanceMap<String> getItemMap() {
        return itemMap;
    }

    public Map<String, ChanceMap<Integer>> getAmountMap() {
        return amountMap;
    }
}
