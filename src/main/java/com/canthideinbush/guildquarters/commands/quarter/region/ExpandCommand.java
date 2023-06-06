package com.canthideinbush.guildquarters.commands.quarter.region;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.quarter.QuartersParentCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuarterRegion;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExpandCommand extends InternalCommand implements ABArgumentCompletion {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String quarterId = parser.next();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(quarterId)) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        BlockFace face;
        try {
            face = BlockFace.valueOf(parser.next().toUpperCase());
        } catch (IllegalArgumentException e) {
            sendConfigErrorMessage(sender, getMessagePath("no-such-direction"));
            return false;
        }


        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }
        int distance;
        try {
            distance = parser.nextInt();
        } catch (NumberFormatException e) {
            sendConfigErrorMessage(sender, "incorrect_data_type");
            return false;
        }

        if (distance <= 0) {
            sendConfigErrorMessage(sender, "must-be-positive");
            return false;
        }

        quarter.getRegion().expand(face, distance);


        sendConfigSuccessMessage(sender, getMessagePath("success"));


        return true;
    }


    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Rozszerzono region!";

    @DefaultConfigMessage(forN = "no-such-direction")
    private static final String NO_SUCH_DIRECTION = "Wprowadzono bledny kierunek!";

    @ABCompleter(index = 0)
    private List<String> completeQuarter() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @ABCompleter(index = 1)
    private List<String> completeDirections() {
        return Arrays.stream(BlockFace.values()).map(Enum::name).collect(Collectors.toList());
    }

    @ABCompleter(index = 2)
    private final int completeDistance = 0;

    @Override
    public String getName() {
        return "expand";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }
    private final List<TabCompleter> completion = prepareCompletion();
    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
