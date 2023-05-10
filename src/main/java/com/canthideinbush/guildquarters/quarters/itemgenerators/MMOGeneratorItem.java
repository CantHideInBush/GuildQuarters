package com.canthideinbush.guildquarters.quarters.itemgenerators;

import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.inventory.ItemStack;

public class MMOGeneratorItem implements GeneratorItem {



    public MMOGeneratorItem(String id, String type, String mmoId) {
        this.type = type;
        this.id = id;
        this.mmoId = mmoId;
    }

    private final String type;
    private final String id;
    private final String mmoId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ItemStack getItem() {
        return MMOItems.plugin.getItem(type, mmoId);
    }

}
