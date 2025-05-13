package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PasteSchemCommand extends InternalCommand implements ABArgumentCompletion {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String quarterId = parser.next();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(quarterId)) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String schemName = parser.next();
        QuarterSchematic schematic;
        if ((schematic = GuildQ.getInstance().getQuarterSchematics().getByName(schemName)) == null) {
            sendConfigErrorMessage(sender, "common.schematic-nonexistent");
            return false;
        }

        quarter.getQuarterObjects().placeSchematic(schematic);
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Wklejono schemat!";

    @ABCompleter(index = 0)
    private static List<String> completeQuarters() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @ABCompleter(index = 1)
    private static List<String> completeSchematics() {
        return GuildQ.getInstance().getQuarterSchematics().getNames();
    }

    @Override
    public String getName() {
        return "paste";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    List<TabCompleter> completion = prepareCompletion();

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }
}
