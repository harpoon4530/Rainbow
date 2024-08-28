package org.rainbow.handlers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {

        String service = request.getAuthType();
        String authTokenHeader = request.getHeader("Authorization");

        // Implement the shop cart functionality.
        String method = request.getMethod();
        switch(method) {
            case "GET":
                doGet("GET", request, response);
                break;
            case "POST":
                doPost("POST", request, response);
                break;
            default:
                throw new UnsupportedOperationException("We only support [GET|POST]");
        }
    }

    public void doGet(String method, HttpServletRequest request, HttpServletResponse response) {
        //timers etc
        try {
            doGet(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(String method, HttpServletRequest request, HttpServletResponse response) {


        try {
            doPost(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;

    public abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;




}