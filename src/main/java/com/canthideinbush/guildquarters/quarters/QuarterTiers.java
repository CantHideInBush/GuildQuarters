package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.managers.KeyedStorage;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;



public class QuarterTiers implements KeyedStorage<QuarterTier> {
    public static final QuarterTiers instance = new QuarterTiers();

    private static ArrayList<QuarterTier> tiers;

    @Override
    public Collection<QuarterTier> getObjects() {
        return tiers;
    }


    public static QuarterTier get(int tier) {
        QuarterTier quarterTier;
        if ((quarterTier = instance.findByKey(tier)) == null) instance.register(quarterTier = new QuarterTier(tier));
        return quarterTier;
    }

    public static boolean exists(int tier) {
        return tier < 0 || instance.findByKey(tier) != null;
    }

    public static void load() {
        tiers = (ArrayList<QuarterTier>) GuildQ.getInstance().getQuartersStorage().get("Tiers", new ArrayList<>());
    }

    public static void save() {
        GuildQ.getInstance().getQuartersStorage().set("Tiers", tiers);
    }


    /*
    Adds new schematic to this tier, pasted when quarter is upgraded

     */
    public static void addStructure(int tier, String schematic, Vector vector) {
        QuarterTier quarterTier = get(tier);
        quarterTier.addStructure(schematic, vector);

    }


    public static void removeStructure(int tier, String schematic) {
        QuarterTier quarterTier;
        if ((quarterTier = get(tier)) == null) return;
        quarterTier.removeStructure(schematic);

    }

}
