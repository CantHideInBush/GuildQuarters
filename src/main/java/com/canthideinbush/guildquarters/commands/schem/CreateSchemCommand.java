package com.canthideinbush.guildquarters.commands.schem;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateSchemCommand extends InternalCommand {

    @Override
    public boolean execute(Player sender, String[] args) {

        ArgParser parser = new ArgParser(args, getArgIndex());
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String name = parser.next();


        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String schematic = parser.next();

        if (GuildQ.getInstance().getQuarterSchematics().addSchematic(name, schematic, sender.getLocation().subtract(QuartersManager.templateQuarter.getInitialLocation()).toVector())) {
            sendConfigSuccessMessage(sender, "command.tier.structure.create.success");
        }
        else {
            sendConfigErrorMessage(sender, getMessagePath("name-taken"));
        }


        return true;
    }

    @DefaultConfigMessage(forN = "name-taken")
    private static final String NAME_TAKEN = "Ta nazwa jest juz zajeta!";

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return SchemParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length == getArgIndex() + 1) {
            return Collections.singletonList("name");
        }
        else if (args.length == getArgIndex() + 2) {
            return Collections.singletonList("schematic");
        }
        return Collections.emptyList();
    }
}
