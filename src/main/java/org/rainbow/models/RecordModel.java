package org.rainbow.models;

import com.google.inject.Inject;
import org.rainbow.db.DbModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecordModel {

    private static final Logger logger = LoggerFactory.getLogger(RecordModel.class);

    private DbModule dbModule;

    @Inject
    public RecordModel(DbModule dbModule) {
        this.dbModule = dbModule;
    }


    public void writeToDb(String data) {

        String row = "INSERT INTO " + dbModule.recordTable + " (record) VALUES ('" + data + "')";
        logger.info("Executing the query: {}", row);

        try {
            Statement stmt = dbModule.getConnection().createStatement();
            stmt.executeUpdate(row);

            stmt = dbModule.getConnection().createStatement();
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
