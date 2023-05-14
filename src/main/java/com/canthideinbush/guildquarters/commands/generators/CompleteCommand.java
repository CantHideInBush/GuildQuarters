package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.chat.ChatUtils;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CompleteCommand extends BuildCommand {


    public CompleteCommand(BuildParentCommand parentCommand) {
        super(parentCommand);
    }


    @Override
    public boolean execute(Player sender, String[] args) {
        if (!parentCommand.isBuilding(sender)) {
            sendConfigErrorMessage(sender, parentCommand.getSubCommand("with").
                    getMessagePath("not-building"));
            return false;
        }

        GeneratorBuilder builder = parentCommand.getGeneratorBuilder(sender);
        if (!builder.isComplete()) {
            StringBuilder stringBuilder = new StringBuilder(
                    GuildQ.getInstance().getUtilsProvider().getChatUtils()
                            .getMessage(getMessagePath("not-complete"))
            );
            stringBuilder.append(ChatColor.GOLD);
            for (String missing : builder.missingOptions()) {
                stringBuilder.append("\n- ").append(missing);
            }
            GuildQ.getInstance().getUtilsProvider().getChatUtils().sendMessage(sender, stringBuilder.toString(), ChatColor.RED);
            return false;
        }

        parentCommand.complete(sender);
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "not-complete")
    private static final String NOT_COMPLETE = "Brakujace opcje: ";

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Utworzono nowy generator!";

    @Override
    public String getName() {
        return "complete";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }
}
