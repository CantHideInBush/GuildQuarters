package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.utils.Utils;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SetProxyNPCCommand extends InternalCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length <= getArgIndex()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        if (!Utils.isNumeric(args[getArgIndex()])) {
            sendConfigErrorMessage(sender, DefMessages.INC_DATA_TYPE);
            return false;
        }

        int id = Integer.parseInt(args[getArgIndex()]);

        GuildQ.getInstance().getQuartersManager().setProxyNPCId(id);
        GuildQ.getInstance().getQuartersManager().getObjects().forEach(
                GuildQuarter::initializeNPC
        );

        sendConfigSuccessMessage(sender, getMessagePath("success"));


        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Ustawiono id!";

    @Override
    public String getName() {
        return "setproxynpc";
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {


        return Collections.emptyList();
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
