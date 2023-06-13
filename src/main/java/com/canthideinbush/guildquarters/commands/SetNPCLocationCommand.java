package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class SetNPCLocationCommand extends InternalCommand {



    @Override
    public boolean execute(Player sender, String[] args) {

        if (!GuildUtils.contains(QuartersManager.templateQuarter, sender.getLocation())) {
            sendConfigErrorMessage(sender, "common.outside-template");
            return false;
        }

        GuildQ.getInstance().getQuartersManager().setProxyNPCLocation(sender.getLocation().subtract(QuartersManager.templateQuarter.getInitialLocation()));

        sendConfigSuccessMessage(sender, getMessagePath("success"));


        GuildQ.getInstance().getQuartersManager().getObjects().forEach(
                GuildQuarter::initializeNPC
        );

        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Ustawiono pozycje npc!";

    @Override
    public String getName() {
        return "setnpcloc";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
