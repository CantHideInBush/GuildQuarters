package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;


@SerializableAs("GuildQuarter")
public class GuildQuarter implements Keyed<UUID>, ABSave {



    @YAMLElement
    public UUID guildUUID;

    @YAMLElement
    private String shortId;

    @YAMLElement
    private int chunkX;

    @YAMLElement
    private int chunkZ;



    public GuildQuarter(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public GuildQuarter(Chunk chunk, String shortId) {
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = shortId;
        setDebugGlass();
        GuildUtils.pasteGuildSchematic(getInitialLocation());
    }

    public GuildQuarter(Chunk chunk, Guild guild) {
        this.guildUUID = guild.getId();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = guild.getName();
        setDebugGlass();
        GuildUtils.pasteGuildSchematic(getInitialLocation());
    }


    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public String getShortId() {
        return shortId;
    }

    @Override
    public UUID getKey() {
        return guildUUID;
    }



    private void setDebugGlass() {
        GuildUtils.getGuildWorld().getBlockAt(chunkX * 16, 100, chunkZ * 16).setType(Material.GLASS);
    }

    private Location getInitialLocation() {
        return new Location(GuildUtils.getGuildWorld(), chunkX * 16, 100, chunkZ * 16, 0, 0);
    }


    public void clearChunks(Runnable r) {
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> {
            CraftWorld nmsWorld = ((CraftWorld) GuildUtils.getGuildWorld()).getHandle().getWorld();
            int quarterSize = GuildUtils.getQuarterSize();
            int minY = GuildUtils.getGuildWorld().getMinHeight();
            int maxY = GuildUtils.getGuildWorld().getMaxHeight();
            ArrayList<BlockState> toUpdate = new ArrayList<>();
            for (int x = chunkX - quarterSize; x < chunkX + quarterSize; x++) {
                for (int z = chunkZ - quarterSize; z < chunkZ + quarterSize; z++) {
                    CraftChunk chunk = (CraftChunk) nmsWorld.getChunkAt(x, z).getPersistentDataContainer();
                    ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, false, false);
                    for (int bX = 0; bX < 16; bX++) {
                        for (int bZ = 0; bZ < 16; bZ++) {
                            if (snapshot.getHighestBlockYAt(bX, bZ) == minY - 1) continue;
                            for (int y = minY; y < maxY; y++) {
                                chunk.
                                if (!snapshot.getBlockType(bX, y, bZ).isAir()) {
                                    BlockState state = chunk.getBlock(bX, y, bZ).getState(false);
                                    state.setType(Material.AIR);
                                    toUpdate.add(state);
                                }
                            }
                        }

                    }


                }
            }
            Bukkit.getScheduler().runTask(GuildQ.getInstance(), () -> {
                toUpdate.forEach(blockState -> blockState.update(false, false));
                r.run();
            });
        });
    }

    public void reset() {
        clearChunks(() -> {
            GuildUtils.pasteGuildSchematic(getInitialLocation());
            GuildQ.getInstance().getLogger().log(Level.INFO, "GuildQuarter " + getShortId() + " reset complete");;
        });
    }

}
