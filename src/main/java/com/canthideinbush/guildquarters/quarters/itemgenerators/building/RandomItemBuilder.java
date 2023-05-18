package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.RandomGeneratorItem;
import com.canthideinbush.guildquarters.utils.Utils;
import com.canthideinbush.utils.ChanceMap;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomItemBuilder implements GeneratorItemBuilder {

    public RandomItemBuilder() {
        this.chanceMap = new ChanceMap<>();
    }

    @Override
    public void setSender(CommandSender sender) {

    }

    private final ChanceMap<String> chanceMap;
    private String name;


    @Override
    public RandomGeneratorItem build() {
        return new RandomGeneratorItem(name, chanceMap);
    }

    @Override
    public List<String> options() {
        return List.of("name", "add", "remove", "modify");
    }

    @Override
    public List<String> complete(CommandSender commandSender, String option) {
        switch (option) {
            case "name" -> {
                return Collections.singletonList(" ");
            }
            case "remove" -> {
                return new ArrayList<>(chanceMap.getKeys());
            }
            case "add" -> {
                return GeneratorItem.getIds().stream().map(id -> id + ":%").collect(Collectors.toList());
            }
            case "modify" -> {
                return chanceMap.getKeys().stream().map(id -> id + ":%").collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {
        switch (option) {
            case "remove" -> {
                if (!chanceMap.contains(value)) {
                    return "Podany przedmiot nie jest dodany jako wartosc!";
                }
            }
            case "add","modify" -> {
                if (!value.contains(":")) {
                    return "Wartosc musi zostac podana w formacie przedmiot:szansa!";
                }
                String[] args = value.split(":");
                if (args.length < 2) {
                    return "Wartosc musi zostac podana w formacie przedmiot:szansa!";
                }
                if (!Utils.isNumeric(args[1])) {
                    return "Szansa musi byc dodatnia liczba rzeczywista!";
                }
            }
        }
        return null;
    }

    @Override
    public void with(String option, String value) {
        switch (option) {
            case "name" -> this.name = value;
            case "remove" -> chanceMap.remove(value);
            case "add" -> {
                String[] args = value.split(":");
                chanceMap.add(args[0], Double.parseDouble(args[1]));
            }
            case "modify" -> {
                String[] args = value.split(":");
                chanceMap.remove(args[0]);
                chanceMap.add(args[0], Double.parseDouble(args[1]));
            }
        }
    }

    @Override
    public List<String> missingOptions() {
        if (isComplete()) return Collections.emptyList();
        ArrayList<String> missing = new ArrayList<>();
        if (name == null) missing.add("name");
        if (chanceMap.isEmpty()) missing.add("add");
        return missing;
    }

    @Override
    public boolean isComplete() {
        return !chanceMap.isEmpty() && name != null;
    }
}
