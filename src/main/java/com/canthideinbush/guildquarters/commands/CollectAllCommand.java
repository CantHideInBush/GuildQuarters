package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.itemgenerators.StructureStorage;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;

public class CollectAllCommand extends InternalCommand implements ABArgumentCompletion {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByShortId(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.quarter-nonexistent");
            return false;
        }

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        QuarterStructure structure;
        if ((structure = quarter.getQuarterObjects().getStructure(parser.next())) == null) {
            sendConfigErrorMessage(sender, "common.structure-not-built");
            return false;
        }


        if (sender instanceof ConsoleCommandSender) {
            sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
            return false;
        }

        Player target = null;
        if (parser.hasNext()) {
            if (!sender.hasPermission(ADMIN_PERMISSION())) {
                sendConfigErrorMessage(sender, DefMessages.ARGS_INSUFFICIENT);
                return false;
            }
            target = Bukkit.getPlayer(parser.next());
        }

        if (target == null) {
            sendConfigErrorMessage(sender, getMessagePath("not-online"));
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
