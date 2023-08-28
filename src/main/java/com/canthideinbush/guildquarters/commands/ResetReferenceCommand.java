package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

public class ResetReferenceCommand extends InternalCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        GuildQ.getInstance().getQuartersManager().referenceTable = null;
        sendConfigSuccessMessage(sender, getMessagePath("success"));
        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Pomyslnie zresetowano tablice referencji!";

    @Override
    public String getName() {
        return "resetreference";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
