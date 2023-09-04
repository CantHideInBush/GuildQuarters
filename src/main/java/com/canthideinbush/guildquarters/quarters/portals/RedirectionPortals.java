package com.canthideinbush.guildquarters.quarters.portals;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedirectionPortals {

    private static ArrayList<RedirectionPortal> registeredPortals = new ArrayList<>();



    @SuppressWarnings("unchecked")
    public static void load() {
        ((ArrayList<RedirectionPortal>) GuildQ.getInstance().getQuartersStorage().get("RedirectionPortals", new ArrayList<>())).forEach(RedirectionPortals::register);
    }

    public static void save() {
        GuildQ.getInstance().getQuartersStorage().set("RedirectionPortals", registeredPortals);
    }

    public static RedirectionPortal getByName(String name) {
        return registeredPortals.stream().filter(rQ -> rQ.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static void register(RedirectionPortal portal) {
        if (portal == null || registeredPortals.stream().anyMatch(rP -> rP.getName().equalsIgnoreCase(portal.getName()))) return;
        registeredPortals.add(portal);
    }

    public static void unregister(RedirectionPortal portal) {
        registeredPortals.remove(portal);
        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            quarter.getQuarterObjects().removeRPortal(portal);
        }
    }

    public static void unregister(String redirectionPortal) {
        unregister(getByName(redirectionPortal));
    }

    public static RedirectionPortal create(String name) {
        RedirectionPortal portal;
        if ((portal = getByName(name)) == null) return null;
        return portal.clone();
    }


    public static boolean exists(String name) {
        return registeredPortals.stream().anyMatch(rQ -> rQ != null && rQ.getName().equalsIgnoreCase(name));
    }

    public static List<String> getIds() {
        return registeredPortals.stream().map(RedirectionPortal::getName).collect(Collectors.toList());
    }
}
