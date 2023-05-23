package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.entity.Player;

public class HomeCommand extends InternalCommand {

    @Override
    public boolean execute(Player sender, String[] args) {
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByMember(sender)) != null) {
            sender.teleport(quarter.getSpawnLocation());
            sendConfigSuccessMessage(sender, getMessagePath("success"));
            return true;
        }
        sendConfigErrorMessage(sender, getMessagePath("not-member"));

        return false;
    }

    @DefaultConfigMessage(forN = "success")
    private static String SUCCESS = "Przeteleportowano!";

    @DefaultConfigMessage(forN = "not-member")
    private static String NOT_MEMBER = "Nie jestes czlonkiem zadnej siedziby!";

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
