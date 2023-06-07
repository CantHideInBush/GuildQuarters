package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructures;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CollectCommand extends InternalCommand {




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


        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        GeneratorItem item;
        if ((item = GeneratorItem.get(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.generator-item-nonexistent");
            return false;
        }


        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        int amount;
        try {
            amount = parser.nextInt();
        } catch (NumberFormatException e) {
            sendConfigErrorMessage(sender, "incorrect_data_type");
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

        structure.getStorage().take(item.getId(), target, amount);



        return true;
    }

    @DefaultConfigMessage(forN = "not-member")
    private static final String NOT_IN_GUILD = "Cel nie znajduje sie w zadnej gildii!";

    @DefaultConfigMessage(forN = "not-online")
    private static final String NOT_ONLINE = "Gracz %s nie jest online!";

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        Player player = null;
        GuildQuarter quarter = null;
        QuarterStructure structure = null;
        GeneratorItem item = null;
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

        if (!isAdmin && args.length >= getArgIndex() + 2) {
            quarter = GuildQ.getInstance().getQuartersManager().getByMember(player);
            if (quarter != null) {
                structure = quarter.getQuarterObjects().getStructure(args[getArgIndex()]);
            }
        }

        if (args.length == getArgIndex() + 2) {
            if (isAdmin) {
                return GeneratorItem.getIds();
            }
            if (structure != null) {
                return structure.getStorage().getAvailable();
            }
        }

        if (!isAdmin && args.length >= getArgIndex() + 2) {
            item = GeneratorItem.get(args[getArgIndex() + 1]);
        }
        if (args.length == getArgIndex() + 3) {
            if (isAdmin) return Collections.singletonList("0");
            if (structure != null && item != null) {
                return Collections.singletonList(structure.getStorage().getAmount(item.getId()) + "");
            }
        }

        if (isAdmin && args.length == getArgIndex() + 4) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }



        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "collect";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
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
