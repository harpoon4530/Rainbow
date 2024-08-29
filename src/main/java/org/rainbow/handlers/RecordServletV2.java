package org.rainbow.handlers;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rainbow.models.RecordModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RecordServletV2 extends BaseServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecordServletV2.class);

    private final RecordModel recordModel;

    @Inject
    public RecordServletV2(RecordModel recordModel) {
        super();
        this.recordModel = recordModel;
    }

    @Override
    public void doGet(Integer recordId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONArray jsonArray = recordModel.readFromDb(recordId);

        if (jsonArray == null) {
            // record not found;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        logger.info("Path: {}", request.getPathInfo());

        String[] parts = request.getPathInfo().split("/");

        JSONObject retVal = null;

        if (parts.length > 3) {
            //throw an exception here!
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Looking for the offset here;
        if (parts.length == 3) {
            int offset = Integer.parseInt(parts[2]);

            if (jsonArray.length() < offset) {
                // throw an exception here
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            } else {
                retVal = new JSONObject(jsonArray.get(offset).toString());
            }
        }

        if (parts.length == 2) {
            int length = jsonArray.length();
            // returns the latest
            //retVal = new JSONObject(jsonArray.get(length-1).toString());

            // returns all
            response.getWriter().println(jsonArray.toString());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(retVal.toString());

    }

    @Override
    public void doPost(Integer recordId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: throw exception here!
        //throw new ExecutionControl.NotImplementedException("");
    }

}
