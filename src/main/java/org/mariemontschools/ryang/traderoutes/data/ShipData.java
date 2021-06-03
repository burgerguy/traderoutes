package org.mariemontschools.ryang.traderoutes.data;

import java.awt.geom.Point2D;
import java.util.List;

public class ShipData {
    private final List<Point2D> points;
    private final Country country;

    public ShipData(List<Point2D> points, Country country) {
        this.points = points;
        this.country = country;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public Country getCountry() {
        return country;
    }
}
