package com.canthideinbush.guildquarters.commands.spawner;

import com.canthideinbush.utils.commands.InternalCommand;

public class SpawnerRemoveCommand extends InternalCommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return SpawnerParentCommand.class;
    }
}
