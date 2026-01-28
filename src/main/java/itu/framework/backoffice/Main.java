package itu.framework.backoffice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.itu.framework.FrontServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting application...");

        // Get port from environment variable (required for Heroku)
        int port = 8080; // default port for local development
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.trim().isEmpty()) {
            port = Integer.parseInt(portEnv);
        }
        System.out.println("Using port: " + port);

        Server server = new Server(port);

        // Use ServletContextHandler and let the framework's FrontServlet handle everything
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Configure the FrontServlet from the framework - it will handle controller scanning
        ServletHolder frontServletHolder = new ServletHolder(new FrontServlet());
        frontServletHolder.setInitParameter("controller-package", "itu.framework.backoffice.controllers");
        frontServletHolder.setInitParameter("view-prefix", "/WEB-INF/pages/");
        frontServletHolder.setInitParameter("view-suffix", ".jsp");

        context.addServlet(frontServletHolder, "/");

        server.setHandler(context);

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started successfully on port " + port);
        server.join();
    }
}