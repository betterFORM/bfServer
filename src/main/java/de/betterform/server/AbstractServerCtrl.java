/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

import java.io.*;
import java.util.Properties;

/**
 * @author <a href="mailto:tobias.krebs@betterform.de">tobi</a>
 * @version $Id: AbstractServerCtrl 17.11.2010 tobi $
 */
public abstract class AbstractServerCtrl implements ServerCtrl {
    private static String configfile = "META-INF/server-conf.xml";

    private static ServerCtrl instance = null;

    private static int port = -1;
    private static String context = null;
    private static String ctrlClass = null;
    private static Properties properties = null;
    private static String workingDirectory = new File(".").getAbsolutePath();

    public static ServerCtrl getInstance() {
        if(AbstractServerCtrl.instance == null) {
           //AbstractServerCtrl.configfile = new File(AbstractServerCtrl.workingDirectory + "/server-conf.xml").getAbsolutePath();
           AbstractServerCtrl.buildInstance();
        }

        return AbstractServerCtrl.instance;
    }

    private static void buildInstance() {
        try {
            Class ctrlClass = Class.forName(getCtrlClass());
            AbstractServerCtrl.instance = (ServerCtrl) ctrlClass.newInstance();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    protected static Properties getProperties() {
        if (AbstractServerCtrl.properties == null) {
            AbstractServerCtrl.properties = new Properties();


            try {
                ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                if (classLoader != null) {
                    InputStream inputStream = classLoader.getResourceAsStream(AbstractServerCtrl.configfile);
                        if (inputStream != null) {
                            AbstractServerCtrl.properties.loadFromXML(inputStream);
                        }
                }
            } catch (FileNotFoundException fnfe) {
                System.err.println("Could not find configfile: " + AbstractServerCtrl.configfile + ".");
                System.err.println("Please ensure it is present on your System.");
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                System.err.println("Could not read configfile: " + AbstractServerCtrl.configfile + ".");
                ioe.printStackTrace();
            }
        }

        return properties;
    }

    private static String getCtrlClass() {
        if (AbstractServerCtrl.ctrlClass == null) {
            AbstractServerCtrl.ctrlClass = getProperties().getProperty("serverCtrlClass", "de.betterform.server.JettyCtrl");
        }

        return AbstractServerCtrl.ctrlClass;
    }

    public int getPort() {
        if (this.port == -1) {
            //Try to use port from java opts
            try {
                this.port = Integer.parseInt(System.getProperties().getProperty("server.port", "-1"));
            } catch (NumberFormatException nfe) {
                System.out.println("You specified an invalid Port as Option to Java!");
                nfe.printStackTrace();
            }
            //Fallback on configured port or default port.
            if (this.port == -1) {
                this.port = Integer.parseInt(getProperties().getProperty("port", "45898"));
            }
        }
        return this.port;
    }

    public String getContext() {
        if (this.context == null) {
            this.context = getProperties().getProperty("context", "/betterform");
        }
        return this.context;
    }

     public String getWorkDir() {
	 System.out.println("WorkingDir: " + this.workingDirectory);
         return this.workingDirectory;
     }
}
