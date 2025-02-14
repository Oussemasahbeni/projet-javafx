package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.Equipment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class EquipmentService {

    /**
     * Adds static equipment data to the database.
     */
    public void addEquipment(Equipment equipment) {
        String query = "INSERT INTO equipments (name, image_url, category) VALUES (?, ?, ?);";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, equipment.getName());
            statement.setString(2, equipment.getImageUrl());
            statement.setString(3, equipment.getCategory());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding equipment: " + e.getMessage());
        }
    }

    /**
     * Fetches all equipment from the database.
     */
    public List<Equipment> getAllEquipments() {
        List<Equipment> equipments = new ArrayList<>();
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT name, image_url, category FROM equipments;
                    """);
            resultSet = queryStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String imageUrl = resultSet.getString("image_url");
                String category = resultSet.getString("category");
                equipments.add(new Equipment(name, imageUrl, category));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching equipment data: " + e.getMessage());
        } finally {
            // Close resources (resultSet, queryStatement, connection) if needed
        }

        return equipments;
    }
}
