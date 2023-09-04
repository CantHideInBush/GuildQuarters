package com.canthideinbush.guildquarters.quarters.portals;

import com.Zrips.CMI.CMI;
import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.redirectionportal.RedirectionPortalBuildCommand;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.utils.DefMessages;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.ConfigMessageExtension;
import com.canthideinbush.utils.commands.DefaultConfigMessage;
import com.canthideinbush.utils.commands.InternalCommand;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RedirectionPortalBuilder implements ObjectBuilder<RedirectionPortal>, ConfigMessageExtension {


    public RedirectionPortalBuilder() {
    }

    public RedirectionPortalBuilder(RedirectionPortal portal) {
        this.targetName = portal.getTargetName();
        this.name = portal.getName();
        this.offset = portal.getOffset();
        this.offset1 = portal.getOffset1();
    }

    private Player sender;
    public void setSender(Player sender) {
        this.sender = sender;
    }

    private String targetName;
    private String name;
    private Vector offset;
    private Vector offset1;

    private boolean isDefault = false;

    @Override
    public void with(String option, String value) {
        switch (option.toLowerCase()) {
            case "targetname" -> this.targetName = value;
            case "name" -> this.name = value;
            case "offset" -> {
                Block targetBlock = sender.getTargetBlock(8);
                Location location = targetBlock != null ? targetBlock.getLocation() : sender.getLocation();
                this.offset = location.toVector().subtract(QuartersManager.templateQuarter.getInitialLocation().toVector());
            }
            case "offset1" -> {
                Block targetBlock = sender.getTargetBlock(8);
                Location location = targetBlock != null ? targetBlock.getLocation() : sender.getLocation();
                this.offset1  = location.toVector().subtract(QuartersManager.templateQuarter.getInitialLocation().toVector());
            }
        }
    }

    @Override
    public String errorFor(String option, String value) {
        if (option.equalsIgnoreCase("name")) {
            if (RedirectionPortals.exists(value)) {
                return getMessage("name-taken");
            }
        }
        else if (option.equalsIgnoreCase("isDefault")) {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage(DefMessages.INC_DATA_TYPE);
            }
        }
        return null;
    }

    @Override
    public List<String> complete(CommandSender sender, String option, String value) {
        switch (option.toLowerCase()) {
            case "name" -> {
                return Collections.singletonList(" ");
            }
            case "targetname" -> {
                return new ArrayList<>(CMI.getInstance().getPortalManager().getPortals().keySet());
            }
            case "offset","offset1" -> {
                Block targetBlock = ((Player) sender).getTargetBlock(8);
                Location location = targetBlock != null ? targetBlock.getLocation() : ((Player) sender).getLocation();
                return Collections.singletonList((location.toBlockLocation().toString()));
            }
            case "isdefault" -> {
                return Arrays.asList("true", "false");
            }
        }



        return ObjectBuilder.super.complete(sender, option, value);
    }

    @Override
    public RedirectionPortal build() {
        return new RedirectionPortal(name, targetName, offset, offset1, isDefault);
    }

    @Override
    public List<String> options() {
        return Arrays.asList("targetname", "name", "offset", "offset1", "isdefault");
    }

    @Override
    public List<String> missingOptions() {
        ArrayList<String> missing = new ArrayList<>();
        if (targetName == null) missing.add("targetname");
        if (name == null) missing.add("name");
        if (offset == null) missing.add("offset");
        if (offset1 == null) missing.add("offset1");
        return missing;
    }

    @DefaultConfigMessage(forN = "name-taken")
    private static final String NAME_TAKEN = "Ta nazwa jest juz zajeta!";

    @Override
    public boolean isComplete() {
        return targetName != null && name != null && offset != null && offset1 != null;
    }

    @Override
    public Class<? extends InternalCommand> getCommandClass() {
        return RedirectionPortalBuildCommand.class;
    }


}
