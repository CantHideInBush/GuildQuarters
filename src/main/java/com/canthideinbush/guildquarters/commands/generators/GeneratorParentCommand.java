package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.RandomItemGeneratorBuilder;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.Collections;
import java.util.List;

public class GeneratorParentCommand extends ParentCommand {

    public GeneratorParentCommand() {
        subCommands.add(new GeneratorBuildCommand());
    }


    @Override
    public String getName() {
        return "generator";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    @Override
    protected List<Class<?>> getAdditionalMessageClasses() {
        return Collections.singletonList(RandomItemGeneratorBuilder.class);
    }
}
