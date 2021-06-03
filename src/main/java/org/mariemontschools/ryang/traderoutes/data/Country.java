package org.mariemontschools.ryang.traderoutes.data;

import org.mariemontschools.ryang.traderoutes.util.Util;

import java.awt.*;

public enum Country {
    NL("Netherlands"),
    IE("Ireland"),
    SE("Sweden"),
    ES("Spain"),
    NO("Norway"),
    PH("Philippines"),
    DE("West Germany"),
    TH("Thailand"),
    US("USA"),
    EG("Egypt"),
    IS("Iceland"),
    YU("Yugoslavia"),
    UK("United Kingdom"),
    CA("Canada"),
    IL("Israel"),
    PL("Poland"),
    FR("France"),
    BE("Belgium"),
    MY("Malaysia"),
    BR("Brazil"),
    DK("Denmark"),
    ZA("South Africa"),
    SU("USSR"),
    SG("Singapore"),
    IT("Italy"),
    AU("Australia"),
    FI("Finland"),
    KE("Kenya"),
    IN("India"),
    JP("Japan"),
    KR("South Korea"),
    TZ("Tanzania"),
    HK("Hong Kong"),
    PK("Pakistan"),
    NC("New Caledonia"),
    UG("Uganda"),
    NZ("New Zealand"),
    AR("Argentina"),
    PT("Portugal"),
    MX("Mexico"),
    DD("East Germany"), // is this right?
    GB("Great Britain"),
    UNKNOWN("Unknown/National", Color.BLACK);

    private final String displayName;
    private final Color color;

    Country(String displayName) {
        this.displayName = displayName;
        this.color = Util.randomColor();
    }

    Country(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }
}
