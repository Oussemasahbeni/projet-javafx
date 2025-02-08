package com.esprit.hitgym.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class CommonService {

    public static int customersListCount;
    public static int employeesListCount;
    public static int totalList;


    public static ArrayList<String> findAllUsernames() {

        ResultSet allUsernamesRs;
        PreparedStatement queryStatement;
        ArrayList<String> allUsernames = new ArrayList<>();


        try {
            queryStatement = getConnection().prepareStatement("""
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
            queryStatement = getConnection().prepareStatement("""
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


    public static boolean cinExists(String cin) {

        ResultSet cinRs = null;
        PreparedStatement queryStatement = null;
        boolean cinExists = false;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT cin FROM customers
                    WHERE cin = ?;""");

            queryStatement.setString(1, cin);
            cinRs = queryStatement.executeQuery();

            if (cinRs.next()) {
                cinExists = true;
            }

        } catch (SQLException e) {
            System.out.println("Error in retrieving cin: " + e);
        }

        return cinExists;
    }

    public static ArrayList<String> findAllEmails() {

        ResultSet allEmailsRs = null;
        PreparedStatement queryStatement = null;
        ArrayList<String> allEmails = new ArrayList<>();


        try {
            queryStatement = getConnection().prepareStatement("""
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
            queryStatement = getConnection().prepareStatement("""
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


    public static void deleteData(String tableName, int id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    CALL delete_data(?,?);""");

            queryStatement.setString(1, tableName);
            queryStatement.setInt(2, id);

            queryStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("error in deleting: " + e);
        }

    }

    public static int generateId(String choice) {

        ResultSet allIds = null;
        int lastId = 0;
        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
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

}
