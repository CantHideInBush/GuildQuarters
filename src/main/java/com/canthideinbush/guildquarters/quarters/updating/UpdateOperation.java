package com.canthideinbush.guildquarters.quarters.updating;

public abstract class UpdateOperation {

    private boolean isComplete = false;

    public boolean isComplete() {
        return isComplete;
    }


    private void complete() {
        isComplete = true;
    }

    public void start() {
        run();
        complete();
    }

    public abstract void run();
}
