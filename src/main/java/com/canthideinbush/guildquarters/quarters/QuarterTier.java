package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;


@SerializableAs("QuarterTier")
public class QuarterTier implements ABSave, Keyed<Object> {

    public QuarterTier(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterTier(int index) {
        this.index = index;
    }

    @YAMLElement
    private int index;


    public int getIndex() {
        return index;
    }

    @Override
    public Object getKey() {
        return getIndex();
    }


    @Override
    public String toString() {
        return "QuarterTier{" +
                "index=" + index +
                '}';
    }
}
