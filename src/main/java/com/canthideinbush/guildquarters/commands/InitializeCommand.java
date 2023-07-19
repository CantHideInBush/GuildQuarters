package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InitializeCommand extends InternalCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (GuildQ.getInstance().getQuartersManager().initialize()) {
            sendConfigSuccessMessage(sender, getMessagePath("success"));
            return true;
        }
        else {
            sendConfigErrorMessage(sender, getMessagePath("failure"), GuildUtils.getSchematicName());
            return false;
        }

    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Inicjalizowano plugin!";
    @DefaultConfigMessage(forN = "failure")
    private static final String FAILURE = "Nie mozna inizjalizowac pluginu! Upewnij sie ze schematic &e%s " + ChatColor.RED + "istnieje!";

    @Override
    public String getName() {
        return "initialize";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
