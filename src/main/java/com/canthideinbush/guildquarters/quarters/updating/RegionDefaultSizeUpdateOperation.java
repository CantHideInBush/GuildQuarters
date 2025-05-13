package com.canthideinbush.guildquarters.quarters.updating;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterRegion;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.WorldGuardUtils;
import com.sk89q.worldedit.math.BlockVector3;

public class RegionDefaultSizeUpdateOperation extends UpdateOperation {

    public static void updateAll(BlockVector3 minDifference, BlockVector3 maxDifference) {
        QuartersUpdateQueue queue = GuildQ.getInstance().getQuartersUpdateQueue();
        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            queue.queueAsync(new RegionDefaultSizeUpdateOperation(quarter.getRegion(), minDifference, maxDifference));
        }
    }

    private final QuarterRegion region;
    private final BlockVector3 minDifference;
    private final BlockVector3 maxDifference;

    public RegionDefaultSizeUpdateOperation(QuarterRegion region, BlockVector3 minDifference, BlockVector3 maxDifference) {
        this.region = region;
        this.minDifference = minDifference;
        this.maxDifference = maxDifference;
    }

    @Override
    public void run() {
        BlockVector3 minExpand = BlockVector3.at(
                Math.min(minDifference.getBlockX(), 0),
                Math.min(minDifference.getBlockY(), 0),
                Math.min(minDifference.getBlockZ(), 0)
        );

        BlockVector3 minContract = BlockVector3.at(
                Math.max(minDifference.getBlockX(), 0),
                Math.max(minDifference.getBlockY(), 0),
                Math.max(minDifference.getBlockZ(), 0)
        );

        BlockVector3 maxContract = BlockVector3.at(
                Math.min(maxDifference.getBlockX(), 0),
                Math.min(maxDifference.getBlockY(), 0),
                Math.min(maxDifference.getBlockZ(), 0)
        );

        BlockVector3 maxExpand = BlockVector3.at(
                Math.max(maxDifference.getBlockX(), 0),
                Math.max(maxDifference.getBlockY(), 0),
                Math.max(maxDifference.getBlockZ(), 0)
        );



        WorldGuardUtils.expand(GuildUtils.getGuildWorld(), region.region(), minExpand, maxExpand);
        WorldGuardUtils.contract(GuildUtils.getGuildWorld(), region.region(), minContract, maxContract);

        complete();
    }
}
