package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorageImpl;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuarterStructure implements ABSave {

    public QuarterStructure(String id) {
        this.id = id;
    }

    public QuarterStructure(StructureBuilder builder) {
        this.id = builder.getId();
        this.storage = builder.getStorage();
        this.generators = builder.getGenerators();
    }

    public QuarterStructure(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    @YAMLElement
    String id;

    public String getId() {
        return id;
    }

    @YAMLElement
    private List<ItemGenerator> generators = new ArrayList<>();

    @YAMLElement
    private StructureStorage storage = new StructureStorageImpl();


    public List<ItemGenerator> getGenerators() {
        return generators;
    }

    public StructureStorage getStorage() {
        return storage;
    }

    @SuppressWarnings("all")
    @Override
    protected Object clone() {
        QuarterStructure clone = new QuarterStructure(this.id);
        clone.generators = this.generators;
        clone.storage = this.storage;
        return clone;
    }
}
