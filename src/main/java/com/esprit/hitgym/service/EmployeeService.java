package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Employee;
import com.esprit.hitgym.Entity.Revenue;
import com.esprit.hitgym.controller.employees.EmployeesPanelController;
import com.esprit.hitgym.helpers.CustomDate;
import com.esprit.hitgym.helpers.Login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class EmployeeService {

    private final RevenueService revenueService;

    public EmployeeService() {
        this.revenueService = new RevenueService();
    }


    public boolean add(Employee employee) {
        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO employees (id, first_name, last_name, designation, cin_number, salary, gender, phone_number, joining_date, username, password, access, email)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);""");

            queryStatement.setInt(1, employee.getId());
            queryStatement.setString(2, employee.getFirstName());
            queryStatement.setString(3, employee.getLastName());
            queryStatement.setString(4, employee.getDesignation());
            queryStatement.setString(5, employee.getCinNumber());
            queryStatement.setInt(6, employee.getSalary());
            queryStatement.setString(7, employee.getGender());
            queryStatement.setString(8, employee.getPhoneNumber());
            queryStatement.setDate(9, CustomDate.getCurrentDate());
            queryStatement.setString(10, employee.getUserName());
            queryStatement.setString(11, employee.getPassword());
            queryStatement.setInt(12, employee.getAccess());
            queryStatement.setString(13, employee.getEmail());

            queryStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }
    }

    public ResultSet findAllEmployees() {
        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT id, first_name, last_name, designation, cin_number, salary, gender, phone_number, joining_date, username, access, email FROM employees
                    WHERE current_status = true;""");
            allDataRs = queryStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error in getting ids: " + e);
        }
        return allDataRs;
    }

    public void updateEmployeePassword(String email, String password) {
        PreparedStatement queryStatement = null;
        try {
            queryStatement = getConnection().prepareStatement("UPDATE employees SET password = ? WHERE email = ?");
            queryStatement.setString(1, password);
            queryStatement.setString(2, email);
            queryStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public boolean updateSalaryStatus(int employeeId, String email) {

        PreparedStatement queryStatement = null;
        ResultSet salaryRs = null;
        int employeeSalary = 0;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT salary FROM employees
                    WHERE id = ?
                    """);

            queryStatement.setInt(1, employeeId);

            salaryRs = queryStatement.executeQuery();

            while (salaryRs.next()) {
                employeeSalary = salaryRs.getInt("salary");
            }


            Revenue revenue = new Revenue(CommonService.generateId("revenues"), CustomDate.getCurrentMonth(), CustomDate.getCurrentYear(), -employeeSalary);
            this.revenueService.saveUpdateToDb(revenue);

        } catch (SQLException e) {
            System.out.println("error: " + e);
        }

        return true;

    }

    public String findEmployeePassword(String employeeUsernameEmail) {
        String password = null;

        switch (Login.queryOption) {
            case "username" -> {
                try {
                    PreparedStatement queryStatement = getConnection().prepareStatement("SELECT password FROM employees WHERE username = ?");
                    queryStatement.setString(1, employeeUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    if (resultSet.next()) {
                        password = resultSet.getString("password");
                    }

                } catch (SQLException e) {
                    System.out.println("Error in retrieving employee: " + e);
                }
            }

            case "email" -> {
                try {
                    PreparedStatement queryStatement = getConnection().prepareStatement("SELECT password FROM employees WHERE email = ?");
                    queryStatement.setString(1, employeeUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    if (resultSet.next()) {
                        password = resultSet.getString("password");
                    }

                } catch (SQLException e) {
                    System.out.println("Error in retrieving employee: " + e);
                }
            }
        }

        return password;
    }

    public void findLoggedInEmployee(String usernameEmail) {

        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        if (Login.queryOption.equals("email")) {

            try {
                queryStatement = getConnection().prepareStatement("""
                        SELECT id, first_name, last_name, designation, cin_number, salary, gender, phone_number, joining_date, username, access, email FROM employees
                        WHERE email = ? AND current_status = true;""");

                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    EmployeesPanelController.employee.setId(allDataRs.getInt("id"));
                    EmployeesPanelController.employee.setFirstName(allDataRs.getString("first_name"));
                    EmployeesPanelController.employee.setLastName(allDataRs.getString("last_name"));
                    EmployeesPanelController.employee.setDesignation(allDataRs.getString("designation"));
                    EmployeesPanelController.employee.setCinNumber(allDataRs.getString("cin_number"));
                    EmployeesPanelController.employee.setSalary(allDataRs.getInt("salary"));
                    EmployeesPanelController.employee.setGender(allDataRs.getString("gender"));
                    EmployeesPanelController.employee.setPhoneNumber(allDataRs.getString("phone_number"));
                    EmployeesPanelController.employee.setUserName(allDataRs.getString("username"));
                    EmployeesPanelController.employee.setEmail(allDataRs.getString("email"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        } else if (Login.queryOption.equals("username")) {

            try {
                queryStatement = getConnection().prepareStatement("""
                        SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                        WHERE username = ? AND current_status = true;
                        """);
                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    EmployeesPanelController.employee.setId(allDataRs.getInt("id"));
                    EmployeesPanelController.employee.setFirstName(allDataRs.getString("first_name"));
                    EmployeesPanelController.employee.setLastName(allDataRs.getString("last_name"));
                    EmployeesPanelController.employee.setDesignation(allDataRs.getString("designation"));
                    EmployeesPanelController.employee.setCinNumber(allDataRs.getString("cin_number"));
                    EmployeesPanelController.employee.setSalary(allDataRs.getInt("salary"));
                    EmployeesPanelController.employee.setGender(allDataRs.getString("gender"));
                    EmployeesPanelController.employee.setPhoneNumber(allDataRs.getString("phone_number"));
                    EmployeesPanelController.employee.setUserName(allDataRs.getString("username"));
                    EmployeesPanelController.employee.setEmail(allDataRs.getString("email"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        }
    }

    public List<String> getAllEmployeeEmails() {
        List<String> emails = new ArrayList<>();
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;

        try {
            queryStatement = getConnection().prepareStatement("SELECT email FROM employees");
            resultSet = queryStatement.executeQuery();

            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving employee emails: " + e);
        }

        return emails;
    }

}
