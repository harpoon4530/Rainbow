package org.personio.handlers;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
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

@Singleton
public class DirectoryServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryServlet.class);

    private EmployeeModel employeeModel;
    @Inject
    private static EmployeeModel em;

    private static final int bufferLength = 65536;


//    public DirectoryServlet() {
//        int i = 0;
//        int j = 0;
//    }
    @Inject
    public DirectoryServlet(EmployeeModel employeeModel) {
        super();
        this.employeeModel = employeeModel;
//        try {
//            init();
//        } catch (ServletException e) {
//            throw new RuntimeException(e);
//        }
    }


    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response) {

        String path = request.getPathInfo();
        String[] pathParts = path.split("/");

        if (pathParts.length != 3) {
            logger.error("Invalid path given; cannot query the path: {}", path);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String employee = pathParts[1];
        int depth = Integer.valueOf(pathParts[2]);

        Employee supervisor = null;
        // Check the request here for servlet management
        try {

            List<String> chain = new ArrayList<String>();

            supervisor = employeeModel.findNDeep(employee, depth);

        } catch (SQLException e) {
            logger.error("Issues finding the user: {} to the depth: {}", employee, depth);
            //throw new RuntimeException(e);
            return;
        } catch (Exception e) {
            logger.error("The user {} is not in the system; hence we can't find the supervisor at the depth.{}", employee, depth);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        logger.info("Serviced a request here!!!! Would be good to add metrics in here!!!");

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println("{ \"supervisor\": \"" + supervisor.getName() + "\"}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws IOException {

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


