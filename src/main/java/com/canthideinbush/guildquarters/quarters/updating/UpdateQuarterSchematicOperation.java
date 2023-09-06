package com.canthideinbush.guildquarters.quarters.updating;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.utils.WEQuarterUtils;
import com.canthideinbush.utils.worldedit.SessionProgressTracker;

import java.util.ArrayList;
import java.util.List;

public class UpdateQuarterSchematicOperation extends UpdateOperation {


    public static void updateAll() {
        int parallelUpdates = GuildQ.getInstance().getConfig().getInt("Quarters.parallel-quarter-schematic-updates", 1);
        QuartersUpdateQueue queue = GuildQ.getInstance().getQuartersUpdateQueue();
        List<GuildQuarter> quarters = GuildQ.getInstance().getQuartersManager().getObjects();
        for (int i = 0; i < quarters.size(); i+= parallelUpdates) {
            ArrayList<UpdateQuarterSchematicOperation> operations = new ArrayList<>();
            for (int j = 0; j < parallelUpdates; j++) {
                if (quarters.size() < i + j) {
                    operations.add(new UpdateQuarterSchematicOperation(quarters.get(i + j)));
                }
            }
            queue.queueAsync(
                    new BundledOperation(operations)
            );
        }
    }

    private final GuildQuarter quarter;
    private SessionProgressTracker tracker;

    public UpdateQuarterSchematicOperation(GuildQuarter quarter) {
        this.quarter = quarter;
    }

    @Override
    public void run() {
        tracker = WEQuarterUtils.updateQuarterSchematic(quarter, null);
    }

    @Override
    public boolean isComplete() {
        return tracker.getProgress() != null && tracker.getProgress().isComplete();
    }
}
