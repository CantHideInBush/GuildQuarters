package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StructureStorageImpl implements StructureStorage{


    public StructureStorageImpl() {}

    public StructureStorageImpl(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    @YAMLElement
    private Map<GeneratorItem, Integer> storedItems = new HashMap<>();

    @Override
    public void take(GeneratorItem item, InventoryHolder to, int amount) {
        int stored = storedItems.getOrDefault(item, amount);
        if (stored <= 0 || amount <= 0) return;
        else if (amount > stored) amount = stored;
        ItemStack stack = item.getItem();
        stack.setAmount(amount);
        to.getInventory().addItem(item.getItem());
    }

    @Override
    public void store(ItemGenerator generator) {
        storedItems.put(generator.getItem(),
                storedItems.getOrDefault(generator.getItem(), 0) + generator.getAmount());
    }

}
