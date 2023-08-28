package com.canthideinbush.guildquarters.commands.region;

import com.canthideinbush.utils.commands.InternalCommand;

public class SetDefaultPropertyCommand extends com.canthideinbush.utils.commands.InternalCommand {
    @Override
    public String getName() {
        return "setdefproperty";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return RegionParentCommand.class;
    }
}
