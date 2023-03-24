package com.canthideinbush.guildquarters.commands.tiers.structure;

import com.canthideinbush.guildquarters.quarters.QuarterTiers;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class CreateStructureCommand extends InternalCommand {

    @Override
    public boolean execute(Player sender, String[] args) {

        ArgParser parser = new ArgParser(args, getArgIndex());
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        int tier;
        try {
            tier = parser.nextInt();
        } catch (NumberFormatException e) {
            sendConfigErrorMessage(sender, "incorrect_data_type");
            return false;
        }

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String schematic = parser.next();

        QuarterTiers.addStructure(tier, schematic, sender.getLocation().subtract(QuartersManager.templateQuarter.getInitialLocation()).toVector());
        sendConfigSuccessMessage(sender, "command.tier.structure.create.success");

        return true;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return StructureParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length == getArgIndex() + 1) {
            return Collections.singletonList("tier");
        }
        else if (args.length == getArgIndex() + 2) {
            return Collections.singletonList("schematic");
        }
        return Collections.emptyList();
    }
}
