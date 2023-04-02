package com.canthideinbush.guildquarters.quarters;


import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.glaremasters.guilds.guild.GuildMember;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

@SerializableAs("QuarterRegion")
public class QuarterRegion implements ABSave {


    private GuildQuarter quarter;

    private String regionId;
    private ProtectedRegion region;

    public static int DEFAULT_XZ_SIZE;
    public static int DEFAULT_Y_UP;
    public static int DEFAULT_Y_DOWN;

    @YAMLElement
    private int xzExpansion;

    @YAMLElement
    private int upExpansion;

    @YAMLElement
    private int downExpansion;


    public QuarterRegion(Map<String, Object> map) {
        deserializeFromMap(map);
        updateMembers();
    }
    public QuarterRegion(GuildQuarter quarter) {
        this.quarter = quarter;
        this.regionId = quarter.getShortId() + "_quarter_region";
        if (exists()) {
            this.region = WorldGuard.getInstance().getPlatform().getRegionContainer()
                    .get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).getRegion(regionId);
        }
        else {
            this.region = create();
            setPermissions();
        }



        ConfigurationSection config = GuildQ.getInstance().getConfig().getConfigurationSection("Quarters.region");
        if (config == null) {
            config = GuildQ.getInstance().getConfig().createSection("Quarters.region");
        }
        DEFAULT_XZ_SIZE = config.getInt("xz-size", 20);
        DEFAULT_Y_UP = config.getInt("y-up",20);
        DEFAULT_Y_DOWN = config.getInt("y-down", 10);
        updateMembers();
    }

    public void updateMembers() {
        if (quarter.getGuild() == null) return;
        Collection<UUID> memberList = quarter.getGuild().getMembers().stream().map(GuildMember::getUuid).collect(Collectors.toList());
        region.getMembers().getUniqueIds().forEach(uuid -> {
            if (!memberList.contains(uuid)) {
                region.getMembers().removePlayer(uuid);
            }
        });
        memberList.forEach(uuid -> region.getMembers().addPlayer(uuid));
    }





    public ProtectedRegion create() {
        ProtectedRegion region = new ProtectedCuboidRegion(
                regionId, BukkitAdapter.adapt(
                quarter.getInitialLocation().add(new Vector(-DEFAULT_XZ_SIZE, -DEFAULT_Y_DOWN, -DEFAULT_XZ_SIZE))
        ).toVector().toBlockPoint()
                , BukkitAdapter.adapt(
                        quarter.getInitialLocation().add(DEFAULT_XZ_SIZE, DEFAULT_Y_UP, DEFAULT_XZ_SIZE))
                .toVector().toBlockPoint());
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).addRegion(region);
        return region;
    }

    public void setPermissions() {
        region.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
    }

    public boolean exists() {
        return WorldGuardUtils.getRegionsInLocation(quarter.getInitialLocation())
                .stream().map(ProtectedRegion::getId)
                .anyMatch(s -> s.equalsIgnoreCase(regionId));
    }

    public int getXzExpansion() {
        return xzExpansion;
    }

    public int getUpExpansion() {
        return upExpansion;
    }

    public int getDownExpansion() {
        return downExpansion;
    }

    public void expandXZ(int distance) {
        xzExpansion += distance;
        try {
            ((Region) region).expand(BlockVector3.at(distance, 0, distance),
                    BlockVector3.at(-distance, 0, -distance));
        } catch (RegionOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void expandUp(int distance) {
        upExpansion += distance;
        try {
            ((Region) region).expand(BlockVector3.at(0, distance, 0));
        } catch (RegionOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void expandDown(int distance) {
        downExpansion += distance;
        try {
            ((Region) region).expand(BlockVector3.at(0, -distance, 0));
        } catch (RegionOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void contractXZ(int distance) {
        xzExpansion -= distance;
        try {
            ((Region) region).contract(BlockVector3.at(distance, 0, distance),
                    BlockVector3.at(-distance, 0, -distance));
        } catch (RegionOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void contractUp(int distance) {
        upExpansion -= distance;
        try {
            ((Region) region).contract(BlockVector3.at(0, distance, 0));
        } catch (RegionOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void contractDown(int distance) {
        downExpansion -= distance;
        try {
            ((Region) region).contract(BlockVector3.at(0, -distance, 0));
        } catch (RegionOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove() {
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).removeRegion(this.regionId);
    }

}
