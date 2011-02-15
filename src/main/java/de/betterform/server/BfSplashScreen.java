/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
/**
 * @author Lars Windauer
 */
public class BfSplashScreen extends JFrame {
    JPanel contentPane;
    JLabel imageLabel = new JLabel();

    public BfSplashScreen(URL url) throws IOException {
        try {
            ImageIcon ii = new ImageIcon(url);
            setBackground(new Color(0, 0, 0, 0));
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            contentPane = (JPanel) getContentPane();
            contentPane.setLayout(new BorderLayout());
            setUndecorated(true);
            setSize(new Dimension(ii.getIconWidth(), ii.getIconHeight()));
            // add the image label
            imageLabel.setIcon(ii);
            contentPane.add(imageLabel, java.awt.BorderLayout.CENTER);
            // show it
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

