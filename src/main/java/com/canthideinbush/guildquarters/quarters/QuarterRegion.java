package com.canthideinbush.guildquarters.quarters;


import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.canthideinbush.utils.storing.ABSave;
import com.canthideinbush.utils.storing.StoreResult;
import com.canthideinbush.utils.storing.YAMLElement;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import me.glaremasters.guilds.guild.GuildMember;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

@SerializableAs("QuarterRegion")
public class QuarterRegion implements ABSave {



    @YAMLElement
    private String quarterId;

    @YAMLElement
    private String regionId;

    @YAMLElement
    private Location savedCenter;

    @StoreResult(field = "savedCenter")
    private Location center() {
        return quarter().getInitialLocation().add(WGQuarterUtils.getRegionOffset());
    }


    private GuildQuarter quarter;

    private GuildQuarter quarter() {
        if (quarter != null) return quarter;
        else return quarter = GuildQ.getInstance().getQuartersManager().getByShortId(quarterId);
    }


    public ProtectedCuboidRegion region() {
        return (ProtectedCuboidRegion) WorldGuardUtils.getRegion(GuildUtils.getGuildWorld(), regionId);
    }


    public static int DEF_POS_X;
    public static int DEF_POS_Y;
    public static int DEF_POS_Z;
    public static int DEF_NEG_X;
    public static int DEF_NEG_Y;
    public static int DEF_NEG_Z;



    public static void init() {
        ConfigurationSection config = GuildQ.getInstance().getConfig().getConfigurationSection("Quarters.region");
        if (config == null) {
            config = GuildQ.getInstance().getConfig().createSection("Quarters.region");
        }

        DEF_POS_X = config.getInt("default-positive-x", 20);
        DEF_POS_Y = config.getInt("default-positive-y", 20);
        DEF_POS_Z = config.getInt("default-positive-z", 20);
        DEF_NEG_X = config.getInt("default-negative-x", 20);
        DEF_NEG_Y = config.getInt("default-negative-y", 20);
        DEF_NEG_Z = config.getInt("default-negative-z", 20);

    }


    public QuarterRegion(Map<String, Object> map) {
        deserializeFromMap(map);
    }


    public QuarterRegion(GuildQuarter quarter) {
        this.quarter = quarter;
        this.quarterId = quarter.getShortId();
        this.regionId = quarter.getShortId() + "_quarter_region";
    }

    void initialize() {
        //If region was not using center(), it couldn't support some operations and might became corrupted after they were used. Hence, it's safer to reset it to default form.
        if (savedCenter == null) {
            remove();
        }
        if (!exists()) create();
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
                regionId, defaultMinPoint()
                , defaultMaxPoint());
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).addRegion(region);
        setPermissions();
        return region;
    }

    public BlockVector3 defaultMinPoint() {
        Location center = center();
        return BlockVector3.at(center.getBlockX() - DEF_NEG_X, center.getBlockY() - DEF_NEG_Y, center.getBlockZ() - DEF_NEG_Z);
    }
    
    public BlockVector3 defaultMaxPoint() {
        Location center = center();
        return BlockVector3.at(center.getBlockX() + DEF_POS_X, center.getBlockY() + DEF_POS_Y, center.getBlockZ() + DEF_POS_Z);
    }

    public void setPermissions() {
        region().setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region().setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
    }

    public boolean exists() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(GuildUtils.getGuildWorld())).getRegion(regionId) != null;
    }

    public int getExpansion(BlockFace face) {
        int expansion = 0;

        Vector direction = face.getDirection();
        BlockVector3 blockVectorDirection = BlockVector3.at(direction.getX(), direction.getY(), direction.getZ());

        BlockVector3 expansionMin = getExpansionMinPoint().multiply(blockVectorDirection);
        BlockVector3 expansionMax = getExpansionMaxPoint().multiply(blockVectorDirection);

        if (direction.getZ() < 0) {
            expansion -= expansionMin.getZ();
        }
        if (direction.getX() < 0) {
            expansion -= expansionMin.getX();
        }
        if (direction.getY() < 0) {
            expansion -= expansionMin.getY();
        }
        if (direction.getZ() > 0) {
            expansion += expansionMax.getZ();
        }
        if (direction.getX() > 0) {
            expansion += expansionMax.getX();
        }
        if (direction.getY() > 0) {
            expansion += expansionMax.getY();
        }





        return expansion;
    }


    public BlockVector3 getExpansionMinPoint() {
        return region().getMinimumPoint().subtract(defaultMinPoint());
    }

    public BlockVector3 getExpansionMaxPoint() {
        return region().getMaximumPoint().subtract(defaultMaxPoint());
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
