package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RemoveGenerator extends InternalCommand implements ABArgumentCompletion {
    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args,getArgIndex());
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

        QuarterStructure structure;
        if ((structure = quarter.getQuarterObjects().getStructure(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.structure-nonexistent");
            return false;
        }



        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        if (!structure.containsGenerator(parser.next())) {
            sendConfigErrorMessage(sender, getMessagePath("non-existent"));
            return false;
        }

        structure.removeGenerator(parser.current());
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "non-existent")
    private static final String NON_EXISTENT = "Struktura nie posiada tego generatora!";

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Usunieto generator!";

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
