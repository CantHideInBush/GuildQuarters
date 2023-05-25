package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.generators.GeneratorParentCommand;
import com.canthideinbush.guildquarters.commands.item.ItemParentCommand;
import com.canthideinbush.guildquarters.commands.quarter.QuartersParentCommand;
import com.canthideinbush.guildquarters.commands.quarter.TeleportToQuarterCommand;
import com.canthideinbush.guildquarters.commands.schem.SchemParentCommand;
import com.canthideinbush.guildquarters.commands.tiers.TierParentCommand;
import com.canthideinbush.utils.CHIBPlugin;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.ArrayList;
import java.util.Collection;

public class MainCommand extends ParentCommand {


    public MainCommand(CHIBPlugin plugin) {
        super(plugin);
        subCommands.add(new SchemParentCommand());
        subCommands.add(new QuartersParentCommand());
        subCommands.add(new ToGuildWorldCommand());
        subCommands.add(new TierParentCommand());
        subCommands.add(new TeleportToQuarterCommand());
        subCommands.add(new GeneratorParentCommand());
        subCommands.add(new ItemParentCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new HomeCommand());
        subCommands.add(new CollectCommand());
    }

    @Override
    public CHIBPlugin getPlugin() {
        return GuildQ.getInstance();
    }


    @Override
    public String getName() {
        return "gq";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return null;
    }

    @Override
    public String getPermission() {
        return "GuildQuarters";
    }
}
