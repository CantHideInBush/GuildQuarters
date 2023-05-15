package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.quarters.itemgenerators.GeneratorItem;
import org.bukkit.entity.Player;

import java.util.List;

public interface ItemBuilder {

    GeneratorItem build();
    List<String> options();

    List<String> complete(Player player, String option);

    String errorFor(String option, String value);

    void with(String option, String value);

    void withId(String id);

    List<String> missingOptions();

    boolean isComplete();


}
