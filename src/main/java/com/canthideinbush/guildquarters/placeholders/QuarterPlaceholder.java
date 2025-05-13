package com.canthideinbush.guildquarters.placeholders;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.http.templating.PlaceholderHandler;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import io.lumine.mythic.core.skills.placeholders.PlaceholderExecutor;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuarterPlaceholder extends PlaceholderExpansion {

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        GuildQuarter quarter = GuildQ.getInstance().getQuartersManager().getByMember(player);
        if (quarter == null) return "";
        return quarter.getShortId();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "gq.player.quarter";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Karwsz";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
}
