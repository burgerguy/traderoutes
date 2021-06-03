package org.mariemontschools.ryang.traderoutes.data;

import java.awt.geom.Point2D;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class TimelineData {
    private final int startingYear;
    private final int endingYear;
    private final Map<Short, YearData> years;
    private final Set<Country> usedCountries;
    // TODO: perhaps include more metadata?

    private TimelineData(int startingYear, int endingYear, Map<Short, YearData> years, Set<Country> usedCountries) {
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.years = years;
        this.usedCountries = usedCountries;
    }

    public static TimelineData fromFile(Path dataFile) throws IOException {
        short earliestYear = Short.MAX_VALUE;
        short latestYear = Short.MIN_VALUE;
        Map<Short, YearData> years = new HashMap<>();
        Set<Country> usedCountries = new TreeSet<>((c1, c2) -> {
            boolean isUnknown1 = c1.equals(Country.UNKNOWN);
            boolean isUnknown2 = c2.equals(Country.UNKNOWN);
            if (isUnknown1 && isUnknown2) {
                return 0;
            } else if (isUnknown1) {
                return 1;
            } else if (isUnknown2) {
                return -1;
            } else {
                return c1.getDisplayName().compareTo(c2.getDisplayName());
            }
        });

        try (DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(dataFile.toAbsolutePath().toString()))))) {
            while (true) {
                short year = 0;
                try {
                    year = inputStream.readShort();
                } catch (EOFException e) {
                    break; // stop reading, no more years
                }
                if (year > latestYear) {
                    latestYear = year;
                }

                if (year < earliestYear) {
                    earliestYear = year;
                }

                int numShips = inputStream.readInt();
                List<ShipData> ships = new ArrayList<>();
                for (int currentShip = 0; currentShip < numShips; currentShip++) {
                    byte countryCode = inputStream.readByte();
                    int numPoints = inputStream.readInt();
                    List<Point2D> points = new ArrayList<>();
                    for (int currentPoint = 0; currentPoint < numPoints; currentPoint++) {
                        points.add(createPoint(inputStream.readShort(), inputStream.readShort()));
                    }
                    Country country = countryCode > Country.values().length ? Country.UNKNOWN : Country.values()[countryCode];
                    usedCountries.add(country);
                    ships.add(new ShipData(points, country));
                }
                years.put(year, new YearData(ships));
            }
        }

        return new TimelineData(earliestYear, latestYear, years, usedCountries);
    }

    private static Point2D createPoint(short storedX, short storedY) {
        return new Point2D.Double((storedX - Short.MIN_VALUE) / 65535.0f, (storedY - Short.MIN_VALUE) / 65535.0f);
    }

    public YearData getYear(short year) {
        return years.get(year);
    }

    public int getStartingYear() {
        return startingYear;
    }

    public int getEndingYear() {
        return endingYear;
    }

    public Set<Country> getUsedCountries() {
        return Collections.unmodifiableSet(usedCountries);
    }
}
