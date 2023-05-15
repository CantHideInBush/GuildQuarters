package com.canthideinbush.guildquarters.commands.item;

import com.canthideinbush.utils.commands.InternalCommand;

public class WithCommand extends com.canthideinbush.utils.commands.InternalCommand {
    @Override
    public String getName() {
        return "with";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }
}
