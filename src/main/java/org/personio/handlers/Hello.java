package org.personio.handlers;


import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.personio.models.EmployeeModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

public class Hello extends BaseServlet {

    private final EmployeeModel employeeModel;
    @Inject
    public Hello(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");

        try {
            employeeModel.readFromDb();
            employeeModel.writeToDb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        System.err.println("Serviced a request here!!!!");
    }

    @Override
    public void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        InputStreamReader isr =  new InputStreamReader(request.getInputStream(),"utf-8");
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder(2024);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        br.close();
        isr.close();

        JSONObject readJson;
        String jsonString = buf.toString();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
    }
}
