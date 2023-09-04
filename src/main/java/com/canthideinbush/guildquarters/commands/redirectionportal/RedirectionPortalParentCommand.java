package com.canthideinbush.guildquarters.commands.redirectionportal;

import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;

public class RedirectionPortalParentCommand extends ParentCommand {

    public RedirectionPortalParentCommand() {
        subCommands.add(new RedirectionPortalBuildCommand());
        subCommands.add(new RemoveRedirectionPortalCommand());
    }


    @Override
    public String getName() {
        return "rportal";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
