package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructure;
import com.canthideinbush.guildquarters.quarters.structures.QuarterStructures;
import com.canthideinbush.utils.commands.ABArgumentCompletion;
import com.canthideinbush.utils.commands.ABCompleter;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.TabCompleter;
import com.canthideinbush.utils.storing.ArgParser;
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

public class CollectCommand extends InternalCommand implements ABArgumentCompletion {


    /*


    /gq collect <struktura>

     */

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


        InventoryHolder target;

        if (!parser.hasNext()) {
            if (sender instanceof ConsoleCommandSender) {
                sendConfigErrorMessage(sender, "command-arguments-insufficient");
                return false;
            }
            target = (Player) sender;
        }
        else {

        }




        return true;
    }


    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public String getName() {
        return "collect";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

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
