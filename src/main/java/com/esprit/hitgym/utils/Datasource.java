package com.esprit.hitgym.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {

    private static Connection connection;

    private static final String dbUrl = "jdbc:mysql://localhost:3306/gym_db";
    private static final String dbUsername = "root";
    private static final String dbPassword = "password";


    private Datasource() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                System.out.println("connexion établie");

            } catch (SQLException ex) {
                System.out.println("bd non trouvée ou problème d'identification " + ex.getMessage());
            }
        }
        return connection;
    }
}
