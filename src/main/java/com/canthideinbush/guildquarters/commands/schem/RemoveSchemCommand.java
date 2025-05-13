package com.canthideinbush.guildquarters.commands.schem;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class RemoveSchemCommand extends InternalCommand {

    @Override
    public boolean execute(Player sender, String[] args) {

        ArgParser parser = new ArgParser(args, getArgIndex());
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String name = parser.next();

        if (GuildQ.getInstance().getQuarterSchematics().removeSchematic(name)) {
            sendConfigSuccessMessage(sender, "command.tier.structure.remove.success");
        }
        else {
            sendConfigErrorMessage(sender, getMessagePath("non-existent"));
        }

        return true;
    }

    @DefaultConfigMessage(forN = "non-existent")
    private static final String NON_EXISTENT = "Schemat o podanej nazwie nie istnieje!";

    @Override
    public String getName() {
        return "remove";
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
        return Collections.emptyList();
    }
}
