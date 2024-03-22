package org.personio.handlers;


import com.google.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import org.personio.models.Employee;
import org.personio.models.EmployeeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Hello extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(Hello.class);

    private final EmployeeModel employeeModel;

    private static final int bufferLength = 65536;
    @Inject
    public Hello(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        // Check the request here for servlet management

        try {
            //List<String> t1 = employeeModel.findEmployee("Sophie");
            //List<String> t2 = employeeModel.findEmployee("Foobar");
            Employee l0 = employeeModel.findNDeep("Pete", 0);
            System.err.println("L0 ==========> " + l0);

            Employee l1 = employeeModel.findNDeep("Pete", 1);
            System.err.println("L1 ==========> " + l1);

            Employee l2 = employeeModel.findNDeep("Pete", 2);
            System.err.println("L2 ==========> " + l2);

            Employee l3 = employeeModel.findNDeep("Pete", 3);
            System.err.println("L3 ==========> " + l3);

            Employee l4 = employeeModel.findNDeep("Pete", 4);
            System.err.println("L4 ==========> " + l4);

            Employee l5 = employeeModel.findNDeep("Pete", 5);
            System.err.println("L5 ==========> " + l5);

            Employee n1 = employeeModel.findNDeep("Nick", 1);
            System.err.println("N1 ==========> " + n1);


            employeeModel.readAllFromDb();
            //employeeModel.writeToDb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("The user {} is not in the system; hence we can't find the supervisor at the depth.");

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{ \"status\": \"ok\"}");

            throw new RuntimeException(e);
        }

        logger.info("Serviced a request here!!!!");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
    }

    @Override
    public void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

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

        JSONObject jsonObject = new JSONObject();
        try {
            readJson = new JSONObject(jsonString);
            jsonObject = processValidJson(readJson);
        } catch (JSONException e) {
            logger.error("Unable to parse the JSON string:{}", jsonString);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            return;
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonObject.toString());
        //response.getWriter().println("{ \"status\": \"ok\"}");
    }

    private final JSONObject processValidJson(JSONObject json) {

        Map<String, String> jsonMap = new HashMap<>();

        Iterator<String> keys = json.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            String value = json.getString(key);
            jsonMap.put(key, value);
            logger.info("Record: employee:{}; supervisor:{}", key, value);
        }
        writeValidJsonToDB(jsonMap);

        JSONObject jsonObject = null;
        try {
            Map<String, Object> hierarchy = employeeModel.generateHierarchy();
            jsonObject = new JSONObject(hierarchy);
            logger.info("Hiearchy: {}", jsonObject.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    private void writeValidJsonToDB(Map<String, String> jsonMap) {
        try {
            employeeModel.writeToDb(jsonMap);
        } catch (SQLException e) {
            logger.error("Error writing to db");
            throw new RuntimeException(e);
        }
    }
}


