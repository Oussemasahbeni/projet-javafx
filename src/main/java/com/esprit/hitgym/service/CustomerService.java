package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Customer;
import com.esprit.hitgym.Entity.Package1;
import com.esprit.hitgym.Entity.Package2;
import com.esprit.hitgym.Entity.Package3;
import com.esprit.hitgym.controller.customer.CustomerPanelController;
import com.esprit.hitgym.helpers.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static com.esprit.hitgym.utils.Datasource.getConnection;


public class CustomerService {


    public boolean add(@NotNull Customer customer) {
        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO customers (id, first_name, last_name, email, phone_number, password, username, gender, weight, dob,
                    monthly_plan, cin, is_active, address)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);""");

            queryStatement.setInt(1, customer.getCustomerId());
            queryStatement.setString(2, customer.getFirstName());
            queryStatement.setString(3, customer.getLastName());
            queryStatement.setString(4, customer.getEmail());
            queryStatement.setString(5, customer.getPhoneNumber());
            queryStatement.setString(6, customer.getPassword());
            queryStatement.setString(7, customer.getUserName());
            queryStatement.setString(8, customer.getGender());
            queryStatement.setString(9, customer.getWeight());
            queryStatement.setString(10, customer.getDob());
            queryStatement.setInt(11, customer.getMonthlyPlan());
            queryStatement.setString(12, customer.getCinNumber());
            queryStatement.setBoolean(13, false);
            queryStatement.setString(14, customer.getAddress());
            queryStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }
    }

    public ResultSet findAllCustomers() {

        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                    WHERE current_status = true;
                    """);
            allDataRs = queryStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error in getting ids: " + e);
        }

        return allDataRs;
    }

    public ObservableList<Customer> findAllCustomersObs() {
        ObservableList<Customer> allDataRs = FXCollections.observableArrayList();

        String query = "SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers WHERE current_status = true";

        try (PreparedStatement queryStatement = getConnection().prepareStatement(query);
             ResultSet resultSet = queryStatement.executeQuery()) {

            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("gender"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("username"),
                        "", // Mot de passe non récupéré
                        resultSet.getString("cin"),
                        resultSet.getString("address"),
                        resultSet.getString("dob"),
                        resultSet.getString("weight"),
                        resultSet.getInt("monthly_plan"),
                        resultSet.getInt("id")
                );
                customer.setActive(resultSet.getBoolean("is_active"));
                allDataRs.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error in getting customers: " + e.getMessage());
        }

        return allDataRs;
    }



    public void updateCustomerPassword(String email, String password) {
        PreparedStatement queryStatement = null;
        try {
            queryStatement = getConnection().prepareStatement("UPDATE customers SET password = ? WHERE email = ?");
            queryStatement.setString(1, password);
            queryStatement.setString(2, email);
            queryStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public int findNumberOfCustomers() {

        PreparedStatement queryStatement = null;
        int allCustomers = 0;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT COUNT(id)
                    FROM customers
                    WHERE current_status = true;""");

            ResultSet customersRs = queryStatement.executeQuery();

            while (customersRs.next()) {
                allCustomers = customersRs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error in getting number of customers: " + e);
        }

        return allCustomers;
    }

    public int[] findNumberOfMemberships() {

        ResultSet resultSet = null;
        PreparedStatement queryStatement = null;
        ArrayList<Integer> tempArr = new ArrayList<>();
        int[] allMemberships = new int[3];

        Package1 package1 = new Package1();
        Package2 package2 = new Package2();
        Package3 package3 = new Package3();

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT monthly_plan
                    FROM customers
                    ORDER BY monthly_plan ASC;
                    """);

            resultSet = queryStatement.executeQuery();

            while (resultSet.next()) {

                tempArr.add(resultSet.getInt(1));

            }

            for (int i : tempArr) {
                if (i == package1.getAmount()) {
                    allMemberships[0] += 1;
                } else if (i == package2.getAmount()) {
                    allMemberships[1] += 1;
                } else if (i == package3.getAmount()) {
                    allMemberships[2] += 1;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error in getting memberships: " + e);
        }

        return allMemberships;
    }

    public String findUserPassword(String customerUsernameEmail) {
        String password = null;

        switch (Login.queryOption) {
            case "username" -> {
                try {
                    PreparedStatement queryStatement = getConnection().prepareStatement("SELECT password FROM customers WHERE current_status = true AND username = ?");
                    queryStatement.setString(1, customerUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    if (resultSet.next()) {
                        password = resultSet.getString("password");
                    }

                } catch (SQLException e) {
                    System.out.println("Error in retrieving customer: " + e);
                }
            }

            case "email" -> {
                try {
                    PreparedStatement queryStatement = getConnection().prepareStatement("SELECT password FROM customers WHERE current_status = true AND email = ?");
                    queryStatement.setString(1, customerUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    if (resultSet.next()) {
                        password = resultSet.getString("password");
                    }

                } catch (SQLException e) {
                    System.out.println("Error in retrieving customer: " + e);
                }
            }
        }

        return password;
    }

    public void findLoggedInCustomer(String usernameEmail) {

        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        if (Login.queryOption.equals("email")) {

            try {
                queryStatement = getConnection().prepareStatement("""
                        SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                        WHERE email = ? AND current_status = true;
                        """);
                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    CustomerPanelController.Customer.setFirstName(allDataRs.getString("first_name"));
                    CustomerPanelController.Customer.setLastName(allDataRs.getString("last_name"));
                    CustomerPanelController.Customer.setCustomerId(allDataRs.getInt("id"));
                    CustomerPanelController.Customer.setEmail(allDataRs.getString("email"));
                    CustomerPanelController.Customer.setPhoneNumber(allDataRs.getString("phone_number"));
                    CustomerPanelController.Customer.setUserName(allDataRs.getString("username"));
                    CustomerPanelController.Customer.setGender(allDataRs.getString("gender"));
                    CustomerPanelController.Customer.setWeight(allDataRs.getString("weight"));
                    CustomerPanelController.Customer.setDob(allDataRs.getString("dob"));
                    CustomerPanelController.Customer.setMonthlyPlan(allDataRs.getInt("monthly_plan"));
                    CustomerPanelController.Customer.setCinNumber(allDataRs.getString("cin"));
                    CustomerPanelController.Customer.setAddress(allDataRs.getString("address"));

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

                    CustomerPanelController.Customer.setFirstName(allDataRs.getString("first_name"));
                    CustomerPanelController.Customer.setLastName(allDataRs.getString("last_name"));
                    CustomerPanelController.Customer.setCustomerId(allDataRs.getInt("id"));
                    CustomerPanelController.Customer.setEmail(allDataRs.getString("email"));
                    CustomerPanelController.Customer.setPhoneNumber(allDataRs.getString("phone_number"));
                    CustomerPanelController.Customer.setUserName(allDataRs.getString("username"));
                    CustomerPanelController.Customer.setGender(allDataRs.getString("gender"));
                    CustomerPanelController.Customer.setWeight(allDataRs.getString("weight"));
                    CustomerPanelController.Customer.setDob(allDataRs.getString("dob"));
                    CustomerPanelController.Customer.setMonthlyPlan(allDataRs.getInt("monthly_plan"));
                    CustomerPanelController.Customer.setCinNumber(allDataRs.getString("cin"));
                    CustomerPanelController.Customer.setAddress(allDataRs.getString("address"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        }
    }

}
