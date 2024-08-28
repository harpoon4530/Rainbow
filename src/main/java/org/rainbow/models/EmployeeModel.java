package org.personio.models;

import jakarta.inject.Inject;
import org.apache.commons.dbcp.BasicDataSource;
import org.personio.db.DbModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
        //writeToDb();
        try {
            readAllFromDb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> generateHierarchy() throws SQLException {

        List<Employee> dbEmployees = this.readAllFromDb();
        Employee root = getRootEmployee(dbEmployees);

        Map<String, Object> hierarchy = new HashMap<>();

        hierarchy.put(root.getName(), generateSubHierarchy(dbEmployees, root.getName()));

        return hierarchy;
    }

    private Map<String, Object> generateSubHierarchy(List<Employee> employees, String supervisor) {
        Map<String, Object> hierarchy = new HashMap<>();
        for (Employee employee : employees) {
            if (employee.getSupervisor().equals(supervisor)) {
                Map<String, Object> subHierarchy = generateSubHierarchy(employees, employee.getName());
                hierarchy.put(employee.getName(), subHierarchy);
            }
        }
        return hierarchy;
    }

    public Employee getRootEmployee() throws SQLException {
        List<Employee> dbEmployees = this.readAllFromDb();
        return getRootEmployee(dbEmployees);
    }
    private Employee getRootEmployee(List<Employee> dbEmployees) throws SQLException {

        Set<String> supervisors = new HashSet<>();
        Set<String> employees = new HashSet<>();

        for (Employee emp : dbEmployees) {
            employees.add(emp.getName());
            supervisors.add(emp.getSupervisor());
        }

        String rootEmployee = findTopLevelEmployee(employees, supervisors);
        return new Employee(rootEmployee, null);
    }

    private String findTopLevelEmployee(Set<String> employees, Set<String> supervisors) {
        String root = null;

        Iterator<String> it = supervisors.iterator();
        while(it.hasNext()) {
            if (employees.contains(it.next())) {
                it.remove();
            }
        }

        if (supervisors.size() != 1) {
            // we have an issue here => throw an exception
            throw new RuntimeException("We couldn't find a root user!!!");
        }

        root = supervisors.iterator().next();
        logger.info("The root user is: {}", root);
        return root;
    }

    public Employee findNDeep(String user, int depth) throws Exception {

        Employee employee = findEmployee(user);
        String supervisor = employee.getSupervisor();

        if (depth == 0) {
            return employee;
        }

        if (depth == 1 || supervisor == null) {
            return new Employee(supervisor, null);
        } else {
            return findNDeep(supervisor, depth -1);
        }
    }

    public List<Employee> readAllFromDb() throws SQLException {

        List<Employee> empoyeeMapping = new ArrayList<Employee>();

        String readQuery = "SELECT * from " + dbModule.dbName + "." + dbModule.directoryTable;
        logger.info(readQuery);

        Statement stmt = dbModule.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(readQuery);

        try {
            while(rs.next()){
                int id = rs.getInt("id");
                String employee = rs.getString("employee");
                String supervisor = rs.getString("supervisor");
                //Display values
                logger.info("ID: {}, Employee: {}, Supervisor: {}", id, employee, supervisor);
                empoyeeMapping.add(new Employee(employee, supervisor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empoyeeMapping;
    }

    public Employee findEmployee(String findEmployee) throws Exception {

       Employee employeeJ = null;

        String query = "SELECT * FROM " + dbModule.dbName + "." + dbModule.directoryTable + " WHERE employee= '" + findEmployee  + "';" ;
        logger.info(query);
        Statement stmt = dbModule.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);

        try {
            while(rs.next()){
                int id = rs.getInt("id");
                String employee = rs.getString("employee");
                String supervisor = rs.getString("supervisor");
                //Display values
                logger.info("ID: {}, Employee: {}, Supervisor: {}", id, employee, supervisor);
                employeeJ = new Employee(employee, supervisor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (employeeJ == null) {
            throw new Exception("Invalid user sent; user does not exist in system: " + findEmployee);
        }

        return employeeJ;
    }

//    public String getSupervisors(String employeeName, int levels) {
//        Optional<Employee> employeeOptional = employeeRepository.findByName(employeeName);
//        if (employeeOptional.isPresent()) {
//            String supervisor = employeeOptional.get().getSupervisor();
//            if (levels == 1 || supervisor == null) {
//                return supervisor;
//            } else {
//                return getSupervisors(supervisor, levels - 1);
//            }
//        }
//        return null;
//    }



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
            // We do not want to close the connection; but good defence practice for standalone processs
            //dbModule.getConnection().close();
        }
    }

//    public void writeToDb() {
//
//        String insertQuery0 = "INSERT INTO employee_directory (employee, supervisor) VALUES ('One', 'Two')";
//        String insertQuery1 = "INSERT INTO employee_directory (employee, supervisor) VALUES ('Three', 'Four')";
//
//        try {
//            Statement stmt = dbModule.getConnection().createStatement();
//            stmt.executeUpdate(insertQuery0);
//            stmt.executeUpdate(insertQuery1);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        //System.err.println("Writing to the DB!!!");
//        //int i = 0;
//    }

}
