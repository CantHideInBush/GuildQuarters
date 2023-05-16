package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ConstantItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstantGeneratorBuilder implements GeneratorBuilder {



    int amount = -1;

    private int interval = -1;

    private String itemId;

    private String id;

    @Override
    public ItemGenerator build() {
        return new ConstantItemGenerator(id, itemId, amount, interval);
    }


    private final List<String> options = List.of("id", "itemid", "amount", "interval");
    @Override
    public List<String> options() {
        return options;
    }


    @Override
    public List<String> complete(CommandSender player, String option) {
        switch (option.toLowerCase()) {
            case "amount","interval" -> {
                return Collections.singletonList("0");
            }
            case "itemid" -> {
                return GeneratorItem.getIds();
            }
            case "id" -> {
                return Collections.singletonList(" ");
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {
        switch (option.toLowerCase()) {
            case "amount","interval" -> {
                if (!Utils.isNumeric(value)) {
                    return GeneratorBuilder.INCORRECT_DATA_TYPE();
                }
                int val = Integer.parseInt(value);
                if (val <= 0) {
                    return GeneratorBuilder.MUST_BE_POSITIVE();
                }
            }
            case "itemid" -> {
                if (GeneratorItem.get(value) == null) {
                    return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage(
                      "common.generator-item-nonexistent"
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void with(String option, String value) {
        switch (option.toLowerCase()) {
            case "amount" -> {
                amount = Integer.parseInt(value);
            }
            case "interval" -> {
                interval = Integer.parseInt(value);
            }
            case "itemid" -> {
                itemId = value;
            }
            case "id" -> {
                id = value;
            }
        }
    }

    @Override
    public void withId(String id) {
        this.id = id;
    }

    @Override
    public List<String> missingOptions() {
        if (isComplete()) return Collections.emptyList();
        ArrayList<String> missing = new ArrayList<>();
        if (id == null) {
            missing.add("id");
        }
        if (itemId == null) {
            missing.add("itemId");
        }
        if (amount == -1) {
            missing.add("amount");
        }
        if (interval == -1) {
            missing.add("interval");
        }

        return missing;
    }

    @Override
    public boolean isComplete() {
        return id != null && itemId != null && amount != -1 && interval != -1;
    }



}
