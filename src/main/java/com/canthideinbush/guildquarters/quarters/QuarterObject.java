package com.canthideinbush.guildquarters.quarters;

public interface QuarterObject {

    String getId();

    String getCategory();

    boolean isDefault();

    void place(GuildQuarter quarter);

    void remove(GuildQuarter quarter);

    void initialize(GuildQuarter quarter);

}
