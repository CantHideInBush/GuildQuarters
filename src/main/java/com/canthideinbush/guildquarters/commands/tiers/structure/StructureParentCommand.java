package com.canthideinbush.guildquarters.commands.tiers.structure;

import com.canthideinbush.guildquarters.commands.tiers.TierParentCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.List;

public class StructureParentCommand extends ParentCommand {

    public StructureParentCommand() {
        subCommands.add(new CreateStructureCommand());
        subCommands.add(new RemoveStructureCommand());
    }

    @Override
    public String getName() {
        return "structure";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return TierParentCommand.class;
    }

}
