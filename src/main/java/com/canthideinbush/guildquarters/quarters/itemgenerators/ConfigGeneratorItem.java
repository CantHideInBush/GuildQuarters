package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.guildquarters.GuildQ;
import org.bukkit.inventory.ItemStack;

public class ConfigGeneratorItem implements GeneratorItem {
    private final String name;

    public ConfigGeneratorItem(ItemStack itemStack, String name) {
        GuildQ.getInstance().getItemsStorage().set(name, itemStack);
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
        return GuildQ.getInstance().getItemsStorage()
                .getItemStack(name);
    }
}
