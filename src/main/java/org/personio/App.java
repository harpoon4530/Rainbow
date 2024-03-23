package org.personio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.personio.handlers.Directory;
import org.personio.security.BasicAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static Server jetty;
    public static void main(String[] args) throws Exception {

        jetty = new Server(8080);

        ServletContextHandler servletContextHandler =
                new ServletContextHandler(jetty, "/", true, true);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletContextHandler});
        jetty.setHandler(handlers);

        Injector injector = Guice.createInjector();

        Directory directory = injector.getInstance(Directory.class);
        servletContextHandler.addServlet(new ServletHolder(directory), "/directory/*");

        //TODO: this should be removed from here; Setup auth; Basic dGVzdDp1c2Vy
        servletContextHandler.setSecurityHandler(
                BasicAuth.basicAuth("test", "user", "Private!"));

        logger.info("Starting the server!!");
        jetty.start();
        jetty.join();

    }

}
