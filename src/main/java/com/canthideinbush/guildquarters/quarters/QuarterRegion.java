package com.canthideinbush.guildquarters.quarters;


import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.glaremasters.guilds.guild.GuildMember;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SerializableAs("QuarterRegion")
public class QuarterRegion implements ABSave {



    @YAMLElement
    private String quarterId;
    private GuildQuarter quarter;

    private GuildQuarter quarter() {
        if (quarter != null) return quarter;
        else return quarter = GuildQ.getInstance().getQuartersManager().getByShortId(quarterId);
    }

    @YAMLElement
    private String regionId;
    public ProtectedCuboidRegion region() {
        return (ProtectedCuboidRegion) WorldGuardUtils.getRegion(GuildUtils.getGuildWorld(), regionId);
    }

    public static int DEFAULT_XZ_SIZE;
    public static int DEFAULT_Y_UP;
    public static int DEFAULT_Y_DOWN;



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
        updateMembers();
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
        setPermissions();
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

    public int getExpansion(BlockFace face) {
        Location min = BukkitAdapter.adapt(quarter().getInitialLocation().getWorld(), region().getMinimumPoint());
        Location max = BukkitAdapter.adapt(quarter().getInitialLocation().getWorld(), region().getMaximumPoint());
        Location defaultMin = quarter().getInitialLocation().add(-DEFAULT_XZ_SIZE, -DEFAULT_Y_DOWN, -DEFAULT_XZ_SIZE);
        Location defaultMax = quarter().getInitialLocation().add(DEFAULT_XZ_SIZE, DEFAULT_Y_UP, DEFAULT_XZ_SIZE);

        int expansion = 0;

        Vector direction = face.getDirection();

        @NotNull Vector substMin = min.subtract(defaultMin).toVector().multiply(direction);
        @NotNull Vector substMax = max.subtract(defaultMax).toVector().multiply(direction);

        if (direction.getZ() < 0) {
            expansion += substMin.getZ();
        }
        if (direction.getX() < 0) {
            expansion += substMin.getX();
        }
        if (direction.getY() < 0) {
            expansion += substMin.getY();
        }
        if (direction.getZ() > 0) {
            expansion += substMax.getZ();
        }
        if (direction.getX() > 0) {
            expansion += substMax.getX();
        }
        if (direction.getY() > 0) {
            expansion += substMax.getY();
        }





        return expansion;
    }







    public void expand(BlockFace face, int distance) {
        WorldGuardUtils.expand(GuildUtils.getGuildWorld(), region(),
                BukkitAdapter.adapt(face).toBlockVector().multiply(distance));
    }

    public void contract(BlockFace face, int distance) {
        WorldGuardUtils.contract(GuildUtils.getGuildWorld(), region(),
                BukkitAdapter.adapt(face.getOppositeFace()).toBlockVector().multiply(distance));
    }


    public void remove() {
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).removeRegion(this.regionId);
    }

    @Override
    public String toString() {
        return "QuarterRegion{" +
                "quarterId='" + quarterId + '\'' +
                ", regionId='" + regionId + '\'' +
                ", region=" + region() +
                '}';
    }
}
