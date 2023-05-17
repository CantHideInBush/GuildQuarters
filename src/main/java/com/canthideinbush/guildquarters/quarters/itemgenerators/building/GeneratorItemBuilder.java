package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import com.canthideinbush.utils.ObjectBuilder;
import org.bukkit.command.CommandSender;

public interface GeneratorItemBuilder extends ObjectBuilder<GeneratorItem> {

    void setSender(CommandSender sender);

}
