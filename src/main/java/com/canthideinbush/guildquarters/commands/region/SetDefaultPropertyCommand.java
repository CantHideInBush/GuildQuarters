package com.canthideinbush.guildquarters.commands.region;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.QuarterRegion;
import com.canthideinbush.guildquarters.quarters.updating.RegionDefaultSizeUpdateOperation;
import com.canthideinbush.guildquarters.utils.Utils;
import com.canthideinbush.utils.commands.*;
import com.canthideinbush.utils.storing.ArgParser;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SetDefaultPropertyCommand extends com.canthideinbush.utils.commands.InternalCommand implements ABArgumentCompletion {

    private final List<TabCompleter> completion = prepareCompletion();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ArgParser parser = new ArgParser(args, getArgIndex());
        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String key = parser.next();

        if (!parser.hasNext()) {
            sendConfigErrorMessage(sender, "command-arguments-insufficient");
            return false;
        }

        String value = parser.next();

        ConfigurationSection section = GuildQ.getInstance().getConfig().getConfigurationSection("Quarters.region");
        if (section == null) {
            section = GuildQ.getInstance().getConfig().createSection("Quarters.region");
        }

        if (key.contains(".")) {
            sendConfigErrorMessage(sender, getMessagePath("forbidden-symbol"));
            return false;
        }

        if (key.matches("default-((?:posi)|(?:nega))tive-[xyz]")) {
            if (!Utils.isNumeric(value)) {
                sendConfigErrorMessage(sender, "incorrect_data_type");
                return false;
            }
            int intVal = Integer.parseInt(value);
            int oldValue = section.getInt(key);
            section.set(key, intVal);

            BlockVector3 negDiff = BlockVector3.ZERO;
            BlockVector3 posDiff = BlockVector3.ZERO;

            switch (key.split("default-")[1]) {
                case "positive-x" -> posDiff = BlockVector3.at(intVal - oldValue, 0 ,0);
                case "positive-y" -> posDiff = BlockVector3.at(0, intVal - oldValue ,0);
                case "positive-z" -> posDiff = BlockVector3.at(0, 0 ,intVal - oldValue);
                case "negative-x" -> negDiff = BlockVector3.at(oldValue - intVal, 0 ,0);
                case "negative-y" -> negDiff = BlockVector3.at(0, oldValue - intVal ,0);
                case "negative-z" -> negDiff = BlockVector3.at(0, 0 ,oldValue - intVal);
            }

            QuarterRegion.init();
            RegionDefaultSizeUpdateOperation.updateAll(negDiff, posDiff);

        }
        else section.set(key, value);

        sendConfigSuccessMessage(sender, getMessagePath("success"));

        return true;
    }


    @ABCompleter(index = 0)
    private List<String> completeKeys() {
        ConfigurationSection section = GuildQ.getInstance().getConfig().getConfigurationSection("Quarters.region");
        if (section == null) {
            section = GuildQ.getInstance().getConfig().createSection("Quarters.region");
        }
        return new ArrayList<>(section.getKeys(false));
    }

    @ABCompleter(index = 1)
    private String completeValue = " ";


    @DefaultConfigMessage(forN = "success")
    private static final String SUCCESS = "Ustawiono wartosc!";

    @DefaultConfigMessage(forN = "forbidden-symbol")
    private static final String FORBIDDEN_SYMBOL = "Klucz zawiera niedozwolony symbol: &e'.'";

    @Override
    public String getName() {
        return "setdefproperty";
    }

    @Override
    public Class<? extends InternalCommand> getParentCommandClass() {
        return RegionParentCommand.class;
    }

    @Override
    public List<String> complete(String[] args, CommandSender sender) {
        return ABComplete(args, sender);
    }

    @Override
    public List<TabCompleter> getCompletion() {
        return completion;
    }
}
