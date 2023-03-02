package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.CHIBCommandsRegistry;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CreateNamedQuarterCommand extends InternalCommand {
    @Override
    public String getName() {
        return "createnamed";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public String getPermission() {
        return "createnamed";
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length - 1 == getArgIndex()) {
            return Collections.singletonList(" ");
        }
        return Collections.emptyList();
    }


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        if (GuildQ.getInstance().getQuartersManager().createNamedQuarter(parser.next())) {
            sendConfigSuccessMessage(sender, "guildq-quarter-create-success");
        }
        else {
            sendConfigErrorMessage(sender, "guildq-quarter-create-shortId_taken");
            return false;
        }

        return true;
    }
}
