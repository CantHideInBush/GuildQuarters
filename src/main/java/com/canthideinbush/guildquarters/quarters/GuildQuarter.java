package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.managers.Keyed;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;
import java.util.UUID;


@SerializableAs("GuildQuarter")
public class GuildQuarter implements Keyed<UUID>, ABSave {



    @YAMLElement
    private UUID guildUUID;

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
    }

    public GuildQuarter(Chunk chunk, Guild guild) {
        this.guildUUID = guild.getId();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.shortId = guild.getName();
        setDebugGlass();
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


}
