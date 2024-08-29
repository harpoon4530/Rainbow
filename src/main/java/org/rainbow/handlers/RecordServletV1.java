package org.rainbow.handlers;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
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

        JSONArray jsonArray = recordModel.readFromDb(recordId);

        if (jsonArray == null) {
            // record not found;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // return the last object only
        int lastIndex = jsonArray.length();
        JSONObject lastObject = new JSONObject(jsonArray.get(lastIndex-1).toString());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(lastObject.toString());

    }

    private JSONObject applyDiff(JSONObject existing, JSONObject incoming) {
        for (String key : incoming.keySet()) {
            if (incoming.isNull(key)) {
                existing.remove(key);
            } else {
                existing.put(key, incoming.get(key));
            }
        }
        return existing;
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

        JSONObject newObject = null;

        // read existing value and apply diff if exists
        JSONArray existingObjectArr = recordModel.readFromDb(recordId);
        JSONArray existingObjectClone = existingObjectArr == null ? null : new JSONArray(existingObjectArr.toString());

        //JSONArray existingObjectClone = recordModel.readFromDb(recordId);

        if (existingObjectArr == null) {
            logger.info("The existing object is null");
            // save the record as is
            newObject = json;
        } else {
            logger.info("The existing object is: {}", existingObjectArr.toString());
            // apply the diff to the last item
            int lastIndex = existingObjectArr.length();
            JSONObject lastObject = new JSONObject(existingObjectArr.get(lastIndex-1).toString());
            logger.info("The last object is: " + lastObject);

            JSONObject updatedJSON = applyDiff(lastObject, json);
            newObject = updatedJSON;
        }

        // do a CAS update!
        recordModel.writeToDb(recordId, newObject, existingObjectClone);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\", \"id\": "  + recordId + ", \"data\": " + newObject  + "}");
    }

}
