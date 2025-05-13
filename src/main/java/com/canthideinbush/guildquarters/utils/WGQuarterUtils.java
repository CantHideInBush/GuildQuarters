package com.canthideinbush.guildquarters.utils;

import com.Zrips.CMI.CMI;
import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.utils.GuildUtils;
import com.canthideinbush.utils.storing.ArgParser;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class WGQuarterUtils {
    public static boolean contains(GuildQuarter quarter, Location location) {
        return contains(quarter, location.toVector());
    }

    public static boolean contains(GuildQuarter quarter, Vector vector) {
        Location min = quarter.getInitialLocation().add(((GuildUtils.getQuarterSize() * -16) | 15) - 15, 0, ((GuildUtils.getQuarterSize() * -16) | 15) - 15);
        min.setY(GuildUtils.getGuildWorld().getMinHeight());
        Location max = quarter.getInitialLocation().add((GuildUtils.getQuarterSize() * 16) | 15, 0, (GuildUtils.getQuarterSize() * 16) | 15);
        max.setY(GuildUtils.getGuildWorld().getMaxHeight());


        return (vector.getX() <= max.getX() && vector.getX() >= min.getX())
                && (vector.getY() <= max.getY() && vector.getY() >= min.getY())
                && (vector.getZ() <= max.getZ() && vector.getZ() >= min.getZ());
    }

    public static Vector getRegionOffset() {
        return new ArgParser(GuildQ.getInstance().getConfig().getString("Quarters.region.offset", "0;0;0").split(";")).nextVector();
    }
}
