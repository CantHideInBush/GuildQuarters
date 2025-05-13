package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ConstantItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.RandomItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.ConstantGeneratorBuilder;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.RandomItemGeneratorBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.commands.builder.BuilderCommand;

import java.util.HashMap;
import java.util.List;

public class GeneratorBuildCommand extends BuilderCommand<ItemGenerator, GeneratorBuilder> {




    public GeneratorBuildCommand() {
        defaultConstructor();
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

    @Override
    public Class<? extends GeneratorBuilder> getBuilderFor(ItemGenerator generator) {
        if (generator instanceof RandomItemGenerator) return RandomItemGeneratorBuilder.class;
        else if (generator instanceof ConstantItemGenerator) return ConstantGeneratorBuilder.class;
        return null;
    }

    @Override
    public ItemGenerator findById(String s) {
        return GuildQ.getInstance().getItemGenerators().get(s);
    }

    @Override
    public List<String> getIdCompletion() {
        return GuildQ.getInstance().getItemGenerators().getIds();
    }
}
