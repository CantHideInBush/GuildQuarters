package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.generators.GeneratorParentCommand;
import com.canthideinbush.guildquarters.commands.item.ItemParentCommand;
import com.canthideinbush.guildquarters.commands.quarter.QuartersParentCommand;
import com.canthideinbush.guildquarters.commands.quarter.TeleportToQuarterCommand;
import com.canthideinbush.guildquarters.commands.region.RegionParentCommand;
import com.canthideinbush.guildquarters.commands.schem.SchemParentCommand;
import com.canthideinbush.guildquarters.commands.spawner.SpawnerParentCommand;
import com.canthideinbush.guildquarters.commands.structure.StructureParentCommand;
import com.canthideinbush.guildquarters.commands.tiers.TierParentCommand;
import com.canthideinbush.utils.CHIBPlugin;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

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
        subCommands.add(new CollectAllCommand());
        subCommands.add(new StructureParentCommand());
        subCommands.add(new ConfirmCommand());
        subCommands.add(new AdjustChunkSizeCommand());
        subCommands.add(new SetSpawnOffsetCommand());
        subCommands.add(new SpawnerParentCommand());
        subCommands.add(new SetProxyNPCCommand());
        subCommands.add(new SetNPCLocationCommand());
        subCommands.add(new InitializeCommand());
        subCommands.add(new ResetReferenceCommand());
        subCommands.add(new RegionParentCommand());
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
