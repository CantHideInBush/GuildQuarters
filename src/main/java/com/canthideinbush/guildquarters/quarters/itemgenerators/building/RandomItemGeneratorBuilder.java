package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.generators.GeneratorParentCommand;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.RandomItemGenerator;
import com.canthideinbush.guildquarters.utils.Utils;
import com.canthideinbush.utils.ChanceMap;
import com.canthideinbush.utils.commands.ConfigMessageExtension;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class RandomItemGeneratorBuilder implements GeneratorBuilder, ConfigMessageExtension {




    public RandomItemGeneratorBuilder(RandomItemGenerator item) {
        this.chanceMap = item.getItemMap();
        this.id = item.getId();
        this.amountMap = item.getAmountMap();
        this.interval = item.getInterval();
    }


    public RandomItemGeneratorBuilder() {
        this.chanceMap = new ChanceMap<>();
        this.amountMap = new HashMap<>();

    }




    private final ChanceMap<String> chanceMap;
    private final Map<String, ChanceMap<Integer>> amountMap;
    private String id;

    private int interval = 1;

    @Override
    public RandomItemGenerator build() {
        return new RandomItemGenerator(id, interval, chanceMap, amountMap);
    }

    @Override
    public List<String> options() {
        return List.of("id", "add", "remove", "modify", "addamount", "removeamount", "modifyamount");
    }


    @Override
    public List<String> complete(CommandSender commandSender, String option, String[] values) {
        switch (option) {
            case "id" -> {
                return Collections.singletonList(" ");
            }
            case "interval" -> {
                return Collections.singletonList(interval + "");
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
            case "removeamount" -> {
                if (values.length == 2) {
                    ChanceMap<Integer> amounts = amountMap.get(values[0]);
                    if (amounts != null) {
                        return amounts.getKeys().stream().map(Object::toString).collect(Collectors.toList());
                    }
                }
                return new ArrayList<>(amountMap.keySet());
            }
            case "addamount" -> {
                if (values.length == 1) {
                    return new ArrayList<>(chanceMap.getKeys());
                }
                else if (values.length == 2) {
                    return Collections.singletonList("1");
                }
                else if (values.length == 3) {
                    return Collections.singletonList("%");
                }

            }
            case "modifyamount" -> {
                if (values.length == 1) {
                    return new ArrayList<>(amountMap.keySet());
                }
                else if (values.length == 2) {
                    return Collections.singletonList("1");
                }
                else if (values.length == 3) {
                    return Collections.singletonList("%");
                }
            }
        }
        return Collections.emptyList();
    }


    @Override
    public String errorFor(String option, String[] values) {
        String value = values[0];
        switch (option) {
            case "remove" -> {
                if (!chanceMap.contains(value)) {
                    return "Podany przedmiot nie jest dodany jako wartosc!";
                }
            }
            case "interval" -> {
                if (!Utils.isNumeric(value)) {
                    return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage("incorrect_data_type");
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
            case "addamount","modifyamount" -> {
                if (values.length < 3) {
                    return GuildQ.getMessage("command-arguments-insufficient");
                }
                else if (!chanceMap.contains(value)) {
                    return getMessage("item-not-added");
                }
                else if (!Utils.isNumeric(values[1]) || !Utils.isNumeric(values[2])) {
                    return GuildQ.getMessage("incorrect_data_type");
                }
            }
            case "removeamount" -> {
                if (values.length < 2) {
                    return GuildQ.getMessage("command-arguments-insufficient");
                }
                if (!Utils.isNumeric(values[1])) {
                    return GuildQ.getMessage("incorrect_data_type");
                }
            }
        }
        return null;
    }

    @DefaultConfigMessage(forN = "item-not-added")
    private static final String ITEM_NOT_ADDED = "Podany przemdiot nie zostal jeszcze dodany do generatora!";

    @Override
    public void with(String option, String[] values) {
        String value = values[0];
        switch (option) {
            case "id" -> this.id = value;
            case "interval" -> this.interval = Integer.parseInt(value);
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
            case "addamount" -> {
                if (!amountMap.containsKey(value)) amountMap.put(value, new ChanceMap<>());
                ChanceMap<Integer> amounts = amountMap.get(value);
                amounts.add(Integer.parseInt(values[1]), Double.parseDouble(values[2]));
            }
            case "modifyamount" -> {
                if (!amountMap.containsKey(value)) amountMap.put(value, new ChanceMap<>());
                ChanceMap<Integer> amounts = amountMap.get(value);
                int key = Integer.parseInt(values[1]);
                amounts.remove(key);
                amounts.add(key, Double.parseDouble(values[2]));
            }
            case "removeamount" -> {
                ChanceMap<Integer> amounts;
                if ((amounts = amountMap.get(value)) != null) {
                    amounts.remove(Integer.parseInt(values[1]));
                }
            }
        }
    }

    @Override
    public List<String> formatCompletion(List<String> completion, String option, String[] values) {
        return GeneratorBuilder.super.formatCompletion(completion, option, values);
    }

    @Override
    public List<String> missingOptions() {
        if (isComplete()) return Collections.emptyList();
        ArrayList<String> missing = new ArrayList<>();
        if (id == null) missing.add("id");
        if (chanceMap.isEmpty()) missing.add("add");
        return missing;
    }

    @Override
    public boolean isComplete() {
        return !chanceMap.isEmpty() && id != null;
    }

    @Override
    public Class<? extends InternalCommand> getCommandClass() {
        return GeneratorParentCommand.class;
    }
}
