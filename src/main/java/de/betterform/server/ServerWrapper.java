/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author <a href="mailto:tobias.krebs@betterform.de">tobi</a>
 * @version $Id: ServerWrapper 17.11.2010 tobi $
 */
public class ServerWrapper {
    private Properties properties;
    private String configfile = "META-INF/server-conf.xml";

    private Properties getProperties() {
        if (this.properties == null) {
            this.properties = new Properties();

            try {
                //FileInputStream fileInputStream = new FileInputStream(this.configfile);
                ClassLoader classLoader = this.getClass().getClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(this.configfile);
                this.properties.loadFromXML(inputStream);
            } catch (FileNotFoundException fnfe) {
                System.err.println("Could not find configfile: " + this.configfile + ".");
                System.err.println("Please ensure it is present on your System.");
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                System.err.println("Could not read configfile: " + this.configfile + ".");
                ioe.printStackTrace();
            }
        }

        return properties;
    }

    private void startServer() {
        Project project = new Project();
        project.init();

        DefaultLogger logger = new DefaultLogger();
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        logger.setMessageOutputLevel(Project.MSG_INFO);
        project.addBuildListener(logger);

        Java java = new Java();
        java.setFork(true);
        java.setSpawn(false);
        java.setClassname(de.betterform.server.Server.class.getName());
        java.setProject(project);
        java.setClasspath(Path.systemClasspath);

        Commandline.Argument jvmArgs = java.createJvmarg();
        jvmArgs.setLine(getJavaOpts());

        java.init();
        java.executeJava();
    }

    protected String getJavaOpts() {
        StringBuffer javaOpts = new StringBuffer(getProperties().getProperty("java.opts", ""));
        String serverCtrlClass = getProperties().getProperty("serverCtrlClass", "");

        if (serverCtrlClass.toLowerCase().contains("xrx")) {
            javaOpts.append(getXRXOpts());
        }
        return javaOpts.toString();
    }

    protected String getXRXOpts() {
        //TODO: get more XRX options
        StringBuffer eXistOpts = new StringBuffer();
        String xrxHome = getProperties().getProperty("xrx.home", "");
	String workingDirectory = new File(xrxHome).getAbsolutePath();
	String endorsedDirs = workingDirectory + "/" + getProperties().getProperty("endorsed.dirs", "lib/endorsed");
        
	eXistOpts.append(" ");
	eXistOpts.append("-Dexist.home=");
	eXistOpts.append(workingDirectory);
       
	eXistOpts.append(" ");
	eXistOpts.append("-Djava.endorsed.dirs=");
	eXistOpts.append(endorsedDirs);
       

        return eXistOpts.toString();
    }

    public static void main(String argv[]) {
        ServerWrapper serverFacade = new ServerWrapper();
        serverFacade.startServer();
    }
}
