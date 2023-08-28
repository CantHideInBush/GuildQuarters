package com.canthideinbush.guildquarters.quarters.updating;

import com.canthideinbush.guildquarters.GuildQ;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class QuartersUpdateQueue {


    public QuartersUpdateQueue() {

    }

    private BukkitTask syncTask;
    private BukkitTask asyncTask;

    private static final class QueueRunnable extends BukkitRunnable {

        private final ArrayList<UpdateOperation> operations;

        public QueueRunnable(ArrayList<UpdateOperation> operations) {
            this.operations = operations;
        }

        private UpdateOperation currentOperation = null;

        @Override
        public void run() {
            if (currentOperation == null || currentOperation.isComplete()) {
                operations.remove(currentOperation);
                if (operations.isEmpty()) {
                    cancel();
                    return;
                }
                currentOperation = operations.get(0);
                currentOperation.start();
            }
        }
    }

    private final ArrayList<UpdateOperation> syncOperations = new ArrayList<>();
    private final ArrayList<UpdateOperation> asyncOperations = new ArrayList<>();

    public void queueSync(UpdateOperation operation) {
        syncOperations.add(operation);
        if (syncTask == null || syncTask.isCancelled()) {
            syncTask = new QueueRunnable(syncOperations).runTaskTimer(GuildQ.getInstance(), 0, 1);
        }
    }

    public void queueAsync(UpdateOperation operation) {
        asyncOperations.add(operation);
        if (asyncTask == null || asyncTask.isCancelled()) {
            asyncTask = new QueueRunnable(asyncOperations).runTaskTimerAsynchronously(GuildQ.getInstance(), 0, 1);
        }
    }


}
