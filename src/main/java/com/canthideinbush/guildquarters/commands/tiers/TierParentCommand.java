package com.canthideinbush.guildquarters.commands.tiers;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.commands.tiers.structure.StructureParentCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class TierParentCommand extends ParentCommand {

    public TierParentCommand() {
        subCommands.add(new StructureParentCommand());
        subCommands.add(new SetTierCommand());
        subCommands.add(new UpgradeCommand());
        subCommands.add(new DowngradeCommand());
    }

    @Override
    public String getName() {
        return "tier";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
