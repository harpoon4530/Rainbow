package org.personio.db;

import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.*;
import java.util.Properties;

public class DbModule {

    private final Properties properties;

    private final String jdbcUrl;
    private final String user;
    private final String password;

    private final String dbName;

    private final Connection connection;

    @Inject
    public DbModule() throws SQLException {
        this.properties = new Properties();

        // Get database credentials from DatabaseConfig class
        this.jdbcUrl = DatabaseConfig.getDbUrl();
        this.user = DatabaseConfig.getDbUsername();
        this.password = DatabaseConfig.getDbPassword();
        this.dbName = "temp";

        try {
            connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch ( MySQLSyntaxErrorException e) {
            System.out.println("The database does now exist!!!!");
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

        // id MEDIUMINT not null AUTO_INCREMENT
        String createDirTableQuery = "CREATE TABLE IF NOT EXISTS employee_directory (ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT , employee VARCHAR(150), supervisor VARCHAR(150))";

        try {
            Statement createTableStmt = connection.createStatement();
            int rs = createTableStmt.executeUpdate(createDirTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
