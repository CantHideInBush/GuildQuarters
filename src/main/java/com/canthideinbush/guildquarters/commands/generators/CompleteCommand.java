package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerators;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.chat.ChatUtils;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CompleteCommand extends com.canthideinbush.utils.commands.builder.CompleteCommand<ItemGenerator> {


    public CompleteCommand(BuildParentCommand buildParentCommand) {
        super(buildParentCommand);
    }

    @Override
    protected void complete(ItemGenerator itemGenerator) {
        GuildQ.getInstance().getItemGenerators().register(itemGenerator);
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }
}
