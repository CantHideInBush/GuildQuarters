package com.canthideinbush.guildquarters.quarters;

public interface QuarterObject {

    boolean isDefault();

    void place();

    void remove();

    QuarterObject clone();


}
