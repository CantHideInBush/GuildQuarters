package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RemoveStructure extends InternalCommand implements ABArgumentCompletion {
    private final List<TabCompleter> completion = prepareCompletion();

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


        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        if (!quarter.getQuarterObjects().hasStructure(parser.current())) {
            sendConfigErrorMessage(sender, getMessagePath("doesnt-exists"));
            return false;
        }

        quarter.getQuarterObjects().removeStructure(parser.current());

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "doesnt-exists")
    private static final String ALR_EXISTS = "Ta struktura nie jest dodana do tej siedziby!";

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Usunieto strukture!";



    @Override
    public String getName() {
        return "removestructure";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @ABCompleter(index = 0)
    private List<String> completeQuarters() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @ABCompleter(index = 1)
    private List<String> completeStructures() {
        return GuildQ.getInstance().getQuarterStructures().getIds();
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
