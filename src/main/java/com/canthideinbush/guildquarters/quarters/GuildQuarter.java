package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.SideEffect;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ClickRedirectTrait;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
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
    private QuarterObjects quarterObjects;

    @YAMLElement
    private QuarterRegion region;

    @YAMLElement
    private Location spawnLocation;

    @YAMLElement
    private boolean removed = false;

    @YAMLElement
    private boolean pasted = false;


    private List<Runnable> queuedActions = new ArrayList<>();

    public GuildQuarter(Map<String, Object> map) {
        deserializeFromMap(map);
    }

    public GuildQuarter(Chunk chunk, String shortId) {
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = shortId;
        pasteSchem();
        region = new QuarterRegion(this);
        spawnLocation = getInitialLocation().add(GuildUtils.getSpawnOffset());
        quarterObjects = new QuarterObjects();
        initialize();
    }

    public GuildQuarter(Chunk chunk, Guild guild) {
        this.guildUUID = guild.getId();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = guild.getName();
        pasteSchem();
        region = new QuarterRegion(this);
        spawnLocation = getInitialLocation().add(GuildUtils.getSpawnOffset());
        quarterObjects = new QuarterObjects();
        initialize();
    }

    private void pasteSchem() {
        if (getGuild().getGuildMaster().isOnline()) {
            GuildQ.getInstance().getUtilsProvider().getChatUtils().sendConfigMessage("common.quarter-pasting-start", getGuild().getGuildMaster().getAsPlayer(), ChatColor.GREEN);
        }
        GuildUtils.pasteGuildSchematic(getInitialLocation(), () -> {
            initializeNPC();
            if (getGuild().getGuildMaster().isOnline()) {
                GuildQ.getInstance().getUtilsProvider().getChatUtils().sendConfigMessage("common.quarter-pasting-complete", getGuild().getGuildMaster().getAsPlayer(), ChatColor.GREEN);
            }
            pasted = true;
            for (Runnable r : queuedActions) {
                r.run();
            }
            queuedActions.clear();
        });
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
        if (quarterTier < this.quarterTier) {
            for (int i = this.quarterTier; i > quarterTier; i--) {
                this.quarterTier = i;
            }
        }
        else if (quarterTier > this.quarterTier) {
            for (int i = this.quarterTier; i < quarterTier; i++) {
                this.quarterTier = i;
            }
        }
    }

    public void clearChunks(Runnable r) {
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> {
            World world = getInitialLocation().getWorld();
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(world)).maxBlocks(-1).build();
            editSession.getSideEffectApplier().with(SideEffect.LIGHTING, SideEffect.State.OFF);

            CuboidRegion region = new CuboidRegion(BlockVector3.at((getChunkX() - GuildUtils.getQuarterSize()) << 4, weWorld.getMinY(), (getChunkZ() - GuildUtils.getQuarterSize()) << 4), BlockVector3.at(((getChunkX() + GuildUtils.getQuarterSize()) << 4) + 15, weWorld.getMaxY(), ((getChunkZ() + GuildUtils.getQuarterSize()) << 4) + 15));
            try {
                editSession.setBlocks(region, BlockTypes.AIR.getDefaultState().toBaseBlock());
            } catch (
                    MaxChangedBlocksException e) {
                throw new RuntimeException(e);
            }
            editSession.close();
            if (r != null)
                Bukkit.getScheduler().runTask(GuildQ.getInstance(), r);
        });
    }

    public void reset() {
        if (removed || !pasted) return;
        setQuarterTier(0);
        clearChunks(() -> {
            GuildUtils.pasteGuildSchematic(getInitialLocation(), () -> pasted = true);
            GuildQ.getInstance().getLogger().log(Level.INFO, "GuildQuarter " + getShortId() + " reset complete");;
        });
    }


    public void remove() {
        removed = true;
        GuildQ.getInstance().getQuartersManager().save();
        if (getGuild() != null) {
            getGuild().getMembers().forEach(
                    (guildMember) -> {
                        if (guildMember.isOnline()) {
                            Player player = guildMember.getAsPlayer();
                            if (GuildUtils.contains(this, player.getLocation())) player.performCommand("spawn");
                        }
                    }

            );
        }
        clearChunks(() -> {
            if (getQuarterNPC() != null) GuildQ.citizens.getNPCRegistry().deregister(getQuarterNPC());
            region.remove();
            GuildQ.getInstance().getQuartersManager().unregister(this);
        });

    }



    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public String toString() {
        return "GuildQuarter{" +
                "guildUUID=" + guildUUID +
                ", shortId='" + shortId + '\'' +
                ", chunkX=" + chunkX +
                ", chunkZ=" + chunkZ +
                ", quarterTier=" + quarterTier +
                ", region=" + region +
                ", spawnLocation=" + spawnLocation +
                '}';
    }

    public QuarterRegion getRegion() {
        return region;
    }


    @YAMLElement
    private int npcId = -1;

    public void initialize() {
        quarterObjects.initialize(this);
        getRegion().updateMembers();
        if (removed) remove();
    }

    public void initializeNPC() {
        if (!pasted) {
            queuedActions.add(() -> initializeNPC());
            return;
        }

        if (this.equals(QuartersManager.templateQuarter)) return;

        NPC quarterNPC;
        NPC proxyNPC = GuildQ.getInstance().getQuartersManager().getProxyNPC();
        if (npcId == -1 || getQuarterNPC() == null) {
            if (GuildQ.getInstance().getQuartersManager().getProxyNPC() == null) return;
            quarterNPC = proxyNPC.copy();
            npcId = quarterNPC.getId();
        }
        else quarterNPC = GuildQ.citizens.getNPCRegistry().getById(npcId);
        quarterNPC.removeTrait(ClickRedirectTrait.class);
        quarterNPC.addTrait(new ClickRedirectTrait(proxyNPC));
        Location proxyLocation = GuildQ.getInstance().getQuartersManager().getProxyNPCLocation();
        Location loc = getInitialLocation().add(proxyLocation);
        loc.setPitch(proxyLocation.getPitch());
        loc.setYaw(proxyLocation.getYaw());
        if (!quarterNPC.isSpawned()) quarterNPC.spawn(loc);
        else quarterNPC.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private NPC getQuarterNPC() {
        if (npcId == -1) return null;
        return CitizensAPI.getNPCRegistry().getById(npcId);
    }

    public QuarterObjects getQuarterObjects() {
        return quarterObjects;
    }

    public void tick() {
        quarterObjects.tick();
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean isPasted() {
        return pasted;
    }

    public List<Runnable> getQueuedActions() {
        return queuedActions;
    }
}
