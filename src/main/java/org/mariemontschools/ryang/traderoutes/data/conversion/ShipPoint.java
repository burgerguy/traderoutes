package org.mariemontschools.ryang.traderoutes.data.conversion;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ShipPoint implements Comparable<ShipPoint> {
    private final LocalDateTime time;
    private final float latitude;
    private final float longitude;

    ShipPoint(LocalDateTime time, float latitude, float longitude) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocalDateTime time() {
        return time;
    }

    public float latitude() {
        return latitude;
    }

    public float longitude() {
        return longitude;
    }

    @Override
    public int compareTo(ShipPoint o) {
        return time.compareTo(o.time);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ShipPoint that = (ShipPoint) obj;
        return Objects.equals(this.time, that.time) &&
                Float.floatToIntBits(this.latitude) == Float.floatToIntBits(that.latitude) &&
                Float.floatToIntBits(this.longitude) == Float.floatToIntBits(that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, latitude, longitude);
    }

    @Override
    public String toString() {
        return "ShipPoint[" +
                "time=" + time + ", " +
                "latitude=" + latitude + ", " +
                "longitude=" + longitude + ']';
    }
}
