package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StructureStorageImpl implements StructureStorage {


    public StructureStorageImpl() {}

    public StructureStorageImpl(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    @YAMLElement
    private Map<String, Integer> storedItems = new HashMap<>();

    @YAMLElement
    private Map<String, Integer> limit = new HashMap<>();

    @Override
    public void take(String itemId, InventoryHolder to, int amount) {
        GeneratorItem item = GeneratorItem.get(itemId);
        if (item == null) return;
        int stored = storedItems.getOrDefault(itemId, amount);
        if (stored <= 0 || amount <= 0) return;
        else if (amount > stored) amount = stored;
        int free = getFreeSlotsFor(item, to);
        if (amount > free) amount = free;
        ItemStack stack = item.getItem();
        stack.setAmount(amount);
        to.getInventory().addItem(stack);
        storedItems.put(itemId, stored - amount);
    }


    public void setLimit(String itemId, int limit) {
        this.limit.put(itemId, limit);
    }

    public int getLimit(String itemId) {
        return limit.getOrDefault(itemId, -1);
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
        String itemId = generator.getItem().getId();
        int amount = generator.getAmount();
        int storedAmount = getAmount(itemId);
        int limit = getLimit(itemId);

        if (getLimit(itemId) >= -1) {
            if (storedAmount >= limit) return;
            if (storedAmount + amount > getLimit(itemId)) {
                amount = limit - storedAmount;
            }
        }

        storedItems.put(itemId,
                storedItems.getOrDefault(itemId, 0) + amount);
    }

    @Override
    public List<String> getAvailable() {
        return new ArrayList<>(storedItems.keySet());
    }

    @Override
    public int getAmount(String itemId) {
        return storedItems.getOrDefault(itemId, 0);
    }

}
