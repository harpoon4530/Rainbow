package org.personio.db;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DbModule {

    private static final Logger logger = LoggerFactory.getLogger(DbModule.class);

    private final String jdbcUrl;
    private final String user;
    private final String password;

    public final String dbName;

    public final String directoryTable;

    private final Connection connection;

    @Inject
    public DbModule() throws SQLException {

        // Get database credentials from DatabaseConfig class
        this.jdbcUrl = DatabaseConfig.getDbUrl();
        this.user = DatabaseConfig.getDbUsername();
        this.password = DatabaseConfig.getDbPassword();

        this.dbName = "temp";
        this.directoryTable = "employee_directory";

        try {
            connection = DriverManager.getConnection(jdbcUrl, user, password);
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
        String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
        Statement st = null;

        try {
            Statement createDbStmt = connection.createStatement();
            int rs = createDbStmt.executeUpdate(createDbQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Statement stmt = connection.createStatement();
            stmt.execute("use " + dbName + ";");
            stmt.close();

            logger.info("Creating the table: {}", directoryTable);

            String createDirTableQuery = "CREATE TABLE IF NOT EXISTS " + dbName + "." + directoryTable +
                    " (ID BIGINT PRIMARY KEY" +
                    " NOT NULL AUTO_INCREMENT , employee VARCHAR(150), supervisor VARCHAR(150))";

            logger.info("Creating the db using the command: {}", createDirTableQuery);

            Statement createTableStmt = connection.createStatement();
            int rs = createTableStmt.executeUpdate(createDirTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
