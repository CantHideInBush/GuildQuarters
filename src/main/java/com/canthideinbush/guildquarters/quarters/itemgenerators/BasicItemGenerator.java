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
    int stored;

    @YAMLElement
    int amount;

    @YAMLElement
    private int interval;

    @YAMLElement
    private String itemId;


    @Override
    public void generate() {
        stored += getAmount();
    }

    @Override
    public int getStored() {
        return stored;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public GeneratorItem getItem() {
        return GeneratorItem.get(itemId);
    }

    ItemStack itemStack;
    @Override
    public void take(InventoryHolder holder, int amount) {
        if (amount <= 0) return;
        if (amount > stored) {
            amount = stored;
        }
        stored -= amount;
        if (itemStack == null) {
            itemStack = getItem().getItem();
            itemStack.setAmount(amount);
        }
        holder.getInventory().addItem(itemStack);
    }
}
