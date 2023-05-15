package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.quarters.itemgenerators.ConfigGeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class ConfigItemBuilder implements ItemBuilder {


    private String itemName;
    private ItemStack itemStack;

    @Override
    public ConfigGeneratorItem build() {
        return new ConfigGeneratorItem(itemStack, itemName);
    }

    @Override
    public List<String> options() {
        return List.of("item", "name");
    }

    @Override
    public List<String> complete(Player player, String option) {

        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {
        return null;
    }

    @Override
    public void with(String option, String value) {

    }

    @Override
    public void withId(String id) {

    }

    @Override
    public List<String> missingOptions() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
