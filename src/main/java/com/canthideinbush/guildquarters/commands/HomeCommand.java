package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomeCommand extends InternalCommand implements ABArgumentCompletion {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        GuildQuarter quarter;

        Player target;

        ArgParser parser = new ArgParser(args, getArgIndex());
        if (parser.hasNext()) {
            if (!hasPermission(sender, ADMIN_PERMISSION())) {
                sendConfigErrorMessage(sender, "common.permissions-insufficient", ADMIN_PERMISSION());
                return false;
            }
            target = Bukkit.getPlayer(parser.next());
            if (target == null) {
                sendConfigErrorMessage(sender, getMessagePath("not-online"));
                return false;
            }
        }
        else {
            if (sender instanceof Player) {
                target = (Player) sender;
            }
            else {
                sendConfigErrorMessage(sender, "command-arguments-insufficient");
                return false;
            }
        }

        if ((quarter = GuildQ.getInstance().getQuartersManager().getByMember(target)) != null) {
            if (quarter.isRemoved()) {
                sendConfigErrorMessage(sender, "common.quarter-removed");
                return false;
            }
            else if (!quarter.isPasted()) {
                sendConfigErrorMessage(sender, "common.quarter-not-pasted");
                return false;
            }
            target.teleport(quarter.getSpawnLocation());
            sendConfigSuccessMessage(target, getMessagePath("success"));
            if (!target.equals(sender)) {
                sendConfigSuccessMessage(sender, getMessagePath("sender-success"));
            }
            return true;
        }
        sendConfigErrorMessage(sender, getMessagePath("not-member"));

        return false;
    }

    @DefaultConfigMessage(forN = "success")
    private static String SUCCESS = "Przeteleportowano!";
    @DefaultConfigMessage(forN = "sender-success")
    private static String SENDER_SUCCESS = "Przeteleportowano cel!";

    @DefaultConfigMessage(forN = "not-member")
    private static String NOT_MEMBER = "Nie jestes czlonkiem zadnej siedziby!";

    @DefaultConfigMessage(forN = "not-online")
    private static String NOT_ONLINE = "Ten gracz nie jest online!";

    private String ADMIN_PERMISSION() {
        return getAbsolutePermission() + ".admin";
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

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    @ABCompleter(index = 0, localPermission = "admin")
    private List<String> completePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    private final List<TabCompleter> completion = prepareCompletion();
    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
