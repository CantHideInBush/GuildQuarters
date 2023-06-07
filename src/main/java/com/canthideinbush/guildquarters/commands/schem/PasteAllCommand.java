package com.canthideinbush.guildquarters.commands.schem;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.ConfirmCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.quarters.schematics.QuarterSchematic;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.function.Consumer;

public class PasteAllCommand extends InternalCommand {


    @Override
    public boolean execute(ConsoleCommandSender sender, String[] args) {

        QuarterSchematic schematic;
        if ((schematic = GuildQ.getInstance().getQuarterSchematics().getByName(args[getArgIndex()])) == null) {
            sendConfigErrorMessage(sender, "common.schematic-nonexistent");
            return false;
        }

        ConfirmCommand.sendWarning(sender);
        ConfirmCommand.actionFor(sender, (s) -> {
            for (GuildQuarter q : GuildQ.getInstance().getQuartersManager().getObjects()) {
                schematic.paste(q);
            }
            sendConfigSuccessMessage(sender, getMessagePath("success"));
        });


        return true;
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Wklejono schemat na wszystkich działkach!";

    @Override
    public String getName() {
        return "pasteall";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return SchemParentCommand.class;
    }
}
