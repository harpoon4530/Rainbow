package org.rainbow.models;

import com.google.inject.Inject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rainbow.db.DbModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecordModel {

    private static final Logger logger = LoggerFactory.getLogger(RecordModel.class);

    private DbModule dbModule;

    /*
    sqlite3 /tmp/rainbow.db
    insert or replace into record ("id", "record") values (1, "hello");
     */

    @Inject
    public RecordModel(DbModule dbModule) {
        this.dbModule = dbModule;
    }

    public JSONArray readFromDb(Integer id) {

        JSONObject json = null;
        JSONArray jsonArray = null;
        String sql = "SELECT * FROM " +  dbModule.recordTable + " WHERE id = ?";

        try {
             PreparedStatement pstmt = dbModule.getConnection().prepareStatement(sql);

            pstmt.setInt(1, id);
            logger.info("Executing the query: {}", pstmt.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Assuming the table has columns id, record
                int recordId = rs.getInt("id");
                String data = rs.getString("record");

                logger.info("For id: {} we have the data: {}", id, data);
                //json = new JSONObject(data);
                jsonArray = new JSONArray(data);
            }
        } catch (SQLException e) {
            logger.error("Unable to execute query");
        }

        if (json == null) {
            logger.error("Unable to find any data for the id: {}", id);
        }

        return jsonArray;
    }

    public void writeToDb(Integer id, JSONObject newData, JSONArray oldData) {

        String row;
        if (oldData == null) {
            JSONArray arr = new JSONArray().put(newData);
            row = "INSERT OR REPLACE INTO " + dbModule.recordTable +
                    " (id, record) VALUES (" + id + ",'" + arr.toString() + "')";
        } else {

            JSONArray updatedData =  new JSONArray(oldData.toString());
            logger.info(updatedData.toString());
            updatedData.put(newData);

            row = "UPDATE " + dbModule.recordTable + " SET record = '" +  updatedData.toString() +
                    "' WHERE id = " + id  + " AND record = '" + oldData.toString()  + "';" ;
        }
        logger.info("Executing the query: {}", row);

        try {
            Statement stmt = dbModule.getConnection().createStatement();
            logger.info("Executing the query: {}", stmt.toString());

            int result = stmt.executeUpdate(row);

            // TODO: if (result == 0); means concurrent modification; think about handling that

            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");

            if (rs.next()) {
                long lastInsertedId = rs.getLong(1);
                logger.info("Last inserted ID: " + lastInsertedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
