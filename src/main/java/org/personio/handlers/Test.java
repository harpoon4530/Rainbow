package org.personio.handlers;


import com.google.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import org.personio.models.Employee;
import org.personio.models.EmployeeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Test extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private final EmployeeModel employeeModel;

    private static final int bufferLength = 65536;
    @Inject
    public Test(EmployeeModel employeeModel) {
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
            String l0 = findNDeep("Pete", 0);
            System.err.println("L0 ==========> " + l0);

            employeeModel.readAllFromDb();
            //employeeModel.writeToDb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("The user {} is not in the system; hence we can't find the supervisor.");

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

    public String findNDeep(String user, int depth) throws Exception {

        Employee employee = employeeModel.findEmployee(user);
        String supervisor = employee.getSupervisor();

        if (depth == 0) {
            return user;
        }

        if (depth == 1 || supervisor == null) {
            return supervisor;
        } else {
            return findNDeep(supervisor, depth -1);
        }
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

        try {
            readJson = new JSONObject(jsonString);
            processValidJson(readJson);
        } catch (JSONException e) {
            logger.error("Unable to parse the JSON string:{}", jsonString);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            return;
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
    }

    private final void processValidJson(JSONObject json) {

        Map<String, String> jsonMap = new HashMap<>();

        Iterator<String> keys = json.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            String value = json.getString(key);
            jsonMap.put(key, value);
            logger.info("Record: employee:{}; supervisor:{}", key, value);
        }
        writeValidJsonToDB(jsonMap);
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


