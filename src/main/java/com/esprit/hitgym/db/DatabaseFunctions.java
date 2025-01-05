package com.esprit.hitgym.db;

import com.esprit.hitgym.controller.customer.CustomerPanel_Controller;
import com.esprit.hitgym.controller.employees.EmployeesPanel_Controller;
import com.esprit.hitgym.helpers.CustomDate;
import com.esprit.hitgym.helpers.Login;
import com.esprit.hitgym.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseFunctions {

    private static final String dbUrl = "jdbc:mysql://localhost:3306/gym_db";
    private static final String dbUsername = "root";
    private static final String dbPassword = "password";


    private static Connection dbConnection = null;

    public static int customersListCount;
    public static int employeesListCount;
    public static int totalList;

    public static void initFunction() {
        makeConnection();

    }

    private static Map<String, Integer> getMonthMap() {
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("January", 1);
        monthMap.put("February", 2);
        monthMap.put("March", 3);
        monthMap.put("April", 4);
        monthMap.put("May", 5);
        monthMap.put("June", 6);
        monthMap.put("July", 7);
        monthMap.put("August", 8);
        monthMap.put("September", 9);
        monthMap.put("October", 10);
        monthMap.put("November", 11);
        monthMap.put("December", 12);
        return monthMap;
    }

    public static boolean makeConnection() {
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.out.println("Error! Could not connect to Db: " + e);
        }
        return true;
    }

    public static boolean saveToDb(Customer customer) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    insert into customers (id, first_name, last_name, email, phone_number, password, username, gender, weight, dob,
                    monthly_plan, cin, is_active, salt, address)
                    values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);""");

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
            queryStatement.setString(14, customer.getPasswordSalt());
            queryStatement.setString(15, customer.getAddress());
            queryStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }

    }

    public static boolean saveToDb(Transaction transaction) {
        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    INSERT INTO transactions (id, created_date, amount, transaction_number, bank_name, account_owner_name, fk_customer_id, status)
                    VALUE (?,?,?,?,?,?,?,?);""");

            queryStatement.setInt(1, transaction.getTransactionId());
            queryStatement.setDate(2, CustomDate.getCurrentDate());
            queryStatement.setInt(3, transaction.getAmount());
            queryStatement.setString(4, transaction.getTransactionNumber());
            queryStatement.setString(5, transaction.getBankName());
            queryStatement.setString(6, transaction.getAccountOwnerName());
            queryStatement.setInt(7, transaction.getFkCustomerId());
            queryStatement.setBoolean(8, transaction.isStatus());

            queryStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }
    }

    public static boolean saveToDb(Employee employee) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    INSERT INTO employees (id, first_name, last_name, designation, cin_number, salary, gender, phone_number, joining_date, username, password, salt, access,email)
                    VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?);""");

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
            queryStatement.setString(12, employee.getSalt());
            queryStatement.setInt(13, employee.getAccess());
            queryStatement.setString(14, employee.getEmail());

            queryStatement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }

    }

    public static boolean saveToDb(Expense expense, Integer fkEmployeeId) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    INSERT INTO expenses (id, description,created_date, amount, month, year, fk_employee_id, selected_date)
                    VALUE (?,?,?,?,?,?,?,?)
                    """);

            queryStatement.setInt(1, expense.getId());
            queryStatement.setString(2, expense.getDescription());
            queryStatement.setDate(3, CustomDate.getCurrentDate());
            queryStatement.setInt(4, expense.getAmount());
            queryStatement.setString(5, expense.getMonth());
            queryStatement.setString(6, expense.getYear());
            queryStatement.setObject(7, fkEmployeeId);
            queryStatement.setDate(8, expense.getSelectedDate());

            queryStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return true;
    }

    public static boolean saveToDb(Queries query) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    INSERT INTO queries (id, heading, email, description, created_date, username)
                    VALUE (?,?,?,?,?,?)""");

            queryStatement.setInt(1, query.getId());
            queryStatement.setString(2, query.getHeading());
            queryStatement.setString(3, query.getUsername());
            queryStatement.setString(4, query.getDescription());
            queryStatement.setDate(5, query.getCurrent_date());
            queryStatement.setString(6, query.getUsername());

            queryStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return false;
    }

    public static boolean saveToDb(BMI bmi, int fk_customer_id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    INSERT INTO defaultdb.bmi (id, weight, recorded_date, fk_customer_id, recorded_month, height, bmi_value)
                    VALUES (?,?,?,?,?,?,?);
                    """);

            queryStatement.setInt(1, bmi.getId());
            queryStatement.setDouble(2, bmi.getWeight());
            queryStatement.setDate(3, bmi.getRecordedDate());
            queryStatement.setInt(4, fk_customer_id);
            queryStatement.setString(5, bmi.getRecordedMonth());
            queryStatement.setDouble(6, bmi.getHeight());
            queryStatement.setDouble(7, bmi.getBMI());

            queryStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }

        return true;
    }

    public static boolean saveToDb(String reply, int id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    UPDATE queries
                    SET reply = ? AND status = true
                    WHERE id = ?
                    """);
            queryStatement.setString(1, reply);
            queryStatement.setInt(2, id);
            queryStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }

    public static boolean saveUpdateToDb(Revenue revenue) {

        PreparedStatement queryStatement = null;
        ResultSet revenueRs = null;
        int amountNew = 0;

        try {

            queryStatement = dbConnection.prepareStatement("""
                    SELECT * FROM revenues WHERE for_month = ? AND for_year = ?;
                    """);

            queryStatement.setString(1, revenue.getForMonth());
            queryStatement.setString(2, revenue.getForYear());

            revenueRs = queryStatement.executeQuery();

            while (revenueRs.next()) {
                amountNew = revenueRs.getInt("amount");
            }

            if (amountNew == 0) {

                queryStatement = dbConnection.prepareStatement("""
                        INSERT INTO revenues (id, for_month, for_year, updated_date, amount)
                        VALUES (?,?,?,?,?);
                        """);
                queryStatement.setInt(1, revenue.getId());
                queryStatement.setString(2, revenue.getForMonth());
                queryStatement.setString(3, revenue.getForYear());
                queryStatement.setDate(4, CustomDate.getCurrentDate());
                queryStatement.setInt(5, revenue.getAmount());

                queryStatement.executeUpdate();

            } else {
                queryStatement = dbConnection.prepareStatement("""
                        UPDATE revenues
                        SET amount = ?
                        WHERE for_month = ? AND for_year = ?;
                        """);


                queryStatement.setInt(1, amountNew + revenue.getAmount());
                queryStatement.setString(2, revenue.getForMonth());
                queryStatement.setString(3, revenue.getForYear());
                queryStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return true;
    }

    public static void updateCustomerPassword(String email, String[] password) {
        PreparedStatement queryStatement = null;
        try {
            queryStatement = dbConnection.prepareStatement("UPDATE customers SET password = ?, salt = ? WHERE email = ?");
            queryStatement.setString(1, password[1]);
            queryStatement.setString(2, password[0]);
            queryStatement.setString(3, email);
            queryStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void updateEmployeePassword(String email, String[] password) {
        PreparedStatement queryStatement = null;
        try {
            queryStatement = dbConnection.prepareStatement("UPDATE employees SET password = ?, salt = ? WHERE email = ?");
            queryStatement.setString(1, password[1]);
            queryStatement.setString(2, password[0]);
            queryStatement.setString(3, email);
            queryStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static boolean updateTransactionStatus(int transactionId) {

        PreparedStatement queryStatement = null;
        PreparedStatement queryStatement2 = null;
        int fkCustomerId = 0;
        int transactionAmount = 0;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    UPDATE transactions
                    SET status = true
                    WHERE id = ?;""");
            queryStatement.setInt(1, transactionId);

            queryStatement.executeUpdate();

            try {
                PreparedStatement queryStatement3 = dbConnection.prepareStatement("SELECT fk_customer_id FROM transactions WHERE id = ?");
                queryStatement3.setInt(1, transactionId);
                ResultSet resultSet = queryStatement3.executeQuery();

                while (resultSet.next()) {
                    fkCustomerId = resultSet.getInt("fk_customer_id");
                }

            } catch (SQLException e) {
                System.out.println("Error: " + e);
            }

            queryStatement2 = dbConnection.prepareStatement("""
                    UPDATE customers
                    SET is_active = true
                    WHERE id = ?;""");

            queryStatement2.setInt(1, fkCustomerId);
            queryStatement2.executeUpdate();

            queryStatement = dbConnection.prepareStatement("""
                    SELECT amount FROM transactions
                    WHERE fk_customer_id = ?;
                    """);
            queryStatement.setInt(1, fkCustomerId);

            ResultSet transactionAmountRs = queryStatement.executeQuery();

            while (transactionAmountRs.next()) {
                transactionAmount = transactionAmountRs.getInt(1);
            }

            Revenue revenue = new Revenue(DatabaseFunctions.generateId("revenues"), CustomDate.getCurrentMonth(), CustomDate.getCurrentYear(), transactionAmount);
            DatabaseFunctions.saveUpdateToDb(revenue);

            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }
    }

    public static boolean updateSalaryStatus(int employeeId, String email) {

        PreparedStatement queryStatement = null;
        ResultSet salaryRs = null;
        int employeeSalary = 0;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT salary FROM employees
                    WHERE id = ?
                    """);

            queryStatement.setInt(1, employeeId);

            salaryRs = queryStatement.executeQuery();

            while (salaryRs.next()) {
                employeeSalary = salaryRs.getInt("salary");
            }


            Revenue revenue = new Revenue(DatabaseFunctions.generateId("revenues"), CustomDate.getCurrentMonth(), CustomDate.getCurrentYear(), -employeeSalary);
            DatabaseFunctions.saveUpdateToDb(revenue);

        } catch (SQLException e) {
            System.out.println("error: " + e);
        }

        return true;

    }

    public static ResultSet getAllCustomers() {

        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                    WHERE current_status = true;
                    """);
            allDataRs = queryStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error in getting ids: " + e);
        }

        return allDataRs;
    }

    public static int[] getNumberOfMemberships() {

        ResultSet resultSet = null;
        PreparedStatement queryStatement = null;
        ArrayList<Integer> tempArr = new ArrayList<>();
        int[] allMemberships = new int[3];

        Package1 package1 = new Package1();
        Package2 package2 = new Package2();
        Package3 package3 = new Package3();

        try {
            queryStatement = dbConnection.prepareStatement("""
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

    public static ResultSet getQuery(String username) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = dbConnection.prepareStatement("""
                    SELECT * FROM queries WHERE  username = ?
                    """);

            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return resultSet;
    }

    public static ResultSet getAllEmployees() {
        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT id, first_name, last_name, designation, cin_number, salary, gender, phone_number, joining_date, username, access, email FROM employees
                    WHERE current_status = true;""");
            allDataRs = queryStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error in getting ids: " + e);
        }
        return allDataRs;
    }

    public static ResultSet getAllExpenses() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT id, description, amount, selected_date, month, year FROM expenses
                    WHERE current_status = true;""");
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;
    }

    public static ResultSet getAllQueries() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT * FROM queries;
                    """);
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;

    }

    public static ResultSet getAllTransactions() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT * FROM transactions;
                    """);
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;

    }

    public static ResultSet getAllRevenues() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT * FROM revenues;
                    """);
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;

    }

    public static ResultSet getAllBmiInfo() {

        PreparedStatement queryStatement = null;
        ResultSet bmiRs = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT * FROM bmi;
                    """);
            bmiRs = queryStatement.executeQuery();

            while (bmiRs.next()) {
                System.out.println(bmiRs.getInt(1));
                System.out.println(bmiRs.getString(2));
                System.out.println(bmiRs.getString(3));
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }

        return bmiRs;

    }

    public static int getCurrentMonthExpense() {

        PreparedStatement queryStatement = null;
        ResultSet allExpensesRs = null;
        int totalMonthlyExpense = 0;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT amount FROM expenses
                    WHERE month = ?;""");

            queryStatement.setString(1, CustomDate.getCurrentMonth());
            allExpensesRs = queryStatement.executeQuery();

            while (allExpensesRs.next()) {

                totalMonthlyExpense += allExpensesRs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        System.out.println(totalMonthlyExpense);
        return totalMonthlyExpense;
    }

    public static ArrayList<String> getUserPassword(String customerUsernameEmail) {

        ArrayList<String> saltPassArray = new ArrayList<>();

        switch (Login.queryOption) {
            case "username" -> {
                try {
                    PreparedStatement queryStatement = dbConnection.prepareStatement("SELECT * FROM customers WHERE current_status = true AND username = ?");
                    queryStatement.setString(1, customerUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    while (resultSet.next()) {
                        saltPassArray.add(resultSet.getString("salt"));
                        saltPassArray.add(resultSet.getString("password"));
                    }

                } catch (SQLException e) {
                    System.out.println("Error in retrieving customer: " + e);
                }
            }

            case "email" -> {
                try {
                    PreparedStatement queryStatement = dbConnection.prepareStatement("SELECT * FROM customers WHERE current_status = true AND email = ?");
                    queryStatement.setString(1, customerUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    while (resultSet.next()) {
                        saltPassArray.add(resultSet.getString("salt"));
                        saltPassArray.add(resultSet.getString("password"));
                    }


                } catch (SQLException e) {
                    System.out.println("Error in retrieving customer: " + e);
                }
            }
        }

        return saltPassArray;

    }

    public static ArrayList<String> getEmployeePassword(String employeeUsernameEmail) {

        ArrayList<String> saltPassArray = new ArrayList<>();

        switch (Login.queryOption) {
            case "username" -> {
                try {
                    PreparedStatement queryStatement = dbConnection.prepareStatement("SELECT * FROM employees WHERE username = ?");
                    queryStatement.setString(1, employeeUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    while (resultSet.next()) {
                        saltPassArray.add(resultSet.getString("salt"));
                        saltPassArray.add(resultSet.getString("password"));
                    }

                } catch (SQLException e) {
                    System.out.println("Error in retrieving customer: " + e);
                }
            }

            case "email" -> {
                try {
                    PreparedStatement queryStatement = dbConnection.prepareStatement("SELECT * FROM employees WHERE email = ?");
                    queryStatement.setString(1, employeeUsernameEmail);
                    ResultSet resultSet = queryStatement.executeQuery();

                    while (resultSet.next()) {
                        saltPassArray.add(resultSet.getString("salt"));
                        saltPassArray.add(resultSet.getString("password"));
                    }


                } catch (SQLException e) {
                    System.out.println("Error in retrieving customer: " + e);
                }
            }
        }

        return saltPassArray;

    }

    public static ArrayList<String> getAllUsernames() {

        ResultSet allUsernamesRs = null;
        PreparedStatement queryStatement = null;
        ArrayList<String> allUsernames = new ArrayList<>();


        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT username FROM customers
                    WHERE current_status = true;""");

            allUsernamesRs = queryStatement.executeQuery();

            while (allUsernamesRs.next()) {
                allUsernames.add(allUsernamesRs.getString(1));
                customersListCount++;
            }

        } catch (SQLException e) {
            System.out.println("Error in retrieving usernames: " + e);
        }

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT username FROM employees
                    WHERE current_status = true;""");

            allUsernamesRs = queryStatement.executeQuery();

            while (allUsernamesRs.next()) {
                allUsernames.add(allUsernamesRs.getString(1));
                employeesListCount++;
            }

        } catch (SQLException e) {
            System.out.println("Error in retrieving usernames: " + e);
        }

        return allUsernames;

    }

    public static ArrayList<String> getAllEmails() {

        ResultSet allEmailsRs = null;
        PreparedStatement queryStatement = null;
        ArrayList<String> allEmails = new ArrayList<>();


        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT email FROM customers
                    WHERE current_status = true;""");

            allEmailsRs = queryStatement.executeQuery();

            while (allEmailsRs.next()) {
                allEmails.add(allEmailsRs.getString(1));
                customersListCount++;
            }


        } catch (SQLException e) {
            System.out.println("Error in retrieving usernames: " + e);
        }

        try {
            queryStatement = dbConnection.prepareStatement("""
                    SELECT email FROM employees
                    WHERE current_status = true;""");

            allEmailsRs = queryStatement.executeQuery();

            while (allEmailsRs.next()) {
                allEmails.add(allEmailsRs.getString(1));
                employeesListCount++;
            }


        } catch (SQLException e) {
            System.out.println("Error in retrieving usernames: " + e);
        }


        return allEmails;
    }

    public static int getNumberOfCustomers() {

        PreparedStatement queryStatement = null;
        int allCustomers = 0;

        try {
            queryStatement = dbConnection.prepareStatement("""
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

    public static int getTotalList() {

        totalList = customersListCount + employeesListCount;
        return totalList;

    }

    public static void getLoggedInCustomer(String usernameEmail) {

        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        if (Login.queryOption.equals("email")) {

            try {
                queryStatement = dbConnection.prepareStatement("""
                        SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                        WHERE email = ? AND current_status = true;
                        """);
                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    CustomerPanel_Controller.Customer.setFirstName(allDataRs.getString("first_name"));
                    CustomerPanel_Controller.Customer.setLastName(allDataRs.getString("last_name"));
                    CustomerPanel_Controller.Customer.setCustomerId(allDataRs.getInt("id"));
                    CustomerPanel_Controller.Customer.setEmail(allDataRs.getString("email"));
                    CustomerPanel_Controller.Customer.setPhoneNumber(allDataRs.getString("phone_number"));
                    CustomerPanel_Controller.Customer.setUserName(allDataRs.getString("username"));
                    CustomerPanel_Controller.Customer.setGender(allDataRs.getString("gender"));
                    CustomerPanel_Controller.Customer.setWeight(allDataRs.getString("weight"));
                    CustomerPanel_Controller.Customer.setDob(allDataRs.getString("dob"));
                    CustomerPanel_Controller.Customer.setMonthlyPlan(allDataRs.getInt("monthly_plan"));
                    CustomerPanel_Controller.Customer.setCinNumber(allDataRs.getString("cin"));
                    CustomerPanel_Controller.Customer.setAddress(allDataRs.getString("address"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        } else if (Login.queryOption.equals("username")) {

            try {
                queryStatement = dbConnection.prepareStatement("""
                        SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                        WHERE username = ? AND current_status = true;
                        """);
                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    CustomerPanel_Controller.Customer.setFirstName(allDataRs.getString("first_name"));
                    CustomerPanel_Controller.Customer.setLastName(allDataRs.getString("last_name"));
                    CustomerPanel_Controller.Customer.setCustomerId(allDataRs.getInt("id"));
                    CustomerPanel_Controller.Customer.setEmail(allDataRs.getString("email"));
                    CustomerPanel_Controller.Customer.setPhoneNumber(allDataRs.getString("phone_number"));
                    CustomerPanel_Controller.Customer.setUserName(allDataRs.getString("username"));
                    CustomerPanel_Controller.Customer.setGender(allDataRs.getString("gender"));
                    CustomerPanel_Controller.Customer.setWeight(allDataRs.getString("weight"));
                    CustomerPanel_Controller.Customer.setDob(allDataRs.getString("dob"));
                    CustomerPanel_Controller.Customer.setMonthlyPlan(allDataRs.getInt("monthly_plan"));
                    CustomerPanel_Controller.Customer.setCinNumber(allDataRs.getString("cin"));
                    CustomerPanel_Controller.Customer.setAddress(allDataRs.getString("address"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        }
    }

    public static void getLoggedInEmployee(String usernameEmail) {

        ResultSet allDataRs = null;
        PreparedStatement queryStatement = null;

        if (Login.queryOption.equals("email")) {

            try {
                queryStatement = dbConnection.prepareStatement("""
                        SELECT id, first_name, last_name, designation, cin_number, salary, gender, phone_number, joining_date, username, access, email FROM employees
                        WHERE email = ? AND current_status = true;""");

                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    EmployeesPanel_Controller.employee.setId(allDataRs.getInt("id"));
                    EmployeesPanel_Controller.employee.setFirstName(allDataRs.getString("first_name"));
                    EmployeesPanel_Controller.employee.setLastName(allDataRs.getString("last_name"));
                    EmployeesPanel_Controller.employee.setDesignation(allDataRs.getString("designation"));
                    EmployeesPanel_Controller.employee.setCinNumber(allDataRs.getString("cin_number"));
                    EmployeesPanel_Controller.employee.setSalary(allDataRs.getInt("salary"));
                    EmployeesPanel_Controller.employee.setGender(allDataRs.getString("gender"));
                    EmployeesPanel_Controller.employee.setPhoneNumber(allDataRs.getString("phone_number"));
                    EmployeesPanel_Controller.employee.setUserName(allDataRs.getString("username"));
                    EmployeesPanel_Controller.employee.setEmail(allDataRs.getString("email"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        } else if (Login.queryOption.equals("username")) {

            try {
                queryStatement = dbConnection.prepareStatement("""
                        SELECT id, first_name, last_name, email, phone_number, username, gender, weight, dob, monthly_plan, cin, is_active, address FROM customers
                        WHERE username = ? AND current_status = true;
                        """);
                queryStatement.setString(1, usernameEmail);
                allDataRs = queryStatement.executeQuery();

                while (allDataRs.next()) {

                    EmployeesPanel_Controller.employee.setId(allDataRs.getInt("id"));
                    EmployeesPanel_Controller.employee.setFirstName(allDataRs.getString("first_name"));
                    EmployeesPanel_Controller.employee.setLastName(allDataRs.getString("last_name"));
                    EmployeesPanel_Controller.employee.setDesignation(allDataRs.getString("designation"));
                    EmployeesPanel_Controller.employee.setCinNumber(allDataRs.getString("cin_number"));
                    EmployeesPanel_Controller.employee.setSalary(allDataRs.getInt("salary"));
                    EmployeesPanel_Controller.employee.setGender(allDataRs.getString("gender"));
                    EmployeesPanel_Controller.employee.setPhoneNumber(allDataRs.getString("phone_number"));
                    EmployeesPanel_Controller.employee.setUserName(allDataRs.getString("username"));
                    EmployeesPanel_Controller.employee.setEmail(allDataRs.getString("email"));

                }

            } catch (SQLException e) {
                System.out.println("Error in getting ids: " + e);
            }

        }
    }

    public static boolean deleteData(String tableName, int id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    CALL delete_data(?,?);""");

            queryStatement.setString(1, tableName);
            queryStatement.setInt(2, id);

            queryStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("error in deleting: " + e);
        }

        return false;
    }

    public static int generateId(String choice) {

        ResultSet allIds = null;
        int lastId = 0;
        PreparedStatement queryStatement = null;

        try {
            queryStatement = dbConnection.prepareStatement("""
                    CALL get_ids(?)
                    """);
            queryStatement.setString(1, choice);
            allIds = queryStatement.executeQuery();
            while (allIds.next()) {
                lastId = allIds.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in getting ids: " + e);
        }
        return lastId + 1;

    }

    // get monthly revenues and expenses
    public static double[] getMonthlyRevenues() {
        double[] monthlyRevenues = new double[12];
        String query = "SELECT for_month, amount FROM revenues ORDER BY for_month";

        try (PreparedStatement statement = dbConnection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String monthName = resultSet.getString("for_month");
                double revenue = resultSet.getDouble("amount");
                System.out.println("revenue: " + revenue);
                int month = getMonthMap().get(monthName);
                monthlyRevenues[month - 1] = revenue; // Assuming month is 1-based (1 for January, 2 for February, etc.)
            }
        } catch (SQLException e) {
            System.out.println("Error fetching monthly revenues: " + e.getMessage());
        }

        return monthlyRevenues;
    }

    public static double[] getMonthlyExpenses() {
        double[] monthlyExpenses = new double[12];
        String query = "SELECT month, amount FROM expenses ORDER BY month";

        try (PreparedStatement statement = dbConnection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String monthName = resultSet.getString("month");
                double expense = resultSet.getDouble("amount");
                int month = getMonthMap().get(monthName);
                monthlyExpenses[month - 1] = expense; // Assuming month is 1-based (1 for January, 2 for February, etc.)
            }
        } catch (SQLException e) {
            System.out.println("Error fetching monthly expenses: " + e.getMessage());
        }
        return monthlyExpenses;
    }

}
