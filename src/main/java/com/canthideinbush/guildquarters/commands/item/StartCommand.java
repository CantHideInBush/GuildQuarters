package com.canthideinbush.guildquarters.commands.item;

import com.canthideinbush.utils.commands.InternalCommand;

public class StartCommand extends InternalCommand {
    @Override
    public String getName() {
        return "start";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }
}
