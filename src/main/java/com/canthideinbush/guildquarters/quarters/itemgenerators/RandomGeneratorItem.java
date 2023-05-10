package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.ChanceMap;
import org.bukkit.inventory.ItemStack;

public class RandomGeneratorItem implements GeneratorItem {

    private String id;

    public RandomGeneratorItem(String id) {
        this.id = id;
    }

    public void addItem(String id, double chance) {
        itemMap.add(id, chance);
    }

    public void removeItem(String id) {
        itemMap.remove(id);
    }

    private ChanceMap<String> itemMap = new ChanceMap<>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ItemStack getItem() {
        return GeneratorItem.get(itemMap.getRandom()).getItem();
    }

}
