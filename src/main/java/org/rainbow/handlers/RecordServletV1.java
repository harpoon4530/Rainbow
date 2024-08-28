package org.rainbow.handlers;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.rainbow.models.RecordModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RecordServletV1 extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecordServletV1.class);

    private final RecordModel recordModel;

    @Inject
    public RecordServletV1(RecordModel recordModel) {
        super();
        this.recordModel = recordModel;
    }

    @Override
    public void doGet(Integer recordId, HttpServletRequest request, HttpServletResponse response) throws IOException {

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

    @Override
    public void doPost(Integer recordId, HttpServletRequest request, HttpServletResponse response) throws IOException {

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
