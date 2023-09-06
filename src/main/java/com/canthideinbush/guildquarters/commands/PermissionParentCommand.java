package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;
import org.bukkit.command.CommandSender;

public class PermissionParentCommand extends ParentCommand {

    public PermissionParentCommand() {
        subCommands.add(new RegisterPermissionCommand());
        subCommands.add(new UnregisterPermissionCommand());
    }

    @Override
    public String getName() {
        return "permissions";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }


    private static class RegisterPermissionCommand extends InternalCommand {

        @Override
        public boolean execute(CommandSender sender, String[] args) {
            if (!checkArgsLength(args, 1, sender)) return false;

            String permission = args[getArgIndex()];

            if (!GuildQ.getInstance().getGuildPermissions().registerPermission(permission.toLowerCase())) {
                sendConfigErrorMessage(sender, getMessagePath("failure"));
                return false;
            }

            sendConfigSuccessMessage(sender, getMessagePath("success"));

            return true;
        }

        @DefaultConfigMessage(forN = "failure")
        private static final String FAILURE = "To uprawnienie jest juz zarejestrowane!";

        @DefaultConfigMessage(forN = "success")
        public static final String SUCCESS = "Zarejestrowano uprawnienie!";

        @Override
        public String getName() {
            return "register";
        }

        @Override
        public Class<? extends InternalCommand> getParentCommandClass() {
            return PermissionParentCommand.class;
        }



    }

    private static class UnregisterPermissionCommand extends InternalCommand {

        @Override
        public boolean execute(CommandSender sender, String[] args) {
            if (!checkArgsLength(args, 1, sender)) return false;

            String permission = args[getArgIndex()];

            if (!GuildQ.getInstance().getGuildPermissions().unregisterPermission(permission.toLowerCase())) {
                sendConfigErrorMessage(sender, getMessagePath("failure"));
                return false;
            }

            sendConfigSuccessMessage(sender, getMessagePath("success"));

            return true;
        }

        @DefaultConfigMessage(forN = "failure")
        private static final String FAILURE = "To uprawnienie nie jest zarejestrowane!";

        @DefaultConfigMessage(forN = "success")
        public static final String SUCCESS = "Odrejestrowano uprawnienie!";


        @Override
        public String getName() {
            return "unregister";
        }

        @Override
        public Class<? extends InternalCommand> getParentCommandClass() {
            return PermissionParentCommand.class;
        }
    }
}
