package com.canthideinbush.guildquarters.commands.spawner;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class SpawnerParentCommand extends ParentCommand {

    public SpawnerParentCommand() {
        subCommands.add(new SpawnerBuildCommand());
        subCommands.add(new SpawnerRemoveCommand());
        subCommands.add(new PlaceAllCommand());
        subCommands.add(new RemoveAllCommand());
    }

    @Override
    public String getName() {
        return "spawner";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
