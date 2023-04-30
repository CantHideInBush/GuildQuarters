package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.ABCompleter;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends InternalCommand implements ABArgumentCompletion {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());


        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String shortId = parser.next();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(shortId)) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        GuildQ.getInstance().getUtilsProvider().getChatUtils().sendMessage(sender,
                quarter.toString(), ChatColor.GREEN);



        return true;
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }

    private final List<TabCompleter> completion = prepareCompletion();



    @ABCompleter(index = 0, arg = "info")
    private List<String> complete() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }


    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }
}
