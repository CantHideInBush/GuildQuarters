package com.canthideinbush.guildquarters.commands.tiers.structure;

import com.canthideinbush.guildquarters.quarters.QuarterTiers;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class RemoveStructureCommand extends InternalCommand {

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

        String name = parser.next();

        if (QuarterTiers.removeStructure(tier, name)) {
            sendConfigSuccessMessage(sender, "command.tier.structure.remove.success");
        }
        else {
            sendConfigErrorMessage(sender, getMessagePath("non-existent"));
        }

        return true;
    }

    @DefaultConfigMessage(forN = "non-existent")
    private static final String NON_EXISTENT = "Struktura o podanej nazwie nie istnieje!";

    @Override
    public String getName() {
        return "remove";
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
            return Collections.singletonList("name");
        }
        return Collections.emptyList();
    }
}
