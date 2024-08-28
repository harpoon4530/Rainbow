package org.personio;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.personio.handlers.CartServlet;
import org.personio.handlers.DirectoryServlet;
import org.personio.security.BasicAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static Server jetty;
    public static void main(String[] args) throws Exception {

        jetty = new Server(8080);
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);

        Injector injector = Guice.createInjector();

        ServletContextHandler servletContextHandler =
                new ServletContextHandler(jetty, "/", true, true);

        //TODO: this should be removed from here; Setup auth; Basic cm9vdDpwYXNzd29yZA==
        servletContextHandler.setSecurityHandler(
                BasicAuth.basicAuth("root", "password", "PersonIO!"));

        //ServletHolder directoryHolder =
        //        servletContextHandler.addServlet(directoryHolder);
        DirectoryServlet directoryServlet = injector.getInstance(DirectoryServlet.class);
        servletContextHandler.addServlet(new ServletHolder(directoryServlet), "/directory/*");

        logger.info("Starting the server!!");
        jetty.start();
        jetty.join();

    }

}
