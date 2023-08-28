package com.canthideinbush.guildquarters.commands.region;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class RegionParentCommand extends ParentCommand {

    public RegionParentCommand() {
        subCommands.add(new SetRegionOffsetCommand());
        subCommands.add(new SetDefaultPropertyCommand());
        subCommands.add(new UpdateAllCommand());
    }
    @Override
    public String getName() {
        return "region";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
