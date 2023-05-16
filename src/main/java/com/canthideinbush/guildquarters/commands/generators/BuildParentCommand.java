package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.commands.builder.BuilderCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

public class BuildParentCommand extends BuilderCommand<ItemGenerator, GeneratorBuilder> {

    public BuildParentCommand() {
        subCommands.add(new StartCommand(this));
        subCommands.add(new WithCommand(this));
        subCommands.add(new StartCommand(this));
        subCommands.add(new CompleteCommand(this));
    }

    @Override
    public String getName() {
        return "build";
    }


    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return GeneratorParentCommand.class;
    }


}
