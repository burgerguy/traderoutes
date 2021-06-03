package org.mariemontschools.ryang.traderoutes.gui;

import org.mariemontschools.ryang.traderoutes.data.TimelineData;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private final JPanel timelinePanel;
    private final JSlider timelineSlider;
    private final JButton pausePlayButton;
    private final JLabel yearLabel;
    private final MapDrawer mapDrawer;
    private final CountriesKey countriesKey;
    private final Timer animationTimer;
    private final String labelString;

    public MapPanel(TimelineData data) {
        setLayout(new BorderLayout());
        this.timelinePanel = new JPanel(new BorderLayout());
        this.timelineSlider = new JSlider(JSlider.HORIZONTAL, data.getStartingYear(), data.getEndingYear(), data.getStartingYear());
        this.pausePlayButton = new JButton("Play");
        this.labelString = "<html><div style='text-align: center;'>Ship Routes from " + data.getStartingYear() + " to " + data.getEndingYear() + "<br/>Year: ";
        this.yearLabel = new JLabel(labelString + timelineSlider.getValue() + "</div></html>");
        yearLabel.setHorizontalAlignment(JLabel.CENTER);
        Font font = new JLabel().getFont().deriveFont(24.0f);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            font = new Font("Segoe UI", Font.PLAIN, 24);
        } else if (osName.contains("mac")) {
            font = new Font("San Francisco", Font.PLAIN, 24);
        }
        yearLabel.setFont(font);
        this.mapDrawer = new MapDrawer(data, timelineSlider::getValue);
        timelineSlider.setMajorTickSpacing(10);
        timelineSlider.setMinorTickSpacing(1);
        timelineSlider.setLabelTable(timelineSlider.createStandardLabels(50));
        timelineSlider.setPaintLabels(true);
        timelineSlider.setPaintTicks(true);
        timelineSlider.addChangeListener(e -> {
            yearLabel.setText(labelString + timelineSlider.getValue() + "</div></html>");
            mapDrawer.repaint();
        });
        this.animationTimer = new Timer(1000, e -> {
            if (!timelineSlider.getValueIsAdjusting()) {
                int newValue = timelineSlider.getValue() + 1;
                timelineSlider.setValue(newValue <= timelineSlider.getMaximum() ? newValue : timelineSlider.getMinimum());
            }
        });
        pausePlayButton.addActionListener(e -> {
            if (animationTimer.isRunning()) {
                pausePlayButton.setText("Play");
                animationTimer.stop();
            } else {
                pausePlayButton.setText("Pause");
                animationTimer.start();
            }
        });
        timelinePanel.add(timelineSlider, BorderLayout.CENTER);
        timelinePanel.add(pausePlayButton, BorderLayout.WEST);
        this.countriesKey = new CountriesKey(data.getUsedCountries());
        add(timelinePanel, BorderLayout.SOUTH);
        add(mapDrawer, BorderLayout.CENTER);
        add(countriesKey, BorderLayout.EAST);
        add(yearLabel, BorderLayout.PAGE_START);
    }
}
