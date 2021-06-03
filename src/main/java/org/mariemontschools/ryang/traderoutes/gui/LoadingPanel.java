package org.mariemontschools.ryang.traderoutes.gui;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {
    public LoadingPanel() {
        setLayout(new BorderLayout());
        ImageIcon loadingIcon = new ImageIcon("src/main/resources/loading.gif");
        JLabel loadingLabel = new JLabel("Loading...", loadingIcon, JLabel.CENTER);
        loadingLabel.setIconTextGap(0);
        Font font = new JLabel().getFont().deriveFont(24.0f);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            font = new Font("Segoe UI", Font.PLAIN, 24);
        } else if (osName.contains("mac")) {
            font = new Font("San Francisco", Font.PLAIN, 24);
        }
        loadingLabel.setFont(font);
        loadingLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        loadingLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        add(loadingLabel, BorderLayout.CENTER);
    }
}
