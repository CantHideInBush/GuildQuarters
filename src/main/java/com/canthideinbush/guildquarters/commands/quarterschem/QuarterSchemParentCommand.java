package com.canthideinbush.guildquarters.commands.quarterschem;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.MainCommand;
import com.canthideinbush.guildquarters.quarters.updating.UpdateQuarterSchematicOperation;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.ParentCommand;
import com.sk89q.worldedit.function.operation.OperationQueue;
import org.bukkit.command.CommandSender;

public class QuarterSchemParentCommand extends ParentCommand {

    public QuarterSchemParentCommand() {
        subCommands.add(new UpdateAllCommand());
        subCommands.add(new GetProgressCommand());
    }

    @Override
    public String getName() {
        return "quarterschem";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    private static class UpdateAllCommand extends InternalCommand {
        @Override
        public boolean execute(CommandSender sender, String[] args) {

            UpdateQuarterSchematicOperation.updateAll();

            sendConfigSuccessMessage(sender, getMessagePath("success"));

            return true;
        }

        @Override
        public String getName() {
            return "updateall";
        }

        @Override
        public Class<? extends InternalCommand> getParentCommandClass() {
            return QuarterSchemParentCommand.class;
        }

        @DefaultConfigMessage(forN = "success")
        private static final String SUCCESS = "Rozpoczeto aktualizacje wszystkich dzialek!";
    }


    private static class GetProgressCommand extends InternalCommand {

        @Override
        public boolean execute(CommandSender sender, String[] args) {

            sendConfigSuccessMessage(sender, getMessagePath("success"), GuildQ.getInstance().getQuartersUpdateQueue().getUpdateOperationsOfType(UpdateQuarterSchematicOperation.class).size());

            return true;
        }

        @DefaultConfigMessage(forN = "success")
        private static final String SUCCESS = "Pozostalo operacji: %s";

        @Override
        public String getName() {
            return "getprogress";
        }

        @Override
        public Class<? extends InternalCommand> getParentCommandClass() {
            return QuarterSchemParentCommand.class;
        }
    }
}
