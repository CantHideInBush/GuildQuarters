package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.commands.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BuildParentCommand extends ParentCommand {

    public BuildParentCommand() {
        subCommands.add(new StartCommand(this));
        subCommands.add(new WithCommand(this));
        subCommands.add(new StartCommand(this));
        subCommands.add(new CompleteCommand(this));
    }

    private final HashMap<Player, GeneratorBuilder> data = new HashMap<>();

    public void setGeneratorBuilder(Player player, GeneratorBuilder builder) {
        data.put(player, builder);
    }

    public GeneratorBuilder getGeneratorBuilder(Player player) {
        return data.getOrDefault(player, null);
    }

    public boolean isBuilding(Player player) {
        return data.containsKey(player);
    }

    public void complete(Player player) {
        GuildQ.getInstance().getItemGenerators().register(getGeneratorBuilder(player).build());
        data.remove(player);
    }

    @Override
    public boolean execute(ConsoleCommandSender sender, String[] args) {
        return super.execute(sender, args);
    }

    public ItemGenerator getGenerator(String[] args) {
        return GuildQ.getInstance().getItemGenerators().get(args[getArgIndex()]);
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
