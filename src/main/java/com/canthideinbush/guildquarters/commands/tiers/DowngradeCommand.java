package com.canthideinbush.guildquarters.commands.tiers;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterTier;
import com.canthideinbush.guildquarters.quarters.QuarterTiers;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.ABCompleter;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DowngradeCommand extends InternalCommand implements ABArgumentCompletion {

    @Override
    public boolean execute(Player sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.guild-nonexistent");
            return false;
        }

        QuarterTier tier = quarter.getTier();

        if (!QuarterTiers.exists(tier.getIndex() - 1)) {
            sendConfigErrorMessage(sender, "common.tier-nonexistent");
            return false;
        }

        quarter.downgrade();

        sendConfigSuccessMessage(sender, "command.tier.downgrade.success");

        return true;
    }

    @Override
    public String getName() {
        return "downgrade";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return TierParentCommand.class;
    }



    List<HashMap<String, Method>> completion = prepareCompletion();
    @ABCompleter(arg = "downgrade", index = 0)
    private List<String> completeQuarter() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @Override
    public List<HashMap<String, Method>> getCompletion() {
        return completion;
    }

    @Override
    public List<String> complete(String[] args) {
        return ABComplete(args);
    }
}
