package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ConfigGeneratorItem implements GeneratorItem {

    @YAMLElement
    private String name;
    @YAMLElement
    private ItemStack itemStack;


    public ConfigGeneratorItem(Map<String, Object> map) {
        deserializeFromMap(map);
    }
    public ConfigGeneratorItem(ItemStack itemStack, String name) {
        this.itemStack = itemStack;
        this.name = name;
    }

    public ConfigGeneratorItem(String name) {
        this.name = name;
    }



    public void remove() {
        GuildQ.getInstance().getItemsStorage().set(name, null);
    }



    @Override
    public String getId() {
        return name;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }
}
