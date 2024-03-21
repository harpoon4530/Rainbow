package org.personio.models;

import jakarta.inject.Inject;
import org.apache.commons.dbcp.BasicDataSource;
import org.personio.db.DbModule;
import org.personio.handlers.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class EmployeeModel {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeModel.class);
    private final BasicDataSource dataSource;

    DbModule dbModule;

    @Inject
    public EmployeeModel(DbModule dbModule) {

        this.dbModule = dbModule;

        // 1. connect to the db; if not present - throw exception and stop
        this.dataSource = new BasicDataSource();
        dbSetup();

    }

    private void dbSetup() {

        this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource.setInitialSize(5);
        this.dataSource.setMaxIdle(20);
        this.dataSource.setMaxActive(75);
        this.dataSource.setMaxWait(150);

        // use driver manager to execute queries
        writeToDb();
        try {
            readFromDb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void readFromDb() throws SQLException {
        String readQuery = "SELECT * from employee_directory";

        Statement stmt = dbModule.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(readQuery);

        try {
            while(rs.next()){
                //Display values
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", Employee: " + rs.getString("employee"));
                System.out.println(", Supervisor: " + rs.getString("supervisor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeToDb(Map<String, String> directoryData) throws SQLException {


        try {
            dbModule.getConnection().setAutoCommit(false);

            // Delete the previous data from the table
            String delete = "TRUNCATE " + dbModule.dbName + "." + dbModule.directoryTable;
            Statement deleteStmt = dbModule.getConnection().createStatement();
            deleteStmt.executeUpdate(delete);

            // Add new data to the table
            for (Map.Entry<String, String> entry : directoryData.entrySet()) {
                String row = "INSERT INTO " + dbModule.dbName + "." + dbModule.directoryTable +
                        "(employee, supervisor) VALUES ('" + entry.getKey() + "' , '" + entry.getValue() + "')";
                logger.info("Executing the statement: {}", row);
                Statement stmt = dbModule.getConnection().createStatement();
                stmt.executeUpdate(row);
            }
            dbModule.getConnection().commit();
        }
        catch(Exception e) {
            dbModule.getConnection().rollback();
            e.printStackTrace();
        }
        finally {
            //dbModule.getConnection().close();
        }
    }

    public void writeToDb() {

        String insertQuery0 = "INSERT INTO employee_directory (employee, supervisor) VALUES ('One', 'Two')";
        String insertQuery1 = "INSERT INTO employee_directory (employee, supervisor) VALUES ('Three', 'Four')";

        try {
            Statement stmt = dbModule.getConnection().createStatement();
            stmt.executeUpdate(insertQuery0);
            stmt.executeUpdate(insertQuery1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //System.err.println("Writing to the DB!!!");
        //int i = 0;
    }

}
