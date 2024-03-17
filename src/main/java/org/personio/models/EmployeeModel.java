package org.personio.models;

import jakarta.inject.Inject;
import org.apache.commons.dbcp.BasicDataSource;
import org.personio.db.DbModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeModel {

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


        System.err.println("Writing to the DB!!!");
        int i = 0;
    }

}
