package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Revenue;
import com.esprit.hitgym.helpers.CustomDate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class RevenueService {


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

    public double[] getMonthlyRevenues() {
        double[] monthlyRevenues = new double[12];
        String query = "SELECT MONTH(updated_date) AS month, SUM(amount) AS revenue FROM revenues GROUP BY MONTH(updated_date) ORDER BY MONTH(updated_date)";

        try (PreparedStatement statement = getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int month = resultSet.getInt("month");
                double revenue = resultSet.getDouble("revenue");
                monthlyRevenues[month - 1] = revenue; // Assuming month is 1-based (1 for January, 2 for February, etc.)
            }
        } catch (SQLException e) {
            System.out.println("Error fetching monthly revenues: " + e.getMessage());
        }

        return monthlyRevenues;
    }

}
