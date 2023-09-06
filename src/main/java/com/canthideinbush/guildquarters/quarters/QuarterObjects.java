package com.canthideinbush.guildquarters.quarters;


import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortal;
import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortals;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawner;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A class containing all kinds of modifiable objects for quarter
 */
public class QuarterObjects implements ABSave {

    public QuarterObjects(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public QuarterObjects() {}

    private GuildQuarter quarter;

    public void initialize(GuildQuarter quarter) {
        this.quarter = quarter;
        quarterStructures.forEach(qS -> qS.initialize(quarter));
        if (GuildQ.isCMIEnabled()) redirectionPortals.forEach(rP -> {
            if (rP != null) rP.initialize(quarter);
        });
        for (RedirectionPortal portal : RedirectionPortals.getObjects()) {
            if (portal.isDefault() && !this.hasRPortal(portal)) {
                addRPortal(portal);
            }
        }
    }

    @YAMLElement
    private List<String> quarterSchematics = new ArrayList<>();

    @YAMLElement
    private List<QuarterStructure> quarterStructures = new ArrayList<>();

    @YAMLElement
    private List<String> mythicSpawners = new ArrayList<>();

    @YAMLElement
    private List<RedirectionPortal> redirectionPortals = new ArrayList<>();

    @YAMLElement
    private HashMap<String, List<QuarterObject>> quarterObjects = new HashMap<>();



    public List<QuarterObject> getQuarterObjects(String category) {
        if (!quarterObjects.containsKey(category)) return Collections.emptyList();
        return quarterObjects.get(category);
    }

    public void addQuarterObject(QuarterObject object) {
        List<QuarterObject> objects;
        if (!quarterObjects.containsKey(object.getCategory())) {
            objects = new ArrayList<>();
            quarterObjects.put(object.getCategory(), objects);
        }
        else objects = quarterObjects.get(object.getCategory());
        if (!isPresent(object, objects)) {
            objects.add(object);
            object.place(quarter);
        }
    }

    public QuarterObject findByName(String category, String id) {
        if (quarterObjects.containsKey(category)) {
            return quarterObjects.get(category).stream().filter(qO -> qO.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
        }
        return null;
    }


    public void removeQuarterObject(String category, String id) {
        QuarterObject quarterObject = findByName(category, id);
        if (quarterObject != null) {
            removeQuarterObject(quarterObject);
        }
    }

    public void removeQuarterObject(QuarterObject object) {
        getQuarterObjects(object.getCategory()).remove(object);
        object.remove(quarter);
    }

    public boolean isPresent(QuarterObject object, List<QuarterObject> objects) {
        return objects.stream().anyMatch(obj -> obj.getId().equalsIgnoreCase(object.getId()));
    }



    public List<QuarterStructure> getStructures() {
        return quarterStructures;
    }

    public List<String> getStructureIds() {
        return quarterStructures.stream().map(QuarterStructure::getId).collect(Collectors.toList());
    }




    public boolean hasStructure(String id) {
        return quarterStructures.stream().anyMatch(s -> s.getId().equalsIgnoreCase(id));
    }

    public QuarterStructure getStructure(String id) {
        return quarterStructures.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findAny().orElse(null);
    }

    public boolean hasSchematic(String name) {
        return quarterSchematics.stream().anyMatch(s -> s.equalsIgnoreCase(name));
    }




    public void placeStructure(QuarterStructure structure) {
        if (!quarter.isPasted()) {
            quarter.getQueuedActions().add(() -> placeStructure(structure));
            return;
        }
        quarterStructures.add(structure);
        for (String schem : structure.getSchematics()) {
            placeSchematic(schem);
        }
        structure.initialize(quarter);
    }

    public void removeStructure(QuarterStructure structure) {
        quarterStructures.remove(structure);
        for (String schem : structure.getSchematics()) {
            removeSchematic(schem);
        }
    }

    public void removeStructure(String structureId) {
        quarterStructures.removeIf(q -> {

            if  (q.getId().equalsIgnoreCase(structureId)) {
                for (String schem : q.getSchematics()) {
                    removeSchematic(schem);
                }
                return true;
            }
            return false;
        });
    }







    public void placeSchematic(String name) {
        if (!quarter.isPasted()) {
            quarter.getQueuedActions().add(() -> placeSchematic(name));
            return;
        }
        QuarterSchematic schematic;
        if ((schematic = GuildQ.getInstance().getQuarterSchematics().getByName(name)) == null) {
            throw new IllegalArgumentException("Quarter schematic with given name does not exist!");
        }

        placeSchematic(schematic);
    }


    public void placeSchematic(@NotNull QuarterSchematic schematic) {
        if (!quarter.isPasted()) {
            quarter.getQueuedActions().add(() -> placeSchematic(schematic));
            return;
        }
        schematic.paste(quarter);

        quarterSchematics.add(schematic.getName());
    }

    public void removeSchematic(@NotNull QuarterSchematic schematic) {
        if (!quarterSchematics.contains(schematic.getName())) return;
        schematic.undo(quarter);

        quarterSchematics.remove(schematic.getName());
    }

    public void removeSchematic(String name) {
        if (!quarterSchematics.contains(name)) return;

        QuarterSchematic schematic;
        if ((schematic = GuildQ.getInstance().getQuarterSchematics().getByName(name)) == null) {
            throw new IllegalArgumentException("Quarter schematic with given name does not exist!");
        }

        schematic.undo(quarter);

        quarterSchematics.remove(name);
    }


    public void placeSpawner(MMSpawner spawner) {
        mythicSpawners.add(spawner.getId());
        spawner.place(quarter);
    }

    public void removeSpawner(MMSpawner spawner) {
        mythicSpawners.remove(spawner.getId());
        spawner.remove(quarter);
    }

    public void removeSpawner(String spawner) {
        mythicSpawners.remove(spawner);
        MMSpawner mmSpawner;
        if ((mmSpawner = MMSpawner.findByName(spawner)) != null) {
            mmSpawner.remove(quarter);
        }
    }

    public void addRPortal(RedirectionPortal portal) {
        if (portal == null || redirectionPortals.stream().anyMatch(rP -> rP != null && rP.getName().equalsIgnoreCase(portal.getName()))) return;
        redirectionPortals.add(portal);
        portal.initialize(quarter);
    }

    public void removeRPortal(RedirectionPortal portal) {
        portal.remove();
        redirectionPortals.remove(portal);
    }

    public void removeRPortal(String name) {
        Optional<RedirectionPortal> optional = redirectionPortals.stream().filter(rP -> rP.getName().equalsIgnoreCase(name)).findAny();
        optional.ifPresent(rP -> {
                rP.remove();
                redirectionPortals.remove(rP);
        });
    }

    public boolean hasRPortal(String name) {
        return redirectionPortals.stream().anyMatch(rP -> rP.getName().equalsIgnoreCase(name));
    }

    public boolean hasRPortal(RedirectionPortal portal) {
        return redirectionPortals.contains(portal);
    }


    int second = 0;
    public void tick() {
        for (QuarterStructure structure : quarterStructures) {
            structure.tickGenerators(second);
        }
        second++;
    }


    public List<String> getSchematics() {
        return new ArrayList<>(quarterSchematics);
    }

}
