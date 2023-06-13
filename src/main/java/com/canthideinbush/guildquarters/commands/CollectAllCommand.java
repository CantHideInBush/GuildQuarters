package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CollectAllCommand extends InternalCommand implements ABArgumentCompletion {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String id = parser.next();
        if (!GuildQ.getInstance().getQuarterStructures().exists(id)) {
            sendConfigErrorMessage(sender, "common.structure-nonexistent");
            return false;
        }



        QuarterStructure structure;


        Player target;

        if (!parser.hasNext()) {
            if (sender instanceof ConsoleCommandSender) {
                sendConfigErrorMessage(sender, "command-arguments-insufficient");
                return false;
            }
            target = (Player) sender;
        }
        else if (!sender.hasPermission(ADMIN_PERMISSION())) {
            sendConfigErrorMessage(sender, "common.permissions-insufficient", ADMIN_PERMISSION());
            return false;
        }
        else {
            target = Bukkit.getPlayer(parser.next());
            if (target == null) {
                sendConfigErrorMessage(sender, getMessagePath("not-online"), parser.current());
                return false;
            }
        }

        Guild guild = Guilds.getApi().getGuild(target);
        if (guild == null) {

            if (sender.equals(target)) sendConfigErrorMessage(sender, "quildq-guilds-not_member");
            else sendConfigErrorMessage(sender, getMessagePath("not-member"));
            return false;
        }

        GuildQuarter quarter = GuildQ.getInstance().getQuartersManager().getByGuildId(guild.getId());
        if (quarter == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        if ((structure = quarter.getQuarterObjects().getStructure(id)) == null) {
            sendConfigErrorMessage(sender, "common.structure-not-built");
            return false;
        }
        StructureStorage storage = structure.getStorage();
        for (String itemId : storage.getAvailable()) {
            storage.take(itemId, target, storage.getAmount(itemId));
        }

        if (!target.equals(sender)) {
            sendConfigSuccessMessage(sender, getMessagePath("success-sender"));
        }
        sendConfigSuccessMessage(target, getMessagePath("success-target"));


        return true;
    }

    @DefaultConfigMessage(forN = "not-online")
    private static final String NOT_ONLINE = "Ten gracz nie jest online!";

    @DefaultConfigMessage(forN = "success-sender")
    private static final String SUCCESS_SENDER = "Przeznano zgromadzone zasoby!";

    @DefaultConfigMessage(forN = "success-target")
    private static final String SUCCESS_TARGET = "Otrzymano zgromadzone zasoby!";

    @Override
    public String getName() {
        return "collectall";
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        Player player;
        GuildQuarter quarter = null;
        boolean isAdmin = sender.hasPermission(ADMIN_PERMISSION());
        if (args.length >= getArgIndex() + 1) {
            if (!isAdmin && sender instanceof Player) {
                player = (Player) sender;
                quarter = GuildQ.getInstance().getQuartersManager().getByMember(player);
            }
        }
        if (args.length == getArgIndex() + 1) {
            if (isAdmin) {
                return GuildQ.getInstance().getQuarterStructures().getIds();
            }
            else if (quarter != null) {
                return quarter.getQuarterObjects().getStructureIds();
            }
        }

        if (isAdmin && args.length == getArgIndex() + 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }



        return Collections.emptyList();
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }

    private String ADMIN_PERMISSION() {
        return getAbsolutePermission() + ".admin";
    }

    @Override
    protected List<Permission> getAdditionalPermissions() {
        Permission admin = new Permission(ADMIN_PERMISSION());
        Bukkit.getPluginManager().getPermission(getAbsolutePermission()).addParent(admin, true);
        return Collections.singletonList(admin);
    }
}
