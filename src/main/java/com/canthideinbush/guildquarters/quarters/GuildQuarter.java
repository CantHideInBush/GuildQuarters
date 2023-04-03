package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLConfig;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldguard.WorldGuard;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import java.util.ArrayList;
import java.util.HashMap;
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

    @YAMLElement
    private int quarterTier = 0;

    @YAMLElement
    private QuarterRegion region;

    @YAMLElement
    private Location spawnLocation;

    public GuildQuarter(Map<String, Object> map) {
        deserializeFromMap(map);
        region = new QuarterRegion(this);
    }

    public GuildQuarter(Chunk chunk, String shortId) {
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = shortId;
        setDebugGlass();
        GuildUtils.pasteGuildSchematic(getInitialLocation());
        getTier().apply(this);
        region = new QuarterRegion(this);
        spawnLocation = getInitialLocation();
    }

    public GuildQuarter(Chunk chunk, Guild guild) {
        this.guildUUID = guild.getId();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = guild.getName();
        setDebugGlass();
        GuildUtils.pasteGuildSchematic(getInitialLocation());
        getTier().apply(this);
        region = new QuarterRegion(this);
        spawnLocation = getInitialLocation();
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

    public Location getInitialLocation() {
        return new Location(GuildUtils.getGuildWorld(), chunkX * 16, 100, chunkZ * 16, 0, 0);
    }

    public QuarterTier getTier() {
        return QuarterTiers.get(quarterTier);
    }


    public Guild getGuild() {
        if (guildUUID == null) return null;
        return Guilds.getApi().getGuild(guildUUID);
    }


    public void setQuarterTier(int quarterTier) {
        if (quarterTier < 0) quarterTier = 0;
        if (quarterTier < this.quarterTier) getTier().undo(this);
        this.quarterTier = quarterTier;
        getTier().apply(this);
    }

    public void clearChunks(Runnable r) {
        ArrayList<BlockPos> toClear = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> {
            CraftWorld nmsWorld = ((CraftWorld) GuildUtils.getGuildWorld()).getHandle().getWorld();
            int quarterSize = GuildUtils.getQuarterSize();
            int minY = GuildUtils.getGuildWorld().getMinHeight();
            int maxY = GuildUtils.getGuildWorld().getMaxHeight();
            for (int x = chunkX - quarterSize; x < chunkX + quarterSize; x++) {
                for (int z = chunkZ - quarterSize; z < chunkZ + quarterSize; z++) {
                    CraftChunk chunk = (CraftChunk) nmsWorld.getChunkAt(x, z);
                    ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, false, false);
                    for (int bX = 0; bX < 16; bX++) {
                        for (int bZ = 0; bZ < 16; bZ++) {
                            if (snapshot.getHighestBlockYAt(bX, bZ) != minY - 1) {
                                for (int y = minY; y < maxY; y++) {
                                    if (!snapshot.getBlockType(bX, y, bZ).isAir()) {
                                        toClear.add(new BlockPos(x * 16 + bX, y, z * 16 + bZ));
                                    }
                                }
                            }
                        }

                    }


                }
            }
            Bukkit.getScheduler().runTask(GuildQ.getInstance(), () -> {
                toClear.forEach((position) ->((CraftChunk) nmsWorld.getChunkAt(position.getX() >> 4, position.getZ() >> 4)).getHandle().setBlockState(position, Blocks.AIR.defaultBlockState(), false));
                if (r != null) r.run();
            });
        });
    }

    public void reset() {
        clearChunks(() -> {
            GuildUtils.pasteGuildSchematic(getInitialLocation());
            GuildQ.getInstance().getLogger().log(Level.INFO, "GuildQuarter " + getShortId() + " reset complete");;
        });
    }

    public boolean upgrade() {
        if (QuarterTiers.get(quarterTier + 1) != null) {
            quarterTier++;
            getTier().apply(this);
            return true;
        }
        else return false;
    }

    public boolean downgrade() {
        if (quarterTier == 0) return false;
        getTier().undo(this);
        quarterTier--;
        return true;
    }

    public void remove() {
        clearChunks(() -> {
            region.remove();
            GuildQ.getInstance().getQuartersManager().unregister(this);
            if (getGuild() != null) {
                getGuild().getMembers().forEach(
                        guildMember -> guildMember.getAsPlayer().performCommand("/spawn")
                );
            }
        });

    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
