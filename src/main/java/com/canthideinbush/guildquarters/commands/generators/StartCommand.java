package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.Reflector;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.builder.BuilderCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StartCommand extends com.canthideinbush.utils.commands.builder.StartCommand {

    public static final HashMap<String, Class<? extends ObjectBuilder<?>>> builders = new HashMap<>();

    public StartCommand(BuilderCommand<?, ?> parent) {
        super(parent);
    }

    @Override
    public HashMap<String, Class<? extends ObjectBuilder<?>>> builders() {
        return builders;
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }
}
