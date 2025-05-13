package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TemplateQuarterTeleportCommand extends InternalCommand {

    @Override
    public boolean execute(Player sender, String[] args) {
        if (QuartersManager.templateQuarter == null) {
            sendConfigErrorMessage(sender, getMessagePath("failure"));
            return false;
        }
        sender.teleport(QuartersManager.templateQuarter.getInitialLocation());
        return true;
    }

    @DefaultConfigMessage(forN = "failure")
    private static final String FAILURE = "Dzialka &eTemplateQuarter" + ChatColor.RED + " nie istnieje!";

    @Override
    public String getName() {
        return "template";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args) {
        return Collections.emptyList();
    }
}
