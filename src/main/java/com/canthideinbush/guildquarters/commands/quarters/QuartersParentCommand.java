package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.commands.tiers.SetTierCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.ArrayList;
import java.util.Collection;

public class QuartersParentCommand extends ParentCommand {



    public QuartersParentCommand() {
        subcommands.add(new CreateQuarterCommand());
        subcommands.add(new CreateNamedQuarterCommand());
        subcommands.add(new TeleportToQuarterCommand());
        subcommands.add(new QuarterResetCommand());
        subcommands.add(new TemplateQuarterTeleportCommand());


    }

    private final ArrayList<InternalCommand> subcommands = new ArrayList<>();


    @Override
    public Collection<InternalCommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public String getName() {
        return "quarters";
    }


    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    @Override
    public String getPermission() {
        return "quarters";
    }





}
