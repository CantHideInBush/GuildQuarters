package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortal;
import com.canthideinbush.guildquarters.quarters.portals.RedirectionPortals;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AddRedirectionPortal extends InternalCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length <= getArgIndex() + 1) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        String quarter = args[getArgIndex()];
        String portal = args[getArgIndex() + 1];

        GuildQuarter guildQuarter;

        if ((guildQuarter = GuildQ.getInstance().getQuartersManager().getByShortId(quarter)) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        if (!RedirectionPortals.exists(portal)) {
            sendConfigErrorMessage(sender, "common.redirection-portal-nonexistent");
            return false;
        }

        guildQuarter.getQuarterObjects().addRPortal(RedirectionPortals.create(portal));
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Dodano portal!";

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        if (args.length == getArgIndex() + 1) {
            return GuildQ.getInstance().getQuartersManager().getShortIds();
        }
        else if (args.length == getArgIndex() + 2) {
            return RedirectionPortals.getIds();
        }
        return Collections.emptyList();
    }





    @Override
    public String getName() {
        return "addrportal";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }
}
