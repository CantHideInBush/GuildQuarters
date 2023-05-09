package com.canthideinbush.guildquarters.quarters;


import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.glaremasters.guilds.guild.GuildMember;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SerializableAs("QuarterRegion")
public class QuarterRegion implements ABSave {



    public enum Direction  {
        UP,DOWN,HORIZON;
        public static List<String> values = Stream.of(values())
                .map(Enum::name).collect(Collectors.toList());
    }

    @YAMLElement
    private String quarterId;
    private GuildQuarter quarter;

    private GuildQuarter quarter() {
        if (quarter != null) return quarter;
        else return quarter = GuildQ.getInstance().getQuartersManager().getByShortId(quarterId);
    }

    @YAMLElement
    private String regionId;
    private ProtectedCuboidRegion region() {
        return (ProtectedCuboidRegion) WorldGuardUtils.getRegion(GuildUtils.getGuildWorld(), regionId);
    }

    public static int DEFAULT_XZ_SIZE;
    public static int DEFAULT_Y_UP;
    public static int DEFAULT_Y_DOWN;

    @YAMLElement
    private int xzExpansion;

    @YAMLElement
    private int upExpansion;

    @YAMLElement
    private int downExpansion;


    public static void init() {
        ConfigurationSection config = GuildQ.getInstance().getConfig().getConfigurationSection("Quarters.region");
        if (config == null) {
            config = GuildQ.getInstance().getConfig().createSection("Quarters.region");
        }
        DEFAULT_XZ_SIZE = config.getInt("xz-size", 20);
        DEFAULT_Y_UP = config.getInt("y-up",20);
        DEFAULT_Y_DOWN = config.getInt("y-down", 10);

    }


    public QuarterRegion(Map<String, Object> map) {
        deserializeFromMap(map);
    }


    public QuarterRegion(GuildQuarter quarter) {
        this.quarter = quarter;
        this.quarterId = quarter.getShortId();
        this.regionId = quarter.getShortId() + "_quarter_region";
        create();
    }

    public void updateMembers() {
        if (quarter().getGuild() == null) return;
        Collection<UUID> memberList = quarter.getGuild().getMembers().stream().map(GuildMember::getUuid).collect(Collectors.toList());
        region().getMembers().getUniqueIds().forEach(uuid -> {
            if (!memberList.contains(uuid)) {
                region().getMembers().removePlayer(uuid);
            }
        });
        memberList.forEach(uuid -> region().getMembers().addPlayer(uuid));
    }





    public ProtectedCuboidRegion create() {
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(
                regionId, BukkitAdapter.adapt(
                quarter().getInitialLocation().add(new Vector(-DEFAULT_XZ_SIZE, -DEFAULT_Y_DOWN, -DEFAULT_XZ_SIZE))
        ).toVector().toBlockPoint()
                , BukkitAdapter.adapt(
                        quarter.getInitialLocation().add(DEFAULT_XZ_SIZE, DEFAULT_Y_UP, DEFAULT_XZ_SIZE))
                .toVector().toBlockPoint());
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).addRegion(region);
        return region;
    }

    public void setPermissions() {
        region().setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region().setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
    }

    public boolean exists() {
        return WorldGuardUtils.getRegionsInLocation(quarter().getInitialLocation())
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

    public void expand(BlockFace face, int distance) {
        WorldGuardUtils.expand(GuildUtils.getGuildWorld(), region(),
                BukkitAdapter.adapt(face).toBlockVector().multiply(distance));
    }

    public void contract(BlockFace face, int distance) {
        WorldGuardUtils.contract(GuildUtils.getGuildWorld(), region(),
                BukkitAdapter.adapt(face).toBlockVector().multiply(distance));
    }

    public void expandXZ(int distance) {
        xzExpansion += distance;
        WorldGuardUtils.expand(GuildUtils.getGuildWorld(), region(),
                BlockVector3.at(distance, 0, distance),
                    BlockVector3.at(-distance, 0, -distance));

    }

    public void expandUp(int distance) {
        upExpansion += distance;
        WorldGuardUtils.expand(GuildUtils.getGuildWorld(), region(), BlockVector3.at(0, distance, 0));
    }





    public void expandDown(int distance) {
        downExpansion += distance;
        WorldGuardUtils.expand(GuildUtils.getGuildWorld(), region(), BlockVector3.at(0, -distance, 0));
    }

    public void contractXZ(int distance) {
        xzExpansion -= distance;
        WorldGuardUtils.contract(GuildUtils.getGuildWorld(), region(),
                BlockVector3.at(distance, 0, distance),
                BlockVector3.at(-distance, 0, -distance));

    }

    public void contractUp(int distance) {
        upExpansion -= distance;
        WorldGuardUtils.contract(GuildUtils.getGuildWorld(), region(), BlockVector3.at(0, distance, 0));

    }

    public void contractDown(int distance) {
        downExpansion -= distance;
        WorldGuardUtils.contract(GuildUtils.getGuildWorld(), region(), BlockVector3.at(0, -distance, 0));

    }

    public void remove() {
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).removeRegion(this.regionId);
    }

    @Override
    public String toString() {
        return "QuarterRegion{" +
                "quarterId='" + quarterId + '\'' +
                ", quarter=" + quarter +
                ", regionId='" + regionId + '\'' +
                ", region=" + region() +
                ", xzExpansion=" + xzExpansion +
                ", upExpansion=" + upExpansion +
                ", downExpansion=" + downExpansion +
                '}';
    }
}
