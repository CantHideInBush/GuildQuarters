package com.canthideinbush.guildquarters.commands;

import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.commands.action.ConfirmActionCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ConfirmCommand extends ConfirmActionCommand {
    private final HashMap<CommandSender, Consumer<CommandSender>> actionMap = new HashMap<>();
    private final HashMap<CommandSender, Long> timeMap = new HashMap<>();


    private static ConfirmCommand instance;
    public ConfirmCommand() {
        super();
        instance = this;
    }

    @Override
    protected HashMap<CommandSender, Consumer<CommandSender>> getActionMap() {
        return actionMap;
    }

    @DefaultConfigMessage(forN = "warning")
    private static final String WARNING = "Czy na pewno chcesz wykonac te akcje? Wpisz /gq confirm aby potwierdzic.";

    public static void sendWarning(CommandSender sender) {
        instance.sendConfigErrorMessage(sender, instance.getMessagePath("warning"));
    }

    @Override
    protected HashMap<CommandSender, Long> getTimeMap() {
        return timeMap;
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return MainCommand.class;
    }

    public static void actionFor(CommandSender sender, Consumer<CommandSender> consumer) {
        instance.setAction(sender, consumer);
    }

    @Override
    protected List<String> getLabels() {
        return Collections.singletonList("c");
    }
}
