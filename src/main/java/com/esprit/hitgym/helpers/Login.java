package com.esprit.hitgym.helpers;

import com.esprit.hitgym.service.CustomerService;
import com.esprit.hitgym.service.EmployeeService;

import java.util.ArrayList;

public class Login {

    private ArrayList<String>[] allUsernamesEmails;
    private String password;
    private String emailUsername;
    private boolean logInSuccessful;
    public static String queryOption;

    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final Password Password = new Password();

    public Login() {
        this.customerService = new CustomerService();
        this.employeeService = new EmployeeService();
    }

    public boolean checkUsernameEmail(String type) {

        if (emailUsername.contains("@")) {
            queryOption = "email";
        } else {
            queryOption = "username";
        }

        switch (queryOption) {
            case "email" -> {

                boolean tmp = Email.checkIfCustomerOrEmployee(type);
                if (tmp) {
                    System.out.println("Email exists");
                    return false;
                } else {
                    return true;
                }
            }
            case "username" -> {
                boolean tmp1 = Username.checkUsername(emailUsername);
                if (tmp1) {
                    System.out.println("Username exists");
                    return false;
                } else {
                    return true;
                }
            }
        }

        return true;
    }

    public void checkPassword() {
        if (Password.verifyPassword(emailUsername, password)) {
            logInSuccessful = true;
        }

    }

    public boolean userLoggedInStatus() {

        checkPassword();

        if (logInSuccessful) {
            System.out.println("User logged in successfully");

            if (Password.isCustomerOrEmployee.equals("customer")) {
                customerService.findLoggedInCustomer(emailUsername);
            } else if (Password.isCustomerOrEmployee.equals("employee")) {
                employeeService.findLoggedInEmployee(emailUsername);
            }

            return true;
        } else {
            return false;
        }

    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
