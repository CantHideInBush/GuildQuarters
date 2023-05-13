package com.canthideinbush.guildquarters.commands.tiers;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterTier;
import com.canthideinbush.guildquarters.quarters.QuarterTiers;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

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

        if (tier.getIndex() - 1 < 0) {
            sendConfigErrorMessage(sender, getMessagePath("non-zero"));
            return false;
        }

        sendConfigSuccessMessage(sender, "command.tier.downgrade.success");

        return true;
    }

    @DefaultConfigMessage(forN = "non-zero")
    private static final String NON_ZERO = "Nie mozna obnizyc poziomu ponizej zera!";

    @Override
    public String getName() {
        return "downgrade";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return TierParentCommand.class;
    }



    List<TabCompleter> completion = prepareCompletion();
    @ABCompleter(arg = "downgrade", index = 0)
    private List<String> completeQuarter() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }
}
