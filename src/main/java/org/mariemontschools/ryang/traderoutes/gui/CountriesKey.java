package org.mariemontschools.ryang.traderoutes.gui;

import org.mariemontschools.ryang.traderoutes.data.Country;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class CountriesKey extends JComponent {
    private static final int HEIGHT_INCREMENT = 15;
    private static final int BOX_SIZE = 9;

    private final Set<Country> countries;

    public CountriesKey(Set<Country> countries) {
        this.countries = countries;
        Dimension d = getPreferredSize();
        setPreferredSize(new Dimension(120, getPreferredSize().height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int currentX = 5;
        int currentY = 40;
        for (Country country : countries) {
            g.setColor(country.getColor());
            g.fillRect(currentX, currentY, BOX_SIZE, BOX_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(currentX, currentY, BOX_SIZE, BOX_SIZE);
            g.drawString(country.getDisplayName(), currentX + BOX_SIZE + 5, currentY + (int) ((g.getFont().getSize() + BOX_SIZE) / 2.0));
            currentY += HEIGHT_INCREMENT;
        }
    }
}
