package com.canthideinbush.guildquarters.quarters.updating;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterRegion;
import com.canthideinbush.guildquarters.quarters.WGQuarterUtils;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.util.Vector;

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

        Vector regionOffset = WGQuarterUtils.getRegionOffset();

        BlockVector3 newMin = BlockVector3.at(-QuarterRegion.DEF_NEG_X + minExpansion.getX() + quarter.getInitialLocation().getBlockX() + regionOffset.getBlockX(), -QuarterRegion.DEF_NEG_Y + minExpansion.getY() + quarter.getInitialLocation().getBlockY() + regionOffset.getBlockY(), -QuarterRegion.DEF_NEG_Z + minExpansion.getZ() + quarter.getInitialLocation().getBlockZ() + regionOffset.getBlockZ());
        BlockVector3 newMax = BlockVector3.at(QuarterRegion.DEF_POS_X + maxExpansion.getX() + quarter.getInitialLocation().getBlockX() + regionOffset.getBlockX(), QuarterRegion.DEF_POS_Y + maxExpansion.getY() + quarter.getInitialLocation().getY() + regionOffset.getBlockZ(), QuarterRegion.DEF_POS_Z + maxExpansion.getZ() + quarter.getInitialLocation().getBlockZ() + regionOffset.getBlockZ());


        CuboidRegion newRegion = new CuboidRegion(BukkitAdapter.adapt(GuildUtils.getGuildWorld()),
                newMin, newMax);

        WorldGuardUtils.redefine(GuildUtils.getGuildWorld(), region.region(), newRegion);
    }
}
