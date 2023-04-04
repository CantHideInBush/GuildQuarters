package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.ABCompleter;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class
QuarterDeleteCommand extends InternalCommand implements ABArgumentCompletion {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }
        quarter.remove();
        sendConfigSuccessMessage(sender, "command.quarter.delete.success");

        return true;
    }

    @ABCompleter(index = 0, arg = "delete")
    private List<String> COMPLETER_0() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    List<TabCompleter> completion = prepareCompletion();

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
