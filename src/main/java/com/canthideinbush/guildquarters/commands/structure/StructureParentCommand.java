package com.canthideinbush.guildquarters.commands.structure;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class StructureParentCommand extends ParentCommand {

    public StructureParentCommand() {
        subCommands.add(new StructureBuildCommand());
        subCommands.add(new StructureRemoveCommand());
    }

    @Override
    public String getName() {
        return "structure";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
