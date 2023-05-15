package com.canthideinbush.guildquarters.commands.item;

import com.canthideinbush.utils.commands.InternalCommand;

public class CompleteCommand extends com.canthideinbush.utils.commands.InternalCommand {
    @Override
    public String getName() {
        return "complete";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }
}
