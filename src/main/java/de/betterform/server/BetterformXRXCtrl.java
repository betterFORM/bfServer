/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

/**
 * @author <a href="mailto:tobias.krebs@betterform.de">tobi</a>
 * @version $Id: BetterformXRXCtrl 17.11.2010 tobi $
 */
public class BetterformXRXCtrl extends AbstractServerCtrl {
    org.exist.start.Main main;

    public BetterformXRXCtrl() {
        main = org.exist.start.Main.getMain();
    }
    public void start() {
        String[] args = new String[] { "jetty" };
        main.run(args);
    }

    public void stop() throws Exception {
        String[] args = new String[] { "shutdown" };
        main.run(args);
    }

    public String getStatus() {
        main.getMode();
        return "Unknown";
    }

    public boolean isRunning() {
        return true;
    }
}
