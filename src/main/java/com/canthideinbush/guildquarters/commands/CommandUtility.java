package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

public class CommandUtility {


    /**
     *
     * @param name - shortId of quarter
     * @param sender - sender of command
     * @param command - command instance that's handling command execution
     * @return null if quarter is not existent, and error message was sent.
     */
    public static GuildQuarter getQuarter(String name, CommandSender sender, InternalCommand command) {
        GuildQuarter quarter = GuildQ.getInstance().getQuartersManager().getByShortId(name);
        if (quarter == null) command.sendConfigErrorMessage(sender, CommonMessages.QUARTER_NONEXISTENT);
        return quarter;
    }


}
