package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Revenue;
import com.esprit.hitgym.helpers.CustomDate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class RevenueService {

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


    public boolean saveUpdateToDb(Revenue revenue) {

        PreparedStatement queryStatement = null;
        ResultSet revenueRs = null;
        int amountNew = 0;


        try {

            queryStatement = getConnection().prepareStatement("""
                    SELECT * FROM revenues WHERE for_month = ? AND for_year = ?;
                    """);

            queryStatement.setString(1, revenue.getForMonth());
            queryStatement.setString(2, revenue.getForYear());

            revenueRs = queryStatement.executeQuery();

            while (revenueRs.next()) {
                amountNew = revenueRs.getInt("amount");
            }

            if (amountNew == 0) {

                queryStatement = getConnection().prepareStatement("""
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
                queryStatement = getConnection().prepareStatement("""
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

    public ResultSet getAllRevenues() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT * FROM revenues;
                    """);
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;

    }

    // get monthly revenues and expenses
    public double[] getMonthlyRevenues() {
        double[] monthlyRevenues = new double[12];
        String query = "SELECT for_month, amount FROM revenues ORDER BY for_month";

        try (PreparedStatement statement = getConnection().prepareStatement(query);
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

}
