package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Expense;
import com.esprit.hitgym.helpers.CustomDate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class ExpensesService {

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

    public boolean add(Expense expense, Integer fkEmployeeId) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO expenses (id, description, created_date, amount, month, year, fk_employee_id, selected_date)
                    VALUES (?,?,?,?,?,?,?,?)
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

    public ResultSet findAllExpenses() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT id, description, amount, selected_date, month, year FROM expenses
                    WHERE current_status = true;""");
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;
    }

    public int findCurrentMonthExpense() {

        PreparedStatement queryStatement = null;
        ResultSet allExpensesRs = null;
        int totalMonthlyExpense = 0;

        try {
            queryStatement = getConnection().prepareStatement("""
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

    public double[] getMonthlyExpenses() {
        double[] monthlyExpenses = new double[12];
        String query = "SELECT month, amount FROM expenses ORDER BY month";

        try (PreparedStatement statement = getConnection().prepareStatement(query);
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
