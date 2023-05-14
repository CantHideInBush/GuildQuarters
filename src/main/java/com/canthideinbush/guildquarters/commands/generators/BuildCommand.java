package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.utils.commands.InternalCommand;

public abstract class BuildCommand extends InternalCommand {

    protected BuildParentCommand parentCommand;

    public BuildCommand(BuildParentCommand parentCommand) {
        this.parentCommand = parentCommand;
    }


}
