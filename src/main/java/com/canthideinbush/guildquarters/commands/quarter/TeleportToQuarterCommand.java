package com.canthideinbush.guildquarters.commands.quarter;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeleportToQuarterCommand extends InternalCommand {
    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    @Override
    public String getPermission() {
        return "tp";
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return args.length - 1 == getArgIndex() ? GuildQ.getInstance().getQuartersManager().getShortIds() :
                args.length == getArgIndex() && sender.hasPermission(ADMIN_PERMISSION()) ?
                        Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()) :



                Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());


        GuildQuarter quarter;
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }
        else if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
            sendConfigErrorMessage(sender, "guildq-quarter-shortId-nonexistent");
            return false;
        }

        Player target;
        if (!parser.hasNext()) {
            if (!(sender instanceof Player)) {
                sendConfigErrorMessage(sender, "invalid-command-sender-console");
                return false;
            }
            target = (Player) sender;
        }
        else if (!sender.hasPermission(ADMIN_PERMISSION())) {
            sendConfigErrorMessage(sender, DefMessages.PERMISSION_INSUFFICIENT, ADMIN_PERMISSION());
            return false;
        }
        else {
            target = Bukkit.getPlayer(parser.next());
            if (target == null) {
                sendConfigErrorMessage(sender, getMessagePath("not-online"));
                return false;
            }
        }

        World w = GuildUtils.getGuildWorld();

        if (quarter.equals(QuartersManager.templateQuarter)) {
            target.teleport(w.getHighestBlockAt(quarter.getInitialLocation().getBlockX(), quarter.getInitialLocation().getBlockZ()).getLocation().add(0, 1, 0));
            return true;
        }




        target.teleport(w.getHighestBlockAt(quarter.getSpawnLocation().getBlockX(), quarter.getSpawnLocation().getBlockZ()).getLocation().add(0, 1, 0));

        sendConfigSuccessMessage(sender, "guildq-quarter-teleport-success");

        return true;
    }

    @DefaultConfigMessage(forN = "not-online")
    private static final String NOT_ONLINE = "Ten gracz nie jest online!";

    private String ADMIN_PERMISSION() {
        return getAbsolutePermission() + ".admin";
    }

    @Override
    protected List<Permission> getAdditionalPermissions() {
        Permission adminPermission = new Permission(ADMIN_PERMISSION());
        Bukkit.getPluginManager().getPermission(getAbsolutePermission()).addParent(adminPermission, true);
        return Collections.singletonList(adminPermission);
    }
}
