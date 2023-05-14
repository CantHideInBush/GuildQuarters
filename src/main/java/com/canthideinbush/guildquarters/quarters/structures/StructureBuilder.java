package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorageImpl;

import java.util.ArrayList;
import java.util.List;

public class StructureBuilder {



    private String id;
    private List<ItemGenerator> generators;
    private StructureStorage storage;

    public StructureBuilder(String id) {
        this.id = id;
        generators = new ArrayList<>();
        storage = new StructureStorageImpl();
    }

    public StructureBuilder(QuarterStructure structure) {
        this.id = structure.getId();
        this.generators = structure.getGenerators();
        this.storage = structure.getStorage();
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
}
