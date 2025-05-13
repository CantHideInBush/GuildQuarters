package com.canthideinbush.guildquarters.commands.region;

import com.canthideinbush.guildquarters.quarters.updating.RegionUpdateOperation;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

public class UpdateAllCommand extends InternalCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        RegionUpdateOperation.updateAll();
        sendConfigSuccessMessage(sender, getMessagePath("success"));
        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Rozpoczynam aktualizacje regionów!";

    @Override
    public String getName() {
        return "updateall";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return RegionParentCommand.class;
    }
}
