package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.managers.KeyedStorage;
import com.canthideinbush.utils.storing.YAMLConfig;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuartersManager implements KeyedStorage<GuildQuarter> {



    private final ArrayList<GuildQuarter> quarters = new ArrayList<>();



    public boolean createNamedQuarter(String shortId) {
        if (getByShortId(shortId) != null) return false;
        register(new GuildQuarter(getEmptyChunk(), shortId));
        return true;
    }
    public boolean createGuildQuarter(Guild guild) {
        if (findByKey(guild.getId()) != null) return false;
        register(new GuildQuarter(getEmptyChunk(), guild));
        return true;
    }

    @Override
    public Collection<GuildQuarter> getObjects() {
        return quarters;
    }

    public GuildQuarter getByChunk(int x, int z) {
        return quarters.stream().filter(q -> q.getChunkX() == x && q.getChunkZ() == z).findAny().orElse(null);
    }

    public GuildQuarter getByShortId(String shortId){
        return quarters.stream().filter(q -> q.getShortId().equalsIgnoreCase(shortId)).findAny().orElse(null);
    }




    @NotNull
    private Chunk getEmptyChunk() {
        for (int x = 0; x < GuildUtils.getGuildWorld().getWorldBorder().getSize(); x+= GuildUtils.getQuarterSize()) {
            for (int z = 0; z < GuildUtils.getGuildWorld().getWorldBorder().getSize(); z+= GuildUtils.getQuarterSize()) {
                if (getByChunk(x, z) == null) return GuildUtils.getGuildWorld().getChunkAt(x, z);
            }
        }
        throw new IllegalStateException("No more space left on guild world");
    }

    public List<String> getShortIds() {
        return quarters.stream().map(GuildQuarter::getShortId).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public void save() {
        YAMLConfig config = GuildQ.getInstance().getQuartersStorage();
        config.set("Quarters", getObjects());
    }

    public void load() {
        YAMLConfig config = GuildQ.getInstance().getQuartersStorage();
        if (config.contains("Quarters")) this.quarters.addAll((Collection<? extends GuildQuarter>) config.getList("Quarters"));
    }


}
