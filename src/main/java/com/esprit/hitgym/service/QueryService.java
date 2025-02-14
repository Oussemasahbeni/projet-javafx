package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class QueryService {

    public boolean addReply(String reply, int id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    UPDATE queries
                    SET reply = ?, status = true
                    WHERE id = ?
                    """);
            queryStatement.setString(1, reply); // Set reply value
            queryStatement.setInt(2, id);      // Set id value
            int rowsAffected = queryStatement.executeUpdate(); // Execute update and get affected rows

            if (rowsAffected > 0) {
                System.out.println("Reply added successfully for Query ID: " + id); // Success log
                return true;
            } else {
                System.out.println("No query found with ID: " + id + ". Reply not added."); // No record updated log
                return false; // Indicate no update happened
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception in addReply: " + e.getMessage()); // Detailed SQL error log
            e.printStackTrace(); // Print stack trace for debugging
            return false; // Indicate failure
        } finally {
            try {
                if (queryStatement != null) {
                    queryStatement.close(); // Close statement in finally block
                }
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    public boolean addQuery(Queries query) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO queries (id, heading, email, description, created_date, username, status, current_status)
                    VALUES (?,?,?,?,?,?,?,?)"""); // Added status and current_status

            queryStatement.setInt(1, query.getId());
            queryStatement.setString(2, query.getHeading());
            queryStatement.setString(3, query.getEmail());
            queryStatement.setString(4, query.getDescription());
            queryStatement.setDate(5, query.getCurrent_date());
            queryStatement.setString(6, query.getUsername());
            queryStatement.setBoolean(7, query.getStatus()); // Set status
            queryStatement.setBoolean(8, true); // Set current_status to true (active)


            queryStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error in addQuery: " + e); // More descriptive error message
            e.printStackTrace(); // Print stack trace for debugging
            return false; // Indicate failure
        }
    }

    public ResultSet findQuery(String username) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = getConnection().prepareStatement("""
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

    public List<Queries> getAllQueries() {
        List<Queries> queriesList = new ArrayList<>();

        try {
            ResultSet resultSet = findAllQueries(); // Exécuter la requête SQL

            while (resultSet.next()) {
                // Extraire les données du ResultSet
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String heading = resultSet.getString("heading");
                String description = resultSet.getString("description");
                boolean status = resultSet.getBoolean("status");

                // Créer un objet Queries et l'ajouter à la liste
                queriesList.add(new Queries(id, username, email, heading, description, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return queriesList;
    }



    public ResultSet findAllQueries() {

        PreparedStatement queryStatement = null;
        ResultSet expensesRs = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT * FROM queries;
                    """);
            expensesRs = queryStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return expensesRs;

    }
    // *** NEW METHOD: findQueriesByUsername ***
    public ResultSet findQueriesByUsername(String username) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = getConnection().prepareStatement("SELECT * FROM queries WHERE username = ? AND current_status = true");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            // No need for the while loop *here*.  The result set is returned directly.
            return resultSet;  // Return the ResultSet

        } catch (SQLException e) {
            System.out.println("Error in findQueryByUsername: " + e);
            e.printStackTrace(); // Print stack trace for debugging
        }

        return null; // Return null if any error occurs.
    }

    public String getLatestHeading() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String latestHeading = null;

        try {
            // SQL query to get the latest query heading based on the created_date
            String query = "SELECT heading FROM queries ORDER BY created_date DESC LIMIT 1";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            // Check if there is a result and retrieve the latest heading
            if (resultSet.next()) {
                latestHeading = resultSet.getString("heading");
            }

        } catch (SQLException e) {
            System.out.println("Error in getLatestHeading: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return latestHeading;
    }
}