package com.canthideinbush.guildquarters.commands.item;

import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class BuildParentCommand extends ParentCommand {

    public BuildParentCommand() {
        subCommands.add(new StartCommand());
        subCommands.add(new CompleteCommand());
        subCommands.add(new WithCommand());
    }

    @Override
    public String getName() {
        return "build";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return ItemParentCommand.class;
    }
}
