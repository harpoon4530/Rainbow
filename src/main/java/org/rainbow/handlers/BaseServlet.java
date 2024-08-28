package org.rainbow.handlers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class BaseServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    private static final int bufferLength = 65536;
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {

        String service = request.getAuthType();
        String authTokenHeader = request.getHeader("Authorization");

        String method = request.getMethod();

        String recordIdStr = request.getPathInfo();

        if (recordIdStr == null) {
            logger.error("Invalid recordId: {}", recordIdStr);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer recordId = null;
        try {
            recordId = getRecordId(recordIdStr);
        } catch (Exception e) {
            logger.error("Cannot extract recordId from: {}", recordIdStr);
        }

        switch(method) {
            case "GET":
                doGet("GET", recordId, request, response);
                break;
            case "POST":
                doPost("POST", recordId, request, response);
                break;
            default:
                throw new UnsupportedOperationException("We only support [GET|POST]");
        }
    }

    private Integer getRecordId(String str) {
        Integer recordId = null;
        try {
            recordId = Integer.parseInt(str.split("\\/")[1]);
        } catch (Exception e) {
            logger.error("Cannot parse recordId: {}", str);
            throw new RuntimeException();
        }
        return recordId;
    }

    public void doGet(String method, Integer recordId, HttpServletRequest request, HttpServletResponse response) {
        //timers etc
        try {
            doGet(recordId, request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(String method, Integer recordId, HttpServletRequest request, HttpServletResponse response) {

        try {
            doPost(recordId, request, response);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    public JSONObject readJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {

        InputStreamReader isr =  new InputStreamReader(request.getInputStream(),"utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder(bufferLength);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        br.close();
        isr.close();

        JSONObject readJson;
        String jsonString = buf.toString();
        try {
            readJson = new JSONObject(jsonString);
        } catch (JSONException e) {
            logger.error("Unable to parse the JSON string:{}", jsonString);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            return null;
        }
        logger.info("The read json is: {}", readJson.toString());
        return readJson;
    }

    public abstract void doGet(Integer recordId, HttpServletRequest request, HttpServletResponse response) throws IOException;

    public abstract void doPost(Integer recordId, HttpServletRequest request, HttpServletResponse response) throws IOException;

}