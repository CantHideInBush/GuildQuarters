package com.canthideinbush.guildquarters.quarters.itemgenerators.building;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.utils.ObjectBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public interface GeneratorBuilder extends ObjectBuilder<ItemGenerator> {



    static String MUST_BE_POSITIVE() {
        return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage(
                "must-be-positive"
        );
    }

    static String INCORRECT_DATA_TYPE() {
        return GuildQ.getInstance().getUtilsProvider().getChatUtils().getMessage(
                "incorrect_data_type"
        );
    }

}
