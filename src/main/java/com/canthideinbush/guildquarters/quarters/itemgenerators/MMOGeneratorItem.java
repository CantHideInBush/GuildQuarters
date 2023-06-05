package com.canthideinbush.guildquarters.quarters.itemgenerators;

import com.canthideinbush.utils.storing.YAMLElement;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MMOGeneratorItem implements GeneratorItem {


    public MMOGeneratorItem(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public MMOGeneratorItem(String id, String type, String mmoId) {
        this.type = type;
        this.id = id;
        this.mmoId = mmoId;
    }

    @YAMLElement
    private String type;
    @YAMLElement
    private String id;
    @YAMLElement
    private String mmoId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = MMOItems.plugin.getItem(type, mmoId);
        return item == null ? null : item.clone();
    }

}
