package com.canthideinbush.guildquarters.quarters.updating;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterRegion;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Location;

public class RegionUpdateOperation extends UpdateOperation {

    public static void updateAll() {
        QuartersUpdateQueue queue = GuildQ.getInstance().getQuartersUpdateQueue();
        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            queue.queueAsync(new RegionUpdateOperation(quarter));
        }
    }

    private final GuildQuarter quarter;

    public RegionUpdateOperation(GuildQuarter quarter) {
        this.quarter = quarter;
    }
    @Override
    public void run() {
        QuarterRegion region = quarter.getRegion();


        BlockVector3 minExpansion = region.getExpansionMinPoint();
        BlockVector3 maxExpansion = region.getExpansionMaxPoint();

        region.updateCenter();

        Location center = region.getCenter();

        BlockVector3 newMin = BlockVector3.at(center.getBlockX() - minExpansion.getBlockX() - QuarterRegion.DEF_NEG_X, center.getBlockY() - minExpansion.getBlockY() - QuarterRegion.DEF_NEG_Y, center.getBlockZ() - minExpansion.getBlockZ() - QuarterRegion.DEF_NEG_Z);
        BlockVector3 newMax = BlockVector3.at(center.getBlockX() + maxExpansion.getBlockX() + QuarterRegion.DEF_POS_X, center.getBlockY() + maxExpansion.getBlockY() + QuarterRegion.DEF_POS_Y, center.getBlockZ() + maxExpansion.getBlockZ() + QuarterRegion.DEF_POS_Z);


        CuboidRegion newRegion = new CuboidRegion(BukkitAdapter.adapt(GuildUtils.getGuildWorld()),
                newMin, newMax);

        WorldGuardUtils.redefine(GuildUtils.getGuildWorld(), region.region(), newRegion);
    }
}
