package org.mariemontschools.ryang.traderoutes.data.conversion;

import org.mariemontschools.ryang.traderoutes.data.Country;
import org.mariemontschools.ryang.traderoutes.util.Util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataConversionHelper {
    public static void main(String[] args) throws IOException {
        Map<Integer, Map<ShipIdentifier, List<ShipPoint>>> yearEntries = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(args[0]), 1)) {
            for (Path path : (Iterable<Path>) paths::iterator) {
                if (!Files.isDirectory(path) && path.toString().contains(".gz")) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path.toAbsolutePath().toString())), StandardCharsets.UTF_8))) {
                        StringBuilder tempStringBuilder = new StringBuilder();
                        boolean endReached = false;
                        long lineNumber = 1;
                        long failedLines = 0;

                        char[] yearBuffer = new char[4];
                        char[] monthBuffer = new char[2];
                        char[] dayBuffer = new char[2];
                        char[] firstHourBuffer = new char[2];
                        char[] secondHourBuffer = new char[2];
                        char[] firstLatBuffer = new char[3];
                        char[] secondLatBuffer = new char[2];
                        char[] firstLongBuffer = new char[4];
                        char[] secondLongBuffer = new char[2];
                        char[] indicatorBuffer = new char[2];
                        char[] shipIdBuffer = new char[9];
                        char[] countryBuffer = new char[2];
                        while (!endReached) {
                            try {
                                reader.read(yearBuffer);
                                reader.read(monthBuffer);
                                reader.read(dayBuffer);
                                reader.read(firstHourBuffer);
                                reader.read(secondHourBuffer);
                                reader.read(firstLatBuffer);
                                reader.read(secondLatBuffer);
                                reader.read(firstLongBuffer);
                                reader.read(secondLongBuffer);
                                reader.skip(9);
                                reader.read(indicatorBuffer);
                                reader.read(shipIdBuffer);
                                reader.read(countryBuffer);
                                reader.skip(95);
                                throwIfBadIndicator(indicatorBuffer);
                                int year = Integer.parseInt(new String(yearBuffer));
                                int month = Integer.parseInt(new String(monthBuffer).trim());
                                int day = Integer.parseInt(new String(dayBuffer).trim());
                                int hour = 0;
                                try {
                                    hour = Integer.parseInt(new String(firstHourBuffer).trim());
                                } catch (NumberFormatException e) {
                                    // ignore, hour is empty when 0
                                }
                                int minute = (int) (Integer.parseInt(new String(secondHourBuffer).trim()) / 100.0 * 60.0); // if minute is empty, dont parse ship bc there's no time data at all
                                LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute);
                                tempStringBuilder.setLength(0);
                                tempStringBuilder.append(firstLatBuffer);
                                if (secondLatBuffer[0] == '-') {
                                    tempStringBuilder.append('-');
                                    tempStringBuilder.append('.');
                                    tempStringBuilder.append(secondLatBuffer[1]);
                                } else {
                                    tempStringBuilder.append('.');
                                    tempStringBuilder.append(secondLatBuffer);
                                }
                                float latitude = Float.parseFloat(Util.aggressiveTrim(tempStringBuilder.toString()));
                                tempStringBuilder.setLength(0);
                                tempStringBuilder.append(firstLongBuffer);
                                if (secondLongBuffer[0] == '-') {
                                    tempStringBuilder.append('-');
                                    tempStringBuilder.append('.');
                                    tempStringBuilder.append(secondLongBuffer[1]);

                                } else {
                                    tempStringBuilder.append('.');
                                    tempStringBuilder.append(secondLongBuffer);
                                }
                                float longitude = Float.parseFloat(Util.aggressiveTrim(tempStringBuilder.toString()));
                                String shipId = new String(shipIdBuffer);
                                if (Util.aggressiveTrim(shipId).equals(""))
                                    throw new IllegalArgumentException("Ship ID can't be blank");
                                byte countryCode = compressCountryCode(new String(countryBuffer));
                                yearEntries.computeIfAbsent(time.getYear(), i -> new HashMap<>()).computeIfAbsent(new ShipIdentifier(countryCode, shipId.hashCode()), i -> new ArrayList<>()).add(new ShipPoint(time, latitude, longitude));
                            } catch (Exception e) {
                                failedLines++;
                            } finally {
                                while (true) {
                                    int nextInt = reader.read();
                                    if (nextInt == -1) {
                                        endReached = true;
                                        break;
                                    }
                                    if ((char) nextInt == '\n') {
                                        lineNumber++;
                                        break;
                                    }
                                }
                            }
                        }
                        System.out.println(path.getFileName().toString() + " Failure Percentage: " + ((double) failedLines / lineNumber) * 100 + "%");
                    }
                }
            }
        }

        Path outputPath = Paths.get("D:", "Downloads", "data.bin.gz");
        if (!Files.exists(outputPath)) Files.createFile(outputPath);
        try (DataOutputStream outputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(outputPath.toAbsolutePath().toString())))) {
            for (Map.Entry<Integer, Map<ShipIdentifier, List<ShipPoint>>> yearEntry : yearEntries.entrySet()) {
                outputStream.writeShort(yearEntry.getKey());
                Map<ShipIdentifier, List<ShipPoint>> shipEntries = yearEntry.getValue();
                outputStream.writeInt(shipEntries.size());
                for (Map.Entry<ShipIdentifier, List<ShipPoint>> shipEntry : shipEntries.entrySet()) {
                    outputStream.writeByte(shipEntry.getKey().countryCode());
                    List<ShipPoint> points = shipEntry.getValue();
                    outputStream.writeInt(points.size());
                    Collections.sort(points);
                    for (ShipPoint point : points) {
                        outputStream.writeShort(compressLongitude(point.longitude()));
                        outputStream.writeShort(compressLatitude(point.latitude()));
                    }
                }
            }
        }
    }

    private static void throwIfBadIndicator(char[] indicatorBuffer) {
        int indicator = Integer.parseInt(new String(indicatorBuffer).trim());
        switch (indicator) {
            case 2: // generic ids
            case 3: // buoys
            case 4: // more buoys
            case 8: // fishing boats
                throw new IllegalArgumentException("Indicator invalid");
        }
    }

    public static byte compressCountryCode(String countryCode) {
        if (Util.aggressiveTrim(countryCode).equals("")) {
            return (byte) Country.UNKNOWN.ordinal();
        } else {
            try {
                return Byte.parseByte(countryCode.trim());
            } catch (NumberFormatException e) {
                return (byte) Country.valueOf(countryCode).ordinal();
            }
        }
    }

    public static short compressLatitude(float latitude) {
        double latRad = latitude * Math.PI / 180.0;
        double mercN = Math.log(Math.tan((Math.PI / 4.0) + (latRad / 2.0)));
        return (short) ((65535.0 / 2.0) - (65535.0 * mercN / (2.0 * Math.PI)) + Short.MIN_VALUE);
    }

    public static short compressLongitude(float longitude) {
        return (short) ((longitude + 180.0) * (65535.0 / 360.0) + Short.MIN_VALUE);
    }
}
