package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.structure.StructureBuildCommand;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerators;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorageImpl;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.ConfigMessageExtension;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructureBuilder implements ObjectBuilder<QuarterStructure>, ConfigMessageExtension {



    private String id;
    private List<ItemGenerator> generators;
    private List<String> schematics;
    private StructureStorage storage;


    public StructureBuilder() {
        generators = new ArrayList<>();
        schematics = new ArrayList<>();
        storage = new StructureStorageImpl();
    }


    public StructureBuilder(QuarterStructure structure) {
        this.id = structure.getId();
        this.generators = structure.getGenerators();
        this.storage = structure.getStorage();
        this.schematics = structure.getSchematics();
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
        return List.of("id", "addgen", "addschem");
    }

    @Override
    public List<String> complete(CommandSender commandSender, String option) {

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
        }

        return null;
    }

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
}
