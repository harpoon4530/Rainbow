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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);

    private static final int bufferLength = 65536;
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

        //JSONObject jsonObject = new JSONObject();
        try {
            readJson = new JSONObject(jsonString);
            //jsonObject = processValidJson(readJson);
        } catch (JSONException e) {
            logger.error("Unable to parse the JSON string:{}", jsonString);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            return null;
        }
        logger.info("The read json is: {}", readJson.toString());
        return readJson;
    }

//    private final JSONObject processValidJson(JSONObject json) {
//
//        Map<String, String> jsonMap = new HashMap<>();
//
//        Iterator<String> keys = json.keys();
//
//        while(keys.hasNext()) {
//            String key = keys.next();
//            String value = json.getString(key);
//            jsonMap.put(key, value);
//            logger.info("Record: employee:{}; supervisor:{}", key, value);
//        }
//        //writeValidJsonToDB(jsonMap);
//
//        JSONObject jsonObject = null;
//        return jsonObject;
//    }

    public abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException;

    public abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;




}