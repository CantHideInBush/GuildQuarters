package com.canthideinbush.guildquarters.commands.quarter.spawner;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.quarter.QuartersParentCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawner;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AddSpawner extends InternalCommand implements ABArgumentCompletion {



    @Override
    public boolean execute(CommandSender sender, String[] args) {

        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        MMSpawner spawner;
        if ((spawner = MMSpawner.findByName(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.spawner-nonexistent");
            return false;
        }

        quarter.getQuarterObjects().placeSpawner(spawner);

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }



    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Pomyslnie dodano spawner!";

    @Override
    public String getName() {
        return "addspawner";
    }

    @ABCompleter(index = 0)
    private List<String> completeQuarters() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @ABCompleter(index = 1)
    private List<String> completeSpawners() {
        return MMSpawner.getIds();
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
