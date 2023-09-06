package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.guildquarters.utils.WEQuarterUtils;
import com.canthideinbush.guildquarters.utils.WGQuarterUtils;
import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.SideEffect;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ClickRedirectTrait;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
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

    @YAMLElement
    private int npcId = -1;

    @YAMLElement
    private HashMap<String, Object> tags;

    @YAMLElement
    private ArrayList<String> sharedPermissions = new ArrayList<>();


    //secure variables
    boolean isLoadedFromStorage = false;

    private final List<Runnable> queuedActions = new ArrayList<>();

    public GuildQuarter(Map<String, Object> map) {
        deserializeFromMap(map);
        isLoadedFromStorage = true;
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

        Player master;
        if (getGuild() != null && getGuild().getGuildMaster().isOnline()) {
            master = getGuild().getGuildMaster().getAsPlayer();
        } else {
            master = null;
        }

        if (master != null) {
            GuildQ.getInstance().getUtilsProvider().getChatUtils().sendConfigMessage("common.quarter-pasting-start", master, ChatColor.GREEN);
        }
        WEQuarterUtils.pasteDefaultSchematic(getInitialLocation(), master,  () -> {
            initializeNPC();
            if (master != null) {
                GuildQ.getInstance().getUtilsProvider().getChatUtils().sendConfigMessage("common.quarter-pasting-complete", master, ChatColor.GREEN);
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

    public HashMap<String, Object> getTags() {
        return tags;
    }

    public boolean hasTags() {
        return tags != null;
    }

    public HashMap<String, Object> getOrCreateTags() {
        return hasTags() ? getTags() : (tags = new HashMap<>());
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
            CuboidRegion region = new CuboidRegion(BlockVector3.at((getChunkX() - GuildUtils.getQuarterSize()) << 4, weWorld.getMinY(), (getChunkZ() - GuildUtils.getQuarterSize()) << 4), BlockVector3.at(((getChunkX() + GuildUtils.getQuarterSize()) << 4) + 15, weWorld.getMaxY(), ((getChunkZ() + GuildUtils.getQuarterSize()) << 4) + 15));

            EditSession session = WorldEdit.getInstance().newEditSession(weWorld);
            session.getSideEffectApplier().with(SideEffect.LIGHTING, SideEffect.State.OFF);
            session.getSideEffectApplier().with(SideEffect.UPDATE, SideEffect.State.OFF);
            try {
                session.setBlocks(region, BlockTypes.AIR.getDefaultState());
                Operations.complete(session.commit());
            } catch (
                    WorldEditException e) {
                throw new RuntimeException(e);
            }
            session.close();
            if (r != null)
                Bukkit.getScheduler().runTask(GuildQ.getInstance(), r);
        });
    }

    public void clearTemplateSchematic(Runnable r) {
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> {
            World world = getInitialLocation().getWorld();
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);

            Clipboard clipboard = GuildQ.getInstance().getUtilsProvider().worldEdit.findByName(GuildUtils.getSchematicName());

            EditSession session = WorldEdit.getInstance().newEditSession(weWorld);
            session.getSideEffectApplier().with(SideEffect.LIGHTING, SideEffect.State.OFF);

            Location loc = getInitialLocation();

            CuboidRegion boundingBox = clipboard.getRegion().getBoundingBox();
            BlockVector3 origin = clipboard.getOrigin();

            BlockVector3 newMin = BlockVector3.at(loc.getBlockX() + boundingBox.getMinimumPoint().getBlockX() - origin.getBlockX(),
                    loc.getBlockY() + boundingBox.getMinimumPoint().getBlockY() - origin.getBlockY(),
                    loc.getBlockZ() + boundingBox.getMinimumPoint().getBlockZ() - origin.getBlockZ());

            CuboidRegion newRegion = new CuboidRegion(
                    newMin,
                    newMin.add(BlockVector3.at(clipboard.getRegion().getWidth(), clipboard.getRegion().getHeight(), clipboard.getRegion().getLength())));


            try {
                session.setBlocks(newRegion, BlockTypes.AIR.getDefaultState());
                Operations.complete(session.commit());
            } catch (
                    WorldEditException e) {
                throw new RuntimeException(e);
            }
            session.close();
            if (r != null)
                Bukkit.getScheduler().runTask(GuildQ.getInstance(), r);
        });
    }

    public void reset() {
        if (removed || !pasted) return;
        setQuarterTier(0);
        clearTemplateSchematic(() -> {
            WEQuarterUtils.pasteDefaultSchematic(getInitialLocation(), null, () -> pasted = true);
            GuildQ.getInstance().getLogger().log(Level.INFO, "GuildQuarter " + getShortId() + " reset complete");;
        });
    }


    public void remove() {
        if (removed) return;
        removed = true;
        sharedPermissions.clear();
        try {
            GuildQ.getInstance().getQuartersManager().save();
            if (getGuild() != null) {
                getGuild().getMembers().forEach(
                        (guildMember) -> {
                            if (guildMember.isOnline()) {
                                Player player = guildMember.getAsPlayer();
                                if (WGQuarterUtils.contains(this, player.getLocation()))
                                    player.performCommand("spawn");
                            }
                        }

                );
            }
            clearTemplateSchematic(() -> {
                if (getQuarterNPC() != null)
                    GuildQ.citizens.getNPCRegistry().deregister(getQuarterNPC());
                if (region != null)
                    region.remove();
                GuildQ.getInstance().getQuartersManager().unregister(this);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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




    public void initialize() {
        quarterObjects.initialize(this);
        if (this.region == null) region = new QuarterRegion(this);
        getRegion().initialize();
        if (removed) remove();
    }

    public void initializeNPC() {
        if (!pasted) {
            queuedActions.add(this::initializeNPC);
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



    /*
    Possible scenarios where player's permissions should be synced:
    - Joining guild
    - Leaving guild
    - Adding permissions to guild
    - Removing permissions from guild
    - Joining server
    */
    public ArrayList<String> getSharedPermissions() {
        return sharedPermissions;
    }




    public void syncSharedPermissions() {
        if (getGuild() == null) return;
        List<String> registeredPermissions = GuildQ.getInstance().getGuildPermissions().getRegisteredPermissions();
        getGuild().getMembers().forEach(
                guildMember -> {
                    Player player = guildMember.getAsPlayer();
                    if (player.isOnline()) {
                        for (String permission : registeredPermissions) {
                            if (sharedPermissions.contains(permission)) GuildQ.getInstance().getPermissions().playerAdd(player, permission);
                            else GuildQ.getInstance().getPermissions().playerRemove(player, permission);
                        }
                    }
                }
        );
    }

    public void syncPermissions(Player player) {
        for (String permission : GuildQ.getInstance().getGuildPermissions().getRegisteredPermissions()) {
            if (sharedPermissions.contains(permission)) GuildQ.getInstance().getPermissions().playerAdd(player, permission);
            else GuildQ.getInstance().getPermissions().playerRemove(player, permission);
        }
    }



}
