package org.personio;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.personio.handlers.Hello;
import org.personio.handlers.Test;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.util.EnumSet;

public class App {

    private static Server jetty;
    public static void main(String[] args) throws Exception {

        jetty = new Server(8080);

        ServletContextHandler servletContextHandler = new ServletContextHandler(jetty, "/", true, false);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{servletContextHandler});
        jetty.setHandler(handlers);

        Injector injector = Guice.createInjector();

        Hello hello = injector.getInstance(Hello.class);
        servletContextHandler.addServlet(new ServletHolder(hello), "/hello");

        Test test = injector.getInstance(Test.class);
        servletContextHandler.addServlet(new ServletHolder(test), "/test");

        //servletContextHandler.addServlet(new ServletHolder((hello), ""));

        System.err.println("Starting the server!!");
        jetty.start();
        jetty.join();

    }

}
