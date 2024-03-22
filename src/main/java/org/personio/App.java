package org.personio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.personio.handlers.Hello;
import org.personio.handlers.Test;
import org.personio.security.BasicAuth;

public class App {

    private static Server jetty;
    public static void main(String[] args) throws Exception {

        jetty = new Server(8080);

        ServletContextHandler servletContextHandler = new ServletContextHandler(jetty, "/", true, true);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletContextHandler});
        jetty.setHandler(handlers);

        Injector injector = Guice.createInjector();

        Hello hello = injector.getInstance(Hello.class);
        servletContextHandler.addServlet(new ServletHolder(hello), "/hello");


        // Setup auth; Basic dGVzdDp1c2Vy
        servletContextHandler.setSecurityHandler(BasicAuth.basicAuth("test", "user", "Private!"));

        Test test = injector.getInstance(Test.class);
        servletContextHandler.addServlet(new ServletHolder(test), "/test/*");




        System.err.println("Starting the server!!");
        jetty.start();
        jetty.join();

    }

}
