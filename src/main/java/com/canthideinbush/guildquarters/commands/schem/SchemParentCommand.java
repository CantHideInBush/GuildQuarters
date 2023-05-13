package com.canthideinbush.guildquarters.commands.schem;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.commands.tiers.TierParentCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.Collection;

public class SchemParentCommand extends ParentCommand {

    public SchemParentCommand() {
        subCommands.add(new CreateSchemCommand());
        subCommands.add(new RemoveSchemCommand());
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
