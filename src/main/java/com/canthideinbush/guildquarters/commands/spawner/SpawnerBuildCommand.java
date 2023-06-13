package com.canthideinbush.guildquarters.commands.spawner;

import com.canthideinbush.guildquarters.quarters.spawners.MMSpawner;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawnerBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.BuilderCommand;

import java.util.HashMap;

public class SpawnerBuildCommand extends BuilderCommand<MMSpawner, MMSpawnerBuilder> {


    public SpawnerBuildCommand() {
        defaultConstructor();
    }

    public static final HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    protected void completeAction(MMSpawner spawner) {
        MMSpawner.register(spawner);
    }

    @Override
    public String getName() {
        return "build";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return SpawnerParentCommand.class;
    }
}
