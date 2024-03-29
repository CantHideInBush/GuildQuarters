package com.canthideinbush.guildquarters.commands.tiers;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterTier;
import com.canthideinbush.guildquarters.quarters.QuarterTiers;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.ABCompleter;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UpgradeCommand extends InternalCommand implements ABArgumentCompletion {

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

        if (!QuarterTiers.exists(tier.getIndex() + 1)) {
            sendConfigErrorMessage(sender, "common.tier-nonexistent");
            return false;
        }

        sendConfigSuccessMessage(sender, "command.tier.upgrade.success");

        return true;
    }



    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return TierParentCommand.class;
    }


    List<TabCompleter> completion = prepareCompletion();
    @ABCompleter(arg = "upgrade", index = 0)
    private List<String> completeQuarter() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }



    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }


}
