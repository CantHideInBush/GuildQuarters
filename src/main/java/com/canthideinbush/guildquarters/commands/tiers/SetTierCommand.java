package com.canthideinbush.guildquarters.commands.tiers;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.quarters.QuartersParentCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetTierCommand extends InternalCommand {


    @Override
    public boolean execute(Player sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String shortId = parser.next();

        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(shortId)) == null) {
            sendConfigErrorMessage(sender, "guildq-quarter-shortId-nonexistent");
            return false;
        }


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

        quarter.setQuarterTier(tier);
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }


    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Ustawiono poziom siedziby";

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length == getArgIndex() + 1) {
            return GuildQ.getInstance().getQuartersManager().getShortIds();
        }
        else if (args.length == getArgIndex() + 2) {
            return Collections.singletonList("tier");
        }
        return Collections.emptyList();
    }
}
