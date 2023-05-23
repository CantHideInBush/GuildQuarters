package com.canthideinbush.guildquarters.commands.quarter.region;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.quarter.QuartersParentCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetExpansion extends InternalCommand implements ABArgumentCompletion {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, getMessagePath("command-arguments-insufficient"));
            return false;
        }

        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
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
            sendConfigErrorMessage(sender, getMessagePath("direction-nonexistent"));
            return false;
        }

        sendConfigSuccessMessage(sender, getMessagePath("success"), quarter.getRegion().getExpansion(face));


        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Ekpsansja siedziby: %s";

    @DefaultConfigMessage(forN = "direction-nonexistent")
    private static final String DIR_NONEXISTENT = "Podany kierunek nie istnieje!";

    @ABCompleter(index = 0)
    private List<String> completeQuarters() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }
    @ABCompleter(index = 1)
    private List<String> completeDirections() {
        return Arrays.stream(BlockFace.values()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "getexpansion";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    private final List<TabCompleter> completer = prepareCompletion();


    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completer;
    }
}
