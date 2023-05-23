package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import me.glaremasters.guilds.Guilds;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;

public class SetSpawnLocationCommand extends InternalCommand implements ABArgumentCompletion {



    @Override
    public boolean execute(Player sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        GuildQuarter quarter;
        if (parser.hasNext()) {
            if (!sender.hasPermission(ADMIN_PERMISSION())) {
                sendConfigErrorMessage(sender, "common.permissions-insufficient", ADMIN_PERMISSION());
                return false;
            }
            if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
                sendConfigErrorMessage(sender, "common.quarter-nonexistent");
                return false;
            }
        }
        else if (Guilds.getApi().getGuild(sender) == null || !Guilds.getApi().getGuildRole(sender).isChangeHome()) {
            sendConfigErrorMessage(sender, getMessagePath("not-owner"));
            return false;
        }
        else {
            quarter = GuildQ.getInstance().getQuartersManager().getByGuildId(Guilds.getApi().getGuild(sender).getId());
        }


        quarter.setSpawnLocation(sender.getLocation());
        sendConfigSuccessMessage(sender, getMessagePath("success"));


        return true;
    }


    private String ADMIN_PERMISSION() {
        return getAbsolutePermission() + "." + "admin";
    }

    @DefaultConfigMessage(forN = "not-owner")
    private static final String NOT_OWNER = "Nie jestes wlascicielem gildii!";
    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Pomyslnie ustawiono spawn!";
    @ABCompleter(index = 0, arg = "setspawn", localPermission = "admin")
    private static List<String> QUARTERS_COMPLETE() {
        return GuildQ.getInstance().getQuartersManager().getShortIds();
    }

    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return QuartersParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    protected List<Permission> getAdditionalPermissions() {
        Permission admin = new Permission(ADMIN_PERMISSION());
        Bukkit.getPluginManager().getPermission(getAbsolutePermission()).addParent(admin, true);
        return Collections.singletonList(admin);
    }

    List<TabCompleter> completion = prepareCompletion();
    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
