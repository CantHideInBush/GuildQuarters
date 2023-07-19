package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.command.CommandSender;

public class AdjustChunkSizeCommand extends InternalCommand  {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Clipboard clipboard;
        if ((clipboard = GuildQ.getInstance().getUtilsProvider().worldEdit.findByName(GuildUtils.getSchematicName())) == null) {
            sendConfigErrorMessage(sender, getMessagePath("clipboard-nonexistent"));
            return false;
        }

        int xLength = clipboard.getDimensions().getX();
        int zLength = clipboard.getDimensions().getZ();

        int suggested = (int) Math.max(Math.ceil(xLength / 16.0), Math.ceil(zLength / 16.0));

        sendConfigSuccessMessage(sender, getMessagePath("confirm"), suggested);

        ConfirmCommand.actionFor(sender, s -> {
            GuildQ.getInstance().getConfig().set("Quarters.ChunkSize", suggested);
            sendConfigSuccessMessage(sender, getMessagePath("success"), suggested);
            GuildQ.getInstance().getConfig().save();
        });


        return true;
    }

    @DefaultConfigMessage(forN = "clipboard-nonexistent")
    private static final String a = "Ten schemat nie istnieje!";
    @DefaultConfigMessage(forN = "confirm")
    private static final String b = "Sugerowany rozmiar: %s - potwierdz akcje za pomoca /gq confirm";
    @DefaultConfigMessage(forN = "success")
    private static final String c = "Zmieniono rozmiar na %s!";


    @Override
    public String getName() {
        return "adjustsize";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }



}
