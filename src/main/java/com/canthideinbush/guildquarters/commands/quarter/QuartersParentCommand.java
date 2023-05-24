package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.commands.quarter.region.ContractCommand;
import com.canthideinbush.guildquarters.commands.quarter.region.ExpandCommand;
import com.canthideinbush.guildquarters.commands.quarter.region.GetExpansion;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

import java.util.ArrayList;
import java.util.Collection;

public class QuartersParentCommand extends ParentCommand {



    public QuartersParentCommand() {
        subcommands.add(new CreateQuarterCommand());
        subcommands.add(new CreateNamedQuarterCommand());
        subcommands.add(new QuarterResetCommand());
        subcommands.add(new TemplateQuarterTeleportCommand());
        subcommands.add(new QuarterDeleteCommand());
        subcommands.add(new SetSpawnLocationCommand());
        subcommands.add(new InfoCommand());
        subcommands.add(new ExpandCommand());
        subcommands.add(new ContractCommand());
        subcommands.add(new PasteSchemCommand());
        subcommands.add(new UndoSchemCommand());
        subcommands.add(new GetExpansion());
        subcommands.add(new AddGenerator());
        subcommands.add(new RemoveGenerator());


    }

    private final ArrayList<InternalCommand> subcommands = new ArrayList<>();


    @Override
    public Collection<InternalCommand> getSubcommands() {
        return subcommands;
    }

    @Override
    public String getName() {
        return "quarter";
    }


    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }






}
