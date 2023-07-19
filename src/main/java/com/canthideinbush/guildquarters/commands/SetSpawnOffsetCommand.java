package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.schem.CreateSchemCommand;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class SetSpawnOffsetCommand extends InternalCommand {

    private final Consumer<CommandSender> consumer = (s) -> {
        Location loc = ((Player) s).getLocation();
        Vector offset = loc.subtract(QuartersManager.templateQuarter.getInitialLocation()).toVector();
        GuildQ.getInstance().getConfig().set("Quarters.SpawnOffset", offset);
        sendConfigSuccessMessage(s, getMessagePath("success"));

    };
    @Override
    public boolean execute(Player sender, String[] args) {

        if (QuartersManager.templateQuarter == null) {
            sendConfigErrorMessage(sender, "common.template-quarter-nonexistent");
            return false;
        }

        if (!GuildUtils.contains(QuartersManager.templateQuarter, sender.getLocation())) {
            sendConfigErrorMessage(sender, getMessagePath("warning"));
            ConfirmCommand.actionFor(sender, consumer);
            return true;
        }

        consumer.accept(sender);
        return true;
    }

    @DefaultConfigMessage(forN = "warning")
    private static final String WARNING = CreateSchemCommand.WARNING;
    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Ustawiono  przesuniecie!";

    @Override
    public String getName() {
        return "setspawnoffset";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }
}
