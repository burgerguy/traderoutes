package org.mariemontschools.ryang.traderoutes.data.conversion;

import java.util.Objects;

public final class ShipIdentifier {
    private final byte countryCode;
    private final int shipIdHash;

    ShipIdentifier(byte countryCode, int shipIdHash) {
        this.countryCode = countryCode;
        this.shipIdHash = shipIdHash;
    }

    public byte countryCode() {
        return countryCode;
    }

    public int shipIdHash() {
        return shipIdHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ShipIdentifier that = (ShipIdentifier) obj;
        return this.countryCode == that.countryCode &&
                this.shipIdHash == that.shipIdHash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, shipIdHash);
    }

    @Override
    public String toString() {
        return "ShipIdentifier[" +
                "countryCode=" + countryCode + ", " +
                "shipIdHash=" + shipIdHash + ']';
    }

}
