package com.canthideinbush.guildquarters.commands.structure;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructures;
import com.canthideinbush.guildquarters.quarters.structures.StructureBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.BuilderCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StructureBuildCommand extends BuilderCommand<QuarterStructure, StructureBuilder> {

    public StructureBuildCommand() {
        defaultConstructor();
    }

    public static HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    protected void completeAction(QuarterStructure structure) {
        GuildQ.getInstance().getQuarterStructures().addStructure(structure);
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return StructureParentCommand.class;
    }


    @Override
    protected List<Class<?>> getAdditionalMessageClasses() {
        return Collections.singletonList(StructureBuilder.class);
    }
}
