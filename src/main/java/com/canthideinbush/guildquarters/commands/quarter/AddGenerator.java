package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AddGenerator extends InternalCommand implements ABArgumentCompletion {


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

        ItemGenerator generator;
        if ((generator = GuildQ.getInstance().getItemGenerators().get(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.generator-nonexistent");
            return false;
        }

        if (structure.containsGenerator(generator.getId())) {
            sendConfigErrorMessage(sender, getMessagePath("already-contains"));
            return false;
        }

        structure.addGenerator(generator);
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @ABCompleter(index = 0)
    private static List<String> completeQuarter() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @ABCompleter(index = 1)
    private List<String> completeStructures(String[] args) {
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(args[getArgIndex() - 1])) != null) {
            return quarter.getQuarterObjects().getStructureIds();
        }
        else return Collections.emptyList();
    }

    @ABCompleter(index = 2)
    private List<String> completeGenerators() {
        return GuildQ.getInstance().getItemGenerators().getIds();
    }


    @DefaultConfigMessage(forN = "already-contains")
    private static final String ALREADY_CONTAINS = "Ten generator jest już dodany!";

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Dodano generator!";

    @Override
    public String getName() {
        return "addgenerator";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }
}
