package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        int free = getFreeSlotsFor(item, to);
        if (amount > free) amount = free;
        ItemStack stack = item.getItem();
        stack.setAmount(amount);
        ItemStack i = item.getItem();
        i.setAmount(amount);
        to.getInventory().addItem(i);
    }

    private int getFreeSlotsFor(GeneratorItem item, InventoryHolder holder) {
        int free = 0;
        ItemStack stack = item.getItem();
        for (ItemStack itemStack : holder.getInventory().getContents()) {
            if (itemStack == null) free += stack.getMaxStackSize();
            else if (itemStack.isSimilar(stack)) free += stack.getMaxStackSize() - itemStack.getAmount();
        }
        return free;
    }

    @Override
    public void store(ItemGenerator generator) {
        storedItems.put(generator.getItem(),
                storedItems.getOrDefault(generator.getItem(), 0) + generator.getAmount());
    }

    @Override
    public List<String> getAvailable() {
        return storedItems.keySet().stream().map(GeneratorItem::getId).collect(Collectors.toList());
    }

    @Override
    public int getAmount(GeneratorItem item) {
        return storedItems.getOrDefault(item, 0);
    }

}
