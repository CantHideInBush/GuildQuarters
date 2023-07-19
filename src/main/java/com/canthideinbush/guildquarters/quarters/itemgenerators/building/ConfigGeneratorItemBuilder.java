package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.item.ItemBuildCommand;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ConfigGeneratorItem;
import com.canthideinbush.utils.commands.CHIBCommandsRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigGeneratorItemBuilder implements GeneratorItemBuilder {


    private String itemName;
    private ItemStack itemStack;

    private CommandSender sender;

    @Override
    public void setSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public ConfigGeneratorItem build() {
        return new ConfigGeneratorItem(itemStack, itemName);
    }

    @Override
    public List<String> options() {
        return List.of("item", "name");
    }

    @Override
    public List<String> complete(CommandSender player, String option, String value) {
        if (option.equalsIgnoreCase("name")) return Collections.singletonList(" ");
        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {
        if (option.equals("item")) {
            if (!(sender instanceof Player)) return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage("invalid-command-sender-console");
            else if (((Player) sender).getInventory().getItemInMainHand().getType().isAir()) {
                return CHIBCommandsRegistry.get(ItemBuildCommand.class).getMessagePath("invalid-item");
            }
        }
        return null;
    }

    @Override
    public void with(String option, String value) {
        switch (option) {
            case "item" ->
                    itemStack = ((Player) sender).getInventory().getItemInMainHand();
            case "name" ->
                    this.itemName = value;
        }
    }


    @Override
    public List<String> missingOptions() {
        if (isComplete()) return Collections.emptyList();
        ArrayList<String> missing = new ArrayList<>();
        if (itemName == null) missing.add("name");
        if (itemStack == null) missing.add("item");
        return missing;
    }

    @Override
    public boolean isComplete() {
        return itemName != null && itemStack != null;
    }
}
