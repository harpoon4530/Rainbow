package org.rainbow.handlers;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.rainbow.models.RecordModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RecordServlet extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecordServlet.class);

    private final RecordModel recordModel;

    @Inject
    public RecordServlet(RecordModel recordModel) {
        super();
        this.recordModel = recordModel;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

        JSONObject jsonObject = recordModel.readFromDb(recordId);

        if (jsonObject == null) {
            // record not found;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonObject.toString());

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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

        JSONObject json = readJSON(request, response);
        if (json == null) {
            logger.error("Unable to parse JSON");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        logger.info("The read json string is: {}", json);

        recordModel.writeToDb(recordId, json.toString());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\", \"id\": "  + recordId + "}");
    }

}
