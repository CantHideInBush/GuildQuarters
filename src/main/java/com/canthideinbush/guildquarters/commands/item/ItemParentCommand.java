package com.canthideinbush.guildquarters.commands.item;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class ItemParentCommand extends ParentCommand {

    public ItemParentCommand() {
        subCommands.add(new BuildParentCommand());
    }

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
