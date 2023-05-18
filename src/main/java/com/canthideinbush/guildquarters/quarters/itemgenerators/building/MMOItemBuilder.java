package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.MMOGeneratorItem;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MMOItemBuilder implements GeneratorItemBuilder {

    private String itemId;
    private String type;
    private String mmoId;


    @Override
    public void setSender(CommandSender ignore) {

    }

    @Override
    public GeneratorItem build() {
        return new MMOGeneratorItem(itemId, type, mmoId);
    }

    @Override
    public List<String> options() {
        return List.of("name", "type", "mmoid");
    }

    @Override
    public List<String> complete(CommandSender commandSender, String option) {
        switch (option) {
            case "name" -> {
                return Collections.singletonList(" ");
            }
            case "type" -> {
                return MMOItems.plugin.getTypes().getAllTypeNames();
            }
            case "mmoid" -> {
                if (type != null) return MMOItems.plugin.getTemplates().getTemplateNames(MMOItems.plugin.getTypes().get(type));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {
        switch (option) {
            case "type" -> {
                if (!MMOItems.plugin.getTypes().has(value)) {
                    return "Ta kategoria przedmiotow nie istnieje!";
                }
            }
            case "mmoid" -> {
                if (type == null) {
                    return "Najpierw ustaw numer!";
                }
                if (MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), value) == null) {
                    return "Przedmiot o tej nazwie nie istnieje!";
                }
            }
        }
        return null;
    }

    @Override
    public void with(String option, String value) {
        switch (option) {
            case "name" -> this.itemId = value;
            case "type" -> this.type = value;
            case "mmoid" -> this.mmoId = value;
        }
    }

    @Override
    public List<String> missingOptions() {
        if (isComplete()) return Collections.emptyList();
        ArrayList<String> missing = new ArrayList<>();
        if (itemId == null) missing.add("name");
        if (type == null) missing.add("type");
        if (mmoId == null) missing.add("mmoid");
        return missing;
    }

    @Override
    public boolean isComplete() {
        return itemId != null && type != null && mmoId != null;
    }
}
