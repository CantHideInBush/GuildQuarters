package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.commands.builder.BuilderCommand;
import com.canthideinbush.utils.commands.builder.CompleteCommand;
import com.canthideinbush.utils.commands.builder.StartCommand;
import com.canthideinbush.utils.commands.builder.WithCommand;

import java.util.HashMap;

public class GeneratorBuildCommand extends BuilderCommand<ItemGenerator, GeneratorBuilder> {


    public GeneratorBuildCommand() {
        subCommands.add(new StartCommand(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return GeneratorBuildCommand.class;
            }
        });
        subCommands.add(new WithCommand(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return GeneratorBuildCommand.class;
            }
        });
        subCommands.add(new CompleteCommand(this) {
            @Override
            public Class<? extends InternalCommand> getParentCommandClass() {
                return GeneratorBuildCommand.class;
            }
        });
    }


    public static HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return GeneratorParentCommand.class;
    }


    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    protected void completeAction(ItemGenerator generator) {
        GuildQ.getInstance().getItemGenerators().register(generator);
    }
}
