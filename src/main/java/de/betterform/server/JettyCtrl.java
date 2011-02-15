/*
 * Copyright (c) 2010. betterForm Project - http://www.betterform.de
 * Licensed under the terms of BSD License
 */
package de.betterform.server;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.NCSARequestLog;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;
import org.mortbay.thread.ThreadPool;

import java.io.IOException;
/**
 * @author Lars Windauer
 * @author <a href="mailto:tobias.krebs@betterform.de">tobi</a>
 * @version $Id: ServerWrapper 17.11.2010 tobi $
 */

public class JettyCtrl extends AbstractServerCtrl {
    private org.mortbay.jetty.Server server;
    private static JettyCtrl instance;
    static final String STATUS_RUNNING = "running";
    static final String STATUS_STOPPED = "stopped";
    private String status = STATUS_STOPPED;
    //private static int PORT = 45898;



    public JettyCtrl() {
        this.server = new org.mortbay.jetty.Server();
        this.server.setAttribute("JETTY_HOME", getWorkDir());
        ThreadPool threadPool = new QueuedThreadPool();
        server.setThreadPool(threadPool);

        Connector connector = new SelectChannelConnector();
        connector.setPort(getPort());

        server.setConnectors(new Connector[]{connector});

        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();

        RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[]{contexts, new DefaultHandler(), requestLogHandler});
        server.setHandler(handlers);


        try {
            WebAppContext.addWebApplications(server, "web", "org/mortbay/jetty/webapp/webdefault.xml", true, false);            

        } catch (IOException e) {
            e.printStackTrace();
        }

        NCSARequestLog requestLog = new NCSARequestLog("./logs/jetty.log");
        requestLog.setExtended(false);
        requestLogHandler.setRequestLog(requestLog);

    }

   /*
    public static JettyCtrl getInstance() {
        if (instance == null) {
            return instance = new JettyCtrl();
        } else {
            return instance;
        }
    }
   */

    public void start()  {

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();  
        }
        server.setStopAtShutdown(true);
        server.setSendServerVersion(true);

        if (server.isStarted()) {
            this.status = STATUS_RUNNING;
        }

    }

    public void stop() throws Exception {
        server.stop();
        this.status = STATUS_STOPPED;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isRunning() {
        return server.isRunning();
    }

/*
    public String getWorkDir() {
        return WORKING_DIRECTORY;
    }

*/
}
