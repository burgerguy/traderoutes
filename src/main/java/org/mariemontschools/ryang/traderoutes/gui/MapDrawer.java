package org.mariemontschools.ryang.traderoutes.gui;

import org.mariemontschools.ryang.traderoutes.data.ShipData;
import org.mariemontschools.ryang.traderoutes.data.TimelineData;
import org.mariemontschools.ryang.traderoutes.data.YearData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.IntSupplier;

public class MapDrawer extends JComponent {
    public static final int DEFAULT_MAP_WIDTH = 900;
    public static final int DEFAULT_MAP_HEIGHT = 900;

    private final TimelineData data;
    private final IntSupplier yearSupplier;
    private final Image mapImage;

    public MapDrawer(TimelineData data, IntSupplier yearSupplier) {
        this.data = data;
        this.yearSupplier = yearSupplier;
        Image readImage;
        try {
            readImage = ImageIO.read(new File("src/main/resources/map.png"));
        } catch (IOException e) {
            readImage = null;
        }
        this.mapImage = readImage;
        setPreferredSize(new Dimension(DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), null);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        YearData yearData = data.getYear((short) yearSupplier.getAsInt());
        if (yearData != null) {
            List<ShipData> ships = yearData.getShips();
            //Collections.shuffle(ships);
            for (ShipData shipData : ships/*.stream().filter(s -> !s.getCountry().equals(Country.UNKNOWN)).limit(100).collect(Collectors.toList())*/) {
                g.setColor(shipData.getCountry().getColor());
                Point2D previousPoint = null;
                for (Point2D point : shipData.getPoints()) {
                    if (previousPoint != null) {
                        if (previousPoint.distanceSq(point) <= 0.03) {
                            g.drawLine((int) (previousPoint.getX() * getWidth()), (int) (previousPoint.getY() * getHeight()), (int) (point.getX() * getWidth()), (int) (point.getY() * getHeight()));
                        }
                    }
                    previousPoint = point;
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}
