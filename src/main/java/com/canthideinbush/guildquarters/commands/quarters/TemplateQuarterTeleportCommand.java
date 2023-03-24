package com.canthideinbush.guildquarters.commands.quarters;

import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TemplateQuarterTeleportCommand extends InternalCommand {

    @Override
    public boolean execute(Player sender, String[] args) {
        sender.teleport(QuartersManager.templateQuarter.getInitialLocation());
        return true;
    }

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
