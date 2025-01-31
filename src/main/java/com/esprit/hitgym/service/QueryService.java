package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class QueryService {

    public boolean addReply(String reply, int id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
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

    public boolean addQuery(Queries query) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO queries (id, heading, email, description, created_date, username)
                    VALUES (?,?,?,?,?,?)""");
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
}
