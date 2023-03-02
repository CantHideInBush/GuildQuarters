package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.commands.quarters.QuartersParentCommand;
import com.canthideinbush.utils.CHIBPlugin;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.ArrayList;
import java.util.Collection;

public class MainCommand extends ParentCommand {

    private final ArrayList<InternalCommand> subcommands = new ArrayList<>();

    public MainCommand(CHIBPlugin plugin) {
        super(plugin);

        subcommands.add(new QuartersParentCommand());
        subcommands.add(new ToGuildWorldCommand());
    }

    @Override
    public Collection<InternalCommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public String getName() {
        return "guildq";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return null;
    }

    @Override
    public String getPermission() {
        return "GuildQuarters.Command.Main";
    }
}
