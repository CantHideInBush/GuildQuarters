package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class QuarterResetCommand extends InternalCommand {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
        }
        String shortId = parser.next();
        GuildQuarter quarter = GuildQ.getInstance().getQuartersManager().getByShortId(shortId);

        if (quarter == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        quarter.reset();
        sendConfigSuccessMessage(sender, "command.quarter.reset.success");

        return true;
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length - 1 == getArgIndex()) {
            return GuildQ.getInstance().getQuartersManager().getShortIds();
        }
        return Collections.emptyList();
    }
}
