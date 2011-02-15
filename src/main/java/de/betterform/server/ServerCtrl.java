/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

/**
 * @author <a href="mailto:tobias.krebs@betterform.de">tobi</a>
 * @version $Id: ServerCtrl 17.11.2010 tobi $
 */
public interface ServerCtrl {

    public void start();
    public void stop() throws Exception;
    public String getStatus();
    public boolean isRunning();
    public int getPort();
    public String getWorkDir();
}
