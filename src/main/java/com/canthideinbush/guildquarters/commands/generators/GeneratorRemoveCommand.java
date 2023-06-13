package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerators;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GeneratorRemoveCommand extends InternalCommand implements ABArgumentCompletion {


    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length <= getArgIndex()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        ItemGenerator generator;
        if ((generator = GuildQ.getInstance().getItemGenerators().get(args[getArgIndex()])) == null) {
            sendConfigErrorMessage(sender, "common.generator-nonexistent");
            return false;
        }

        GuildQ.getInstance().getItemGenerators().unregister(generator);
        sendConfigErrorMessage(sender, getMessagePath("success"));


        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Usunieto generator!";

    @ABCompleter(index = 0)
    private List<String> completeGenerators() {
        return GuildQ.getInstance().getItemGenerators().getIds();
    }

    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return GeneratorParentCommand.class;
    }
}
