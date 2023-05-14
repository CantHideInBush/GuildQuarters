package com.canthideinbush.guildquarters.commands.generators;

import com.canthideinbush.guildquarters.quarters.itemgenerators.building.GeneratorBuilder;
import com.canthideinbush.utils.Reflector;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StartCommand extends BuildCommand {
    public StartCommand(BuildParentCommand parentCommand) {
        super(parentCommand);
    }

    public static HashMap<String, Class<? extends GeneratorBuilder>> builders = new HashMap<>();

    @Override
    public boolean execute(Player sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String builderName = parser.next();
        GeneratorBuilder builder = fromName(builderName);
        if (builder == null) {
            sendConfigErrorMessage(sender, getMessagePath("unknown-builder"));
            return false;
        }

        parentCommand.setGeneratorBuilder(sender, builder);
        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }

    @DefaultConfigMessage(forN = "unknown-builder")
    private static final String UNKNOWN_BUILDER = "Nie mozna odnalezc tego typu generatora!";

    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Rozpoczeto tworzenie generatora!";

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        if (args.length == getArgIndex() + 1) {
            return new ArrayList<>(builders.keySet());
        }
        return Collections.emptyList();
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return BuildParentCommand.class;
    }

    public static GeneratorBuilder fromName(String name) {
        if (!builders.containsKey(name)) return null;
        return Reflector.newInstance(builders.get(name), new Class[]{});
    }
}
