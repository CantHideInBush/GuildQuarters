package com.canthideinbush.guildquarters.commands.spawner;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.spawners.MMSpawner;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RemoveAllCommand extends InternalCommand {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        MMSpawner spawner;
        if ((spawner = MMSpawner.findByName(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.spawner-nonexistent");
            return false;
        }

        for (GuildQuarter quarter : GuildQ.getInstance().getQuartersManager().getObjects()) {
            quarter.getQuarterObjects().removeSpawner(spawner);
        }

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;

    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length - 1 == getArgIndex()) {
            return MMSpawner.getIds();
        }
        return Collections.emptyList();
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Pomyslnie usunieto spawner dla wszystkich dzialek!";


    @Override
    public String getName() {
        return "removeall";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return SpawnerParentCommand.class;
    }
}
