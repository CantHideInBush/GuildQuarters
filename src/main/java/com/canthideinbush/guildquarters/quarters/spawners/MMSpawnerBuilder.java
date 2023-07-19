package com.canthideinbush.guildquarters.quarters.spawners;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.commands.spawner.SpawnerBuildCommand;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.ObjectBuilder;
import com.canthideinbush.utils.commands.ConfigMessageExtension;
import com.canthideinbush.utils.commands.InternalCommand;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MMSpawnerBuilder implements ObjectBuilder<MMSpawner> {
    public String id;
    public String mythicId;
    public Vector offset;



    private Player sender;
    public void setSender(Player sender) {
        this.sender = sender;
    }

    @Override
    public MMSpawner build() {
        return new MMSpawner(this);
    }

    @Override
    public List<String> options() {
        return Arrays.asList("id", "mythicid", "offset");
    }

    @Override
    public List<String> complete(CommandSender commandSender, String option, String value) {
        switch (option) {
            case "id" -> {
                return Collections.singletonList(" ");
            }
            case "mythicid" -> {
                return MythicBukkit.inst().getSpawnerManager().getSpawners().stream().map(
                        MythicSpawner::getName
                ).filter(s -> !s.contains("guildq-")).collect(Collectors.toList());
            }
            case "offset" -> {
                if (commandSender instanceof Player) {
                    Block block = ((Player) commandSender).getTargetBlock(6);
                    if (block != null) {
                        return Collections.singletonList(block.getX() + ";" + block.getY() + ";" + block.getZ());
                    }
                }
            }


        }
        return Collections.emptyList();
    }

    @Override
    public String errorFor(String option, String value) {
        switch (option) {
            case "mythicid" -> {
                if (MythicBukkit.inst().getSpawnerManager().getSpawnerByName(value) == null) {
                    return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage("common.mythic-spawner-nonexistent");
                }
            }
            case "offset" -> {
                if (!GuildUtils.contains(QuartersManager.templateQuarter, sender.getLocation())) {
                    return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage("common.outside-template");
                }
            }
        }
        return null;
    }

    @Override
    public void with(String option, String value) {
        switch (option) {
            case "id" -> this.id = value;
            case "mythicid" -> this.mythicId = value;
            case "offset" -> this.offset = (sender.getLocation().subtract(QuartersManager.templateQuarter.getInitialLocation())).toVector();
        }
    }

    @Override
    public List<String> missingOptions() {
        if (isComplete()) return null;
        ArrayList<String> options = new ArrayList<>();
        if (id == null) options.add("id");
        if (offset == null) options.add("offset");
        if (mythicId == null) options.add("mythicid");
        return options;
    }

    @Override
    public boolean isComplete() {
        return id != null && offset != null && mythicId != null;
    }

}
