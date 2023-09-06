package com.canthideinbush.guildquarters.quarters.updating;

public abstract class UpdateOperation {

    private boolean isComplete = false;

    public boolean isComplete() {
        return isComplete;
    }


    protected void complete() {
        isComplete = true;
    }

    public void start() {
        run();
    }

    public abstract void run();
}
