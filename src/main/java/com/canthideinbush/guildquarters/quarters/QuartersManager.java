package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.http.integration.HttpsPluginIntegration;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.managers.KeyedStorage;
import com.canthideinbush.utils.storing.YAMLConfig;
import com.canthideinbush.utils.worldedit.ReferenceTable;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class QuartersManager implements KeyedStorage<GuildQuarter> {



    private final ArrayList<GuildQuarter> quarters = new ArrayList<>();


    public static GuildQuarter templateQuarter;

    private boolean initialized = false;

    public ReferenceTable referenceTable = null;

    public boolean isInitialized() {
        return initialized;
    }

    public QuartersManager() {
        GuildQ.getInstance().setQuartersManager(this);
        if (GuildQ.getInstance().getUtilsProvider()
                .worldEdit.findByName(GuildUtils.getSchematicName()) == null) {
            GuildQ.getInstance().getLogger().log(Level.WARNING,
                    "Default schematic file not found! Disabling quarters manager! Please create " + GuildUtils.getSchematicName() + " schematic and issue '/gq initialize' command");
        }
        else {
            initialize();
            initialized = true;
            GuildQ.getInstance().getLogger().log(Level.INFO, "Successfully loaded quarters manager!");
        }


    }

    private BukkitTask quartersLoop;

    public boolean initialize() {
        if (initialized || GuildQ.getInstance().getUtilsProvider()
                .worldEdit.findByName(GuildUtils.getSchematicName()) == null) return false;
        load();
        createNamedQuarter("TemplateQuarter");
        templateQuarter = getByShortId("TemplateQuarter");

        quarters.forEach(GuildQuarter::initialize);

        quartersLoop = Bukkit.getScheduler().runTaskTimerAsynchronously(GuildQ.getInstance(),
                () -> {
            for (int i = 0; i < quarters.size(); i++) {
                GuildQuarter quarter = quarters.get(i);
                quarter.tick();
            }
                }, 0, 20);
        HttpsPluginIntegration.integrateQuarters(this);
        HttpsPluginIntegration.integrateItemGenerators();
        return true;
    }





    public boolean createNamedQuarter(String shortId) {
        if (getByShortId(shortId) != null) return false;
        register(new GuildQuarter(getEmptyChunk(), shortId));
        return true;
    }

    public boolean createNamedQuarterAsync(String shortId) {
        if (getByShortId(shortId) != null) return false;
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> register(new GuildQuarter(getEmptyChunk(), shortId)));
        return true;
    }

    public boolean createGuildQuarter(Guild guild) {
        if (getByGuildId(guild.getId()) != null) return false;
        register(new GuildQuarter(getEmptyChunk(), guild));
        return true;
    }

    public boolean createGuildQuarterAsync(Guild guild) {
        if (getByGuildId(guild.getId()) != null) return false;
        Bukkit.getScheduler().runTaskAsynchronously(GuildQ.getInstance(), () -> register(new GuildQuarter(getEmptyChunk(), guild)));
        return true;
    }

    @Override
    public List<GuildQuarter> getObjects() {
        return quarters;
    }

    public GuildQuarter getByChunk(int x, int z) {
        return quarters.stream().filter(q -> q.getChunkX() == x && q.getChunkZ() == z).findAny().orElse(null);
    }

    public GuildQuarter getByShortId(String shortId){
        return quarters.stream().filter(q -> q.getShortId().equalsIgnoreCase(shortId)).findAny().orElse(null);
    }
    public GuildQuarter getByGuildId(@NotNull UUID uuid){
        return quarters.stream().filter(q -> uuid.equals(q.guildUUID)).findAny().orElse(null);
    }


    public GuildQuarter getByMember(Player member){
        if (member == null) return null;
        return quarters.stream().filter(q -> q.getGuild() != null && Guilds.getApi().getGuild(member).equals(q.getGuild())).findAny().orElse(null);
    }


    @NotNull
    private Chunk getEmptyChunk() {
        for (int x = 0; x < GuildUtils.getGuildWorld().getWorldBorder().getSize(); x+= GuildUtils.getQuarterSize() * 2) {
            for (int z = 0; z < GuildUtils.getGuildWorld().getWorldBorder().getSize(); z+= GuildUtils.getQuarterSize() * 2) {
                if (getByChunk(x, z) == null) return GuildUtils.getGuildWorld().getChunkAt(x, z);
            }
        }
        throw new IllegalStateException("No more space left on guild world");
    }

    public List<String> getShortIds() {
        return quarters.stream().map(GuildQuarter::getShortId).filter(Objects::nonNull).collect(Collectors.toList());
    }



    private int proxyNPCId = -1;

    public int getProxyNPCId() {
        return proxyNPCId;
    }

    public void setProxyNPCId(int proxyNPCId) {
        this.proxyNPCId = proxyNPCId;
    }

    public Location proxyNPCLocation = new Location(GuildUtils.getGuildWorld(), 0, 0,0);

    public void setProxyNPCLocation(Location proxyNPCLocation) {
        this.proxyNPCLocation = proxyNPCLocation;
    }

    public Location getProxyNPCLocation() {
        return proxyNPCLocation;
    }



    public void save() {
        YAMLConfig config = GuildQ.getInstance().getQuartersStorage();
        config.set("Quarters", getObjects());
        config.set("ProxyNPCLocation", proxyNPCLocation);
        config.set("ProxyNPCId", proxyNPCId);
        config.set("ReferenceTable", referenceTable);
    }

    public void load() {
        YAMLConfig config = GuildQ.getInstance().getQuartersStorage();
        if (config.contains("Quarters")) this.quarters.addAll((Collection<? extends GuildQuarter>) config.getList("Quarters"));
        proxyNPCLocation = config.getLocation("ProxyNPCLocation");
        proxyNPCId = config.getInt("ProxyNPCId");
        referenceTable = (ReferenceTable) config.get("ReferenceTable", null);
    }


    public NPC getProxyNPC() {
        if (proxyNPCId == -1 || GuildQ.citizens == null) return null;
        return GuildQ.citizens.getNPCRegistry().getById(proxyNPCId);
    }
}
