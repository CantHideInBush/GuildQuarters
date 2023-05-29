package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorageImpl;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.utils.ObjectBuilder;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StructureBuilder implements ObjectBuilder<QuarterStructure> {



    private String id;
    private List<ItemGenerator> generators;
    private List<String> schematics;
    private StructureStorage storage;

    public StructureBuilder(String id) {
        this.id = id;
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
    public List<String> complete(CommandSender commandSender, String s) {
        return null;
    }

    @Override
    public String errorFor(String s, String s1) {
        return null;
    }

    @Override
    public void with(String s, String s1) {

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
