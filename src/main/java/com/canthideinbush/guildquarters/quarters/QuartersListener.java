package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import me.glaremasters.guilds.api.events.GuildRemoveEvent;
import me.glaremasters.guilds.guild.Guild;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuartersListener implements Listener {


    @EventHandler
    public void onGuildRemove(GuildRemoveEvent event) {
        Guild guild = event.getGuild();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByGuildId(guild.getId())) != null) {
            quarter.clearChunks(() -> {});
            GuildQ.getInstance().getQuartersManager().unregister(quarter);
        }
    }

}
