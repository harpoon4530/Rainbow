package org.rainbow;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.rainbow.handlers.RecordServletV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static Server jetty;
    public static void main(String[] args) throws Exception {
        startServer();
    }

    public static void startServer() throws Exception {
        jetty = new Server(8080);
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);

        Injector injector = Guice.createInjector();

        ServletContextHandler servletContextHandler =
                new ServletContextHandler(jetty, "/", true, true);

        RecordServletV1 recordServletV1 = injector.getInstance(RecordServletV1.class);
        servletContextHandler.addServlet(new ServletHolder(recordServletV1), "/api/v1/record/*");

        logger.info("Starting the server!!");
        jetty.start();
        jetty.join();
    }

}
