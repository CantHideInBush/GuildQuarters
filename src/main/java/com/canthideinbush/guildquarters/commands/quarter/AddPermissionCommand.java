package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.CommandUtility;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AddPermissionCommand extends InternalCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgsLength(args, 2, sender)) return false;

        String quarterName = args[getArgIndex()];

        GuildQuarter quarter;
        if ((quarter = CommandUtility.getQuarter(quarterName, sender, this)) == null) return false;

        String permission = args[getArgIndex() + 1].toLowerCase();

        GuildQ.getInstance().getGuildPermissions().registerPermission(permission);

        quarter.getSharedPermissions().add(permission);

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        quarter.syncSharedPermissions();

        return true;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        if (args.length == getArgIndex() + 1) {
            return GuildQ.getInstance().getQuartersManager().getShortIds();
        }
        else if (args.length == getArgIndex() + 2) {
            return Collections.singletonList(" ");
        }

        return super.complete(args, sender);
    }

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Pomyslnie dodano uprawnienie!";

    @Override
    public String getName() {
        return "addpermission";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }
}
