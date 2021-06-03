package org.mariemontschools.ryang.traderoutes.main;

import org.mariemontschools.ryang.traderoutes.data.TimelineData;
import org.mariemontschools.ryang.traderoutes.gui.LoadingPanel;
import org.mariemontschools.ryang.traderoutes.gui.MapPanel;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Unable to set system LAF");
        }
        JFrame frame = new JFrame();
        frame.setTitle("Ship Routes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new LoadingPanel());
        frame.pack();
        frame.setVisible(true);
        new Thread(() -> {
            try {
                TimelineData data = TimelineData.fromFile(Paths.get("src", "main", "resources", "data.bin.gz"));
                SwingUtilities.invokeLater(() -> {
                    frame.getContentPane().removeAll();
                    frame.add(new MapPanel(data));
                    frame.revalidate();
                    frame.pack();
                });
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }, "Data Loader Thread").start();
    }
}
