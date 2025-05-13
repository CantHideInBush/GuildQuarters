package com.canthideinbush.guildquarters.commands.structure;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StructureRemoveCommand extends InternalCommand implements ABArgumentCompletion {
    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        GuildQ.getInstance().getQuarterStructures().removeStructure(parser.next());


        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Usunieto strukture!";

    @ABCompleter(index = 0)
    private List<String> completeStructures() {
        return GuildQ.getInstance().getQuarterStructures().getIds();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return StructureParentCommand.class;
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
