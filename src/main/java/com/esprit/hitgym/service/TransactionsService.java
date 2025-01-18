package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Revenue;
import com.esprit.hitgym.Entity.Transaction;
import com.esprit.hitgym.helpers.CustomDate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class TransactionsService {

    private final RevenueService revenueService;

    public TransactionsService() {
        this.revenueService = new RevenueService();
    }

    public boolean saveToDb(Transaction transaction) {
        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO transactions (id, created_date, amount, transaction_number, bank_name, account_owner_name, fk_customer_id, status)
                    VALUES (?,?,?,?,?,?,?,?);""");

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

    public boolean updateTransactionStatus(int transactionId) {

        PreparedStatement queryStatement = null;
        PreparedStatement queryStatement2 = null;
        int fkCustomerId = 0;
        int transactionAmount = 0;

        try {
            queryStatement = getConnection().prepareStatement("""
                    UPDATE transactions
                    SET status = true
                    WHERE id = ?;""");
            queryStatement.setInt(1, transactionId);

            queryStatement.executeUpdate();

            try {
                PreparedStatement queryStatement3 = getConnection().prepareStatement("SELECT fk_customer_id FROM transactions WHERE id = ?");
                queryStatement3.setInt(1, transactionId);
                ResultSet resultSet = queryStatement3.executeQuery();

                while (resultSet.next()) {
                    fkCustomerId = resultSet.getInt("fk_customer_id");
                }

            } catch (SQLException e) {
                System.out.println("Error: " + e);
            }

            queryStatement2 = getConnection().prepareStatement("""
                    UPDATE customers
                    SET is_active = true
                    WHERE id = ?;""");

            queryStatement2.setInt(1, fkCustomerId);
            queryStatement2.executeUpdate();

            queryStatement = getConnection().prepareStatement("""
                    SELECT amount FROM transactions
                    WHERE fk_customer_id = ?;
                    """);
            queryStatement.setInt(1, fkCustomerId);

            ResultSet transactionAmountRs = queryStatement.executeQuery();

            while (transactionAmountRs.next()) {
                transactionAmount = transactionAmountRs.getInt(1);
            }

            Revenue revenue = new Revenue(CommonService.generateId("revenues"), CustomDate.getCurrentMonth(), CustomDate.getCurrentYear(), transactionAmount);
            this.revenueService.saveUpdateToDb(revenue);

            return true;

        } catch (SQLException e) {
            System.out.println("Error! Could not run query: " + e);
            return false;
        }
    }

    public ResultSet getAllTransactions() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT * FROM transactions;
                    """);
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;

    }

}
