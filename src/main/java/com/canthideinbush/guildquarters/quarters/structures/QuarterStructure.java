package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorageImpl;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;

import java.util.ArrayList;
import java.util.Map;

public class QuarterStructure implements ABSave {

    public QuarterStructure(String id) {
        this.id = id;
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
    private ArrayList<ItemGenerator> generators = new ArrayList<>();

    @YAMLElement
    private StructureStorage storage = new StructureStorageImpl();

    @SuppressWarnings("all")
    @Override
    protected Object clone() {
        QuarterStructure clone = new QuarterStructure(this.id);
        clone.generators = this.generators;
        clone.storage = this.storage;
        return clone;
    }
}
