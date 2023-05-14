package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class GeneratorParentCommand extends ParentCommand {

    public GeneratorParentCommand() {
        subCommands.add(new BuildParentCommand());
    }


    @Override
    public String getName() {
        return "generator";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
