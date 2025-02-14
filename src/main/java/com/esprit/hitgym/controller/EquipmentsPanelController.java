package com.esprit.hitgym.controller;

import com.esprit.hitgym.Entity.Equipment;
import com.esprit.hitgym.service.EquipmentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EquipmentsPanelController implements Initializable {
    @FXML
    private HBox freeWeightsContainer;
    @FXML
    private HBox machinesContainer;
    @FXML
    private HBox cardioContainer;

    private EquipmentService equipmentService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        equipmentService = new EquipmentService();
        loadEquipments();
    }

    private void loadEquipments() {
        // Clear existing UI elements (if any)
        freeWeightsContainer.getChildren().clear();
        machinesContainer.getChildren().clear();
        cardioContainer.getChildren().clear();

        // Fetch equipment data from the database
        List<Equipment> equipments = equipmentService.getAllEquipments();

        // Populate the UI with equipment data
        for (Equipment equipment : equipments) {
            // Construct the correct image URL
            String imagePath = equipment.getImageUrl();
            Image image;
            try {
                image = new Image(getClass().getResource(imagePath).toExternalForm());
            } catch (NullPointerException e) {
                System.err.println("Image not found: " + imagePath);
                continue; // Skip this equipment if the image is not found
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            imageView.setPickOnBounds(true);

            Text text = new Text(equipment.getName());

            VBox itemContainer = new VBox(imageView, text);
            itemContainer.setSpacing(10);

            // Add the equipment to the appropriate category container
            switch (equipment.getCategory().toLowerCase()) {
                case "free weights":
                    freeWeightsContainer.getChildren().add(itemContainer);
                    break;
                case "machines":
                    machinesContainer.getChildren().add(itemContainer);
                    break;
                case "cardio":
                    cardioContainer.getChildren().add(itemContainer);
                    break;
            }
        }
    }

    @FXML
    private void openAddEquipmentInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/hitgym/AddEquipment.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Equipment");
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> loadEquipments());
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading AddEquipment.fxml: " + e.getMessage());
        }
    }
}