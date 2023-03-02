package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.CHIBCommandsRegistry;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateQuarterCommand extends InternalCommand {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public String getPermission() {
        return "create";
    }

    @Override
    public List<String> complete(String[] args) {
        if (args.length - 1 == getArgIndex()) {
            return Collections.singletonList(" ");
        }
        return Collections.emptyList();
    }


    @Override
    public boolean execute(Player sender, String[] args) {
        //ArgParser parser = new ArgParser(args, getArgIndex());

        Guild guild = Guilds.getApi().getGuild(sender);
        if (guild == null) {
            sendConfigErrorMessage(sender, "guildq-guilds-not_member");
            return false;
        }
        else if (!guild.getGuildMaster().getAsOfflinePlayer().equals(sender)) {
            sendConfigErrorMessage(sender, "guildq-guilds-not_master");
            return false;
        }

        if (!GuildQ.getInstance().getQuartersManager().createGuildQuarter(guild)) {
            sendConfigErrorMessage(sender, "guildq-guilds-create-exists");
            return false;
        }

        sendConfigSuccessMessage(sender, "guildq-quarter-create-success");

        return true;
    }
}
