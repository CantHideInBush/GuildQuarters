package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.structure.StructureBuildCommand;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.quarters.itemgenerators.*;
import com.canthideinbush.guildquarters.utils.Utils;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.ConfigMessageExtension;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StructureBuilder implements ObjectBuilder<QuarterStructure>, ConfigMessageExtension {



    private String id;
    private List<ItemGenerator> generators;
    private List<String> schematics;
    private StructureStorage storage;
    private Vector collectionBlock;


    public StructureBuilder() {
        generators = new ArrayList<>();
        schematics = new ArrayList<>();
        storage = new StructureStorageImpl();
    }


    private Player sender;
    public void setSender(Player sender) {
        this.sender = sender;
    }

    public StructureBuilder(QuarterStructure structure) {
        this.id = structure.getId();
        this.generators = structure.getGenerators();
        this.storage = structure.getStorage();
        this.schematics = structure.getSchematics();
        this.collectionBlock = structure.getCollectionBlock();
    }

    public StructureBuilder withGenerator(ItemGenerator generator) {
        generators.add(generator);
        return this;
    }

    public StructureBuilder withStorage(StructureStorage storage) {
        this.storage = storage;
        return this;
    }

    public StructureBuilder withId(String id) {
        this.id = id;
        return this;
    }
    public StructureBuilder withSchem(String id) {
        if (schematics.contains(id)) return this;
        schematics.add(id);
        return this;
    }

    public String getId() {
        return id;
    }

    public List<ItemGenerator> getGenerators() {
        return generators;
    }

    public StructureStorage getStorage() {
        return storage;
    }

    public QuarterStructure build() {
        return new QuarterStructure(this);
    }

    @Override
    public List<String> options() {
        return List.of("id", "addgen", "addschem", "remgen", "remschem", "setlimit", "remlimit", "collectionblock");
    }

    @Override
    public List<String> complete(CommandSender commandSender, String option, String value) {

        switch (option) {
            case "id" -> {
                return Collections.singletonList(" ");
            }
            case "addgen" -> {
                return GuildQ.getInstance().getItemGenerators().getIds();
            }
            case "addschem" -> {
                return GuildQ.getInstance().getQuarterSchematics().getNames();
            }
            case "remgen" -> {
                return generators.stream().map(ItemGenerator::getId).collect(Collectors.toList());
            }
            case "remschem" -> {
                return schematics;
            }
            case "setlimit" -> {
                List<String> available = new ArrayList<>();
                generators.forEach(g -> available.addAll(g.available()));

                String itemId;
                if (value.contains(":")) itemId = value.split(":")[0];
                else itemId = value;

                if (available.stream().noneMatch(g -> g.equalsIgnoreCase(itemId))) {
                    return available.stream().map(s -> s + ":" + storage.getLimit(s)).collect(Collectors.toList());
                }
                else if (!value.contains(":")) {
                    return Collections.singletonList(value + ":" + storage.getLimit(itemId));
                }
                else if (value.endsWith(":")) {
                    return Collections.singletonList(value + storage.getLimit(itemId));
                }

            }
            case "remlimit" -> {
                List<String> available = new ArrayList<>();
                generators.forEach(g -> available.addAll(g.available()));
                return available;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {

        switch (option) {
            case "addgen" -> {
                if (GuildQ.getInstance().getItemGenerators().get(value) == null) {
                    return getMessage("generator-nonexistent");
                }
            }
            case "addschem" -> {
                if (GuildQ.getInstance().getQuarterSchematics().getByName(value) == null) {
                    return getMessage("schem-nonexistent");
                }
            }
            case "collectionblock" -> {
                if (sender.getTargetBlock(6) == null) {
                    return getMessage("common.no-target-block");
                }
            }
            case "remgen" -> {
                if (generators.stream().noneMatch(g -> value.equalsIgnoreCase(g.getId()))) {
                    return getMessage("generator-nonexistent");
                }
            }
            case "remschem" -> {
                if (!schematics.contains(option)) {
                    return getMessage("schem-nonexistent");
                }
            }
            case "setlimit" -> {
                if (!value.contains(":") || value.endsWith(":")) {
                    return getMessage("limit-format");
                }
                String[] split = value.split(":");
                if (!Utils.isNumeric(split[1])) return getMessage("limit-format");
            }
        }

        return null;
    }

    @DefaultConfigMessage(forN = "limit-format")
    private static final String LIMIT_FORMAT = "Prawidlowy format: item:limit";

    @DefaultConfigMessage(forN = "generator-nonexistent")
    private static final String GEN_NONEXISTENT = "Ten generator nie istnieje!";

        @DefaultConfigMessage(forN = "schem-nonexistent")
    private static final String SCHEM_NONEXISTENT = "Ten schemat nie istnieje!";



    @Override
    public Class<? extends InternalCommand> getCommandClass() {
        return StructureBuildCommand.class;
    }

    @Override
    public void with(String option, String value) {
        switch (option) {
            case "id" -> {
                id = value;
            }
            case "addgen" -> {
                this.withGenerator(GuildQ.getInstance().getItemGenerators().get(value));
            }
            case "addschem" -> {
                this.withSchem(value);
            }
            case "collectionblock" -> {
                Block target = sender.getTargetBlock(6);
                if (target == null) break;
                this.collectionBlock = target.getLocation().subtract(QuartersManager.templateQuarter.getInitialLocation()).toVector();
            }
            case "remgen" -> {
                generators.removeIf(g -> g.getId().equalsIgnoreCase(value));
            }
            case "remschem" -> {
                schematics.remove(value);
            }
            case "setlimit" -> {
                String[] split = value.split(":");
                storage.setLimit(split[0], Integer.parseInt(split[1]));
            }
            case "remlimit" -> {
                storage.setLimit(value, -1);
            }
        }
    }

    @Override
    public List<String> missingOptions() {
        if (id == null) return Collections.singletonList("id");

        return Collections.emptyList();
    }

    @Override
    public boolean isComplete() {
        return id != null;
    }

    public List<String> getSchematics() {
        return schematics;
    }

    public Vector getCollectionBlock() {
        return collectionBlock;
    }
}
