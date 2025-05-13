package com.canthideinbush.guildquarters.http.integration;

import com.canthideinbush.guildquarters.GuildQ;
import com.canthideinbush.guildquarters.http.QuarterEditorHandler;
import com.canthideinbush.guildquarters.quarters.GuildQuarter;
import com.canthideinbush.guildquarters.quarters.QuartersManager;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerator;
import com.canthideinbush.guildquarters.quarters.itemgenerators.ItemGenerators;

public class HttpsPluginIntegration {


    public static void integrateQuarters(QuartersManager manager) {
        QuarterEditorHandler.registerSupplier("quarterList", () -> {
            StringBuilder builder = new StringBuilder();
            for (GuildQuarter quarter : manager.getObjects()) {
                builder.append("<div class=\"quarter-template\">")
                        .append("<p>")
                        .append(quarter.getShortId())
                        .append("</p>")
                        .append("</div>");
            }
            return builder.toString();
        });
    }

    public static void integrateItemGenerators() {
        QuarterEditorHandler.registerSupplier("generatorsList", () -> {
            StringBuilder builder = new StringBuilder();
            for (ItemGenerator generator : GuildQ.getInstance().getItemGenerators().getGenerators()) {
                builder.append("<div class=\"quarter-template\">")
                        .append("<p>")
                        .append(generator.getId())
                        .append("</p>")
                        .append("</div>");
            }
            return builder.toString();
        });
    }

}
