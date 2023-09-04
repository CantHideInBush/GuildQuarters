package com.canthideinbush.guildquarters.commands.redirectionportal;

import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortals;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.ABCompleter;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RemoveRedirectionPortalCommand extends com.canthideinbush.utils.commands.InternalCommand {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length <= getArgIndex()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        String name = args[getArgIndex()];

        RedirectionPortals.unregister(name);

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Usunieto portal!";

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        if (args.length == getArgIndex() + 1) {
            return RedirectionPortals.getIds();
        }

        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return RedirectionPortalParentCommand.class;
    }
}
