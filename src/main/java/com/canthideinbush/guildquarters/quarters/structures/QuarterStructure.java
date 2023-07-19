package com.canthideinbush.guildquarters.quarters.structures;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorageImpl;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import me.glaremasters.guilds.Guilds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuarterStructure implements ABSave, Listener {


    /**
     * !!! cloned object
     */

    public QuarterStructure(String id) {
        this.id = id;
    }

    public QuarterStructure(StructureBuilder builder) {
        this.id = builder.getId();
        this.storage = builder.getStorage();
        this.generators = builder.getGenerators();
        this.schematics = builder.getSchematics();
        this.collectionBlock = builder.getCollectionBlock();
    }

    public QuarterStructure(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    @YAMLElement
    String id;

    private GuildQuarter quarter;

    public void initialize(GuildQuarter quarter) {
        this.quarter = quarter;
        Bukkit.getPluginManager().registerEvents(this, GuildQ.getInstance());
    }

    public String getId() {
        return id;
    }

    @YAMLElement
    private List<ItemGenerator> generators = new ArrayList<>();

    @YAMLElement
    private List<String> schematics = new ArrayList<>();

    @YAMLElement
    private StructureStorage storage = new StructureStorageImpl();

    @YAMLElement
    private Vector collectionBlock;


    public List<ItemGenerator> getGenerators() {
        return generators;
    }

    public StructureStorage getStorage() {
        return storage;
    }

    public void setCollectionBlock(Vector collectionBlock) {
        this.collectionBlock = collectionBlock;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (collectionBlock == null) return;
        if (event.getAction().isRightClick() && event.getClickedBlock() != null) {
            if (event.getClickedBlock().equals(quarter.getInitialLocation().add(collectionBlock).getBlock())) {
                if (quarter.getGuild() != null && quarter.getGuild().equals(Guilds.getApi().getGuild(event.getPlayer()))) {

                    for (String itemId : storage.getAvailable()) {
                        int amount = storage.take(itemId, event.getPlayer(), storage.getAmount(itemId));
                        if (amount > 0) {
                            GuildQ.getInstance().getUtilsProvider().getChatUtils().sendConfigMessage("common.item-collected", event.getPlayer(), ChatColor.GREEN, itemId, amount);
                        }
                    }
                }
            }
        }
    }

    public Vector getCollectionBlock() {
        return collectionBlock;
    }

    public void addGenerator(ItemGenerator generator) {
        if (containsGenerator(generator.getId())) return;
        generators.add(generator);
    }

    public void removeGenerator(ItemGenerator generator) {
        generators.remove(generator);
    }

    public void removeGenerator(String id) {
        generators.removeIf(g -> g.getId().equalsIgnoreCase(id));
    }

    public boolean containsGenerator(String id) {
        return generators.stream().anyMatch(generator -> generator.getId().equalsIgnoreCase(id));
    }

    public boolean containsGenerator(ItemGenerator generator) {
        return generators.contains(generator);
    }

    public void addSchematic(String schem) {
        if (schematics.contains(schem)) return;
        schematics.add(schem);

        QuarterSchematic schematic = GuildQ.getInstance().getQuarterSchematics().getByName(schem);
        if (schematic != null) {
            schematic.paste(quarter);
        }

    }
    public void removeSchematic(String schem) {
        schematics.remove(schem);
        QuarterSchematic schematic = GuildQ.getInstance().getQuarterSchematics().getByName(schem);
        if (schematic != null) schematic.undo(quarter);
    }



    public void tickGenerators(int second) {
        for (ItemGenerator generator : generators) {
            if (second % generator.getInterval() == 0) {
                generator.generate();
                storage.store(generator);
            }
        }
        if (collectionBlock != null) {
            if (!storage.isEmpty()) {
                StructureParticles.highlightBlock(quarter.getInitialLocation().add(collectionBlock).getBlock());
            }
        }
    }


    @SuppressWarnings("all")
    @Override
    public QuarterStructure clone() {
        QuarterStructure clone = new QuarterStructure(this.id);
        clone.generators = this.generators;
        clone.storage = this.storage;
        clone.schematics = this.schematics;
        clone.collectionBlock = this.collectionBlock;
        return clone;
    }



    public List<String> getSchematics() {
        return schematics;
    }

    public void setStorage(StructureStorage storage) {
        this.storage = storage;
    }
}
