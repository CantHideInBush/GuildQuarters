package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.commands.CHIBCommandsRegistry;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TeleportToQuarterCommand extends InternalCommand {
    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    @Override
    public String getPermission() {
        return "tp";
    }

    @Override
    public List<String> complete(String[] args) {
        return args.length - 1 == getArgIndex() ? GuildQ.getInstance().getQuartersManager().getShortIds() : Collections.emptyList();
    }

    @Override
    public boolean execute(Player sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());


        GuildQuarter quarter;
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }
        else if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
            sendConfigErrorMessage(sender, "guildq-quarter-shortId-nonexistent");
            return false;
        }

        World w = GuildUtils.getGuildWorld();

        sender.teleport(w.getHighestBlockAt(quarter.getSpawnLocation().getBlockX(), quarter.getSpawnLocation().getBlockZ()).getLocation().add(0, 1, 0));

        sendConfigSuccessMessage(sender, "guildq-quarter-teleport-success");

        return true;
    }
}
