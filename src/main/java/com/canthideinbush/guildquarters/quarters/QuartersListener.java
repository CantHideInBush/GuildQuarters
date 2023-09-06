package com.canthideinbush.guildquarters.quarters;

import com.canthideinbush.guildquarters.GuildQ;
import com.sk89q.worldedit.event.Event;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.api.events.GuildJoinEvent;
import me.glaremasters.guilds.api.events.GuildLeaveEvent;
import me.glaremasters.guilds.api.events.GuildRemoveEvent;
import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildMember;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class QuartersListener implements Listener {


    @EventHandler
    public void onGuildRemove(GuildRemoveEvent event) {
        Guild guild = event.getGuild();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByGuildId(guild.getId())) != null) {
            quarter.remove();
            for (GuildMember member : guild.getMembers()) {
                GuildQ.getInstance().getGuildPermissions().purgePermissions(member.getAsPlayer());
            }
        }
    }

    @EventHandler
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByGuildId(guild.getId())) != null) {
            quarter.getRegion().updateMembers();
            quarter.syncPermissions(event.getPlayer());
        }
    }

    @EventHandler
    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByGuildId(guild.getId())) != null) {
            quarter.getRegion().updateMembers();
            GuildQ.getInstance().getGuildPermissions().purgePermissions(event.getPlayer());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GuildQuarter quarter;
        if ((quarter = GuildQ.getInstance().getQuartersManager().getByMember(player)) != null) {
            quarter.syncPermissions(event.getPlayer());
        }
        else GuildQ.getInstance().getGuildPermissions().purgePermissions(player);
    }


}
