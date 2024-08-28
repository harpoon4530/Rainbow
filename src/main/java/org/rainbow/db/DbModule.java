package org.rainbow.db;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DbModule {

    private static final Logger logger = LoggerFactory.getLogger(DbModule.class);

    private final String jdbcUrl;
    //private final String user;
    //private final String password;

    public final String dbName;

    public final String recordTable;

    private final Connection connection;

    @Inject
    public DbModule() throws SQLException {

        // Get database credentials from DatabaseConfig class
        this.jdbcUrl = DatabaseConfig.getDbUrl();
        //this.user = DatabaseConfig.getDbUsername();
        //this.password = DatabaseConfig.getDbPassword();

        this.dbName = "rainbow";
        this.recordTable = "record";

        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (Exception e) {
            logger.error("The database does not exist!!!!");
            throw new RuntimeException("Cannot connect to DB; " + e.getMessage());
        }

        createDatabaseAndTables();
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void createDatabaseAndTables() {
        try {
            logger.info("Creating the table: {}", recordTable);

            String createDirTableQuery = "CREATE TABLE IF NOT EXISTS " + recordTable +
                    " (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                    ", record VARCHAR(65536))";

            logger.info("Creating the db using the command: {}", createDirTableQuery);

            Statement createTableStmt = connection.createStatement();
            int rs = createTableStmt.executeUpdate(createDirTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
