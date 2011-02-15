/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Lars Windauer
 */
public class Server extends JFrame  {

    private static final String EXIT = "OK";
    private static final String SERVICERUNNING = "Service is already running";


    public static void main(String[] args) {
        Server server = new Server();
        try {
            System.out.println("Show BfSplashScreen");
            BfSplashScreen splash = server.showSplashScreen();
            System.out.println("Close BfSplashScreen");
            BfDialog bfDialog = null;
            bfDialog = bfDialog.getInstance();
            bfDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            splash.setVisible(false);
            splash= null;
            bfDialog.setVisible(true);
            bfDialog.toFront();
        }
		catch (Exception e) {
    		handleExecption(e);
        }
    }





    private BfSplashScreen showSplashScreen() throws IOException {
        String workingDIr = new File(".").getAbsolutePath();
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL imageURL = classLoader.getResource("META-INF/images/bf_logo_482x195.png");

        BfSplashScreen splash = new BfSplashScreen(imageURL);
        if (splash == null) {
            return null;
        }


        for(int i=0; i<150; i++) {
            try {
                Thread.sleep(10);
            }
            catch(InterruptedException ie) {
            }
        }
        splash.setVisible(false);
        return splash;

    }


    private static void handleExecption(Exception e) {
        JDialog dialog = new JDialog();
        dialog.setSize(300, 300);
        dialog.setLocation(300, 300);
        JLabel errorTitle = new JLabel();

        System.out.println("1: " + e.toString());
        System.out.println("2: " + e.getMessage());

        if (e instanceof java.net.BindException) {
            errorTitle.setText("\n\n" + SERVICERUNNING);
        } else if (e instanceof java.lang.IllegalArgumentException) {
            errorTitle.setText("\n\n" + EXIT);
        } else {
            errorTitle.setText("Unknown Error: " + e.toString());
        }
        JPanel jp = new JPanel();
        JButton btnExit = new JButton(EXIT);
        jp.add(btnExit);

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (ae.getActionCommand().equals(EXIT)) {
                    System.exit(0);
                }
            }
        });
        dialog.setLayout(new BorderLayout());
        dialog.add(BorderLayout.CENTER, jp);
        dialog.add(BorderLayout.NORTH, errorTitle);


    }
}
