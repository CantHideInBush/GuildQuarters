package com.canthideinbush.guildquarters.quarters.spawners;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MMSpawner implements ABSave {

    public MMSpawner(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public MMSpawner(String id, String mythicId, Vector offset) {
        this.id = id;
        this.mythicId = mythicId;
        this.offset = offset;
    }

    public MMSpawner(MMSpawnerBuilder builder) {
        this.id = builder.id;
        this.mythicId = builder.mythicId;
        this.offset = builder.offset;
    }

    private static List<MMSpawner> registered;

    @YAMLElement
    private String id;

    public static List<String> getIds() {
        return registered.stream().map(MMSpawner::getId).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    @YAMLElement
    private String mythicId;

    @YAMLElement
    private Vector offset;

    public String getSpawnerName(GuildQuarter quarter) {
        return "guildq-"  + quarter.getShortId() + "-" + mythicId;
    }

    public MythicSpawner getSpawner(GuildQuarter quarter) {
        return MythicBukkit.inst().getSpawnerManager().getSpawnerByName(getSpawnerName(quarter));
    }

    public boolean contains(GuildQuarter quarter) {
        return getSpawner(quarter) != null;
    }

    public static MMSpawner findByName(String id) {
        return registered.stream().filter(m -> m.id.equalsIgnoreCase(id)).findAny().orElse(null);
    }

    public static void register(MMSpawner spawner) {
        if (findByName(spawner.id) != null) {
            return;
        }
        registered.add(spawner);
    }

    public static void unregister(MMSpawner spawner) {
        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            quarter.getQuarterObjects().removeSpawner(spawner);
        }
        registered.remove(spawner);
    }


    public void place(GuildQuarter quarter) {
        if (getSpawner(quarter) != null) {
            return;
        }
        Location location = quarter.getInitialLocation().add(offset);
        AbstractLocation abstractLocation = new AbstractLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        MythicBukkit.inst().getSpawnerManager().copySpawner(mythicId, getSpawnerName(quarter), abstractLocation);
    }

    public void remove(GuildQuarter quarter) {
        MythicSpawner spawner;
        if ((spawner = getSpawner(quarter)) != null) {
            MythicBukkit.inst().getSpawnerManager().removeSpawner(spawner);
        }
    }

    public static void load() {
        registered = (List<MMSpawner>) GuildQ.getInstance().getQuartersStorage().getList("MMSpawners", new ArrayList<>());
    }

    public static void save() {
        GuildQ.getInstance().getQuartersStorage().set("MMSpawners", registered);
    }



}
