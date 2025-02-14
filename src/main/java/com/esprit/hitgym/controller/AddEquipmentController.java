package com.esprit.hitgym.controller;

import com.esprit.hitgym.Entity.Equipment;
import com.esprit.hitgym.service.EquipmentService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AddEquipmentController {
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> categoryField;
    @FXML
    private ImageView imageView;

    private File imageFile;
    private String imageUrl;
    private EquipmentService equipmentService = new EquipmentService();

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Equipment Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                String targetDirectory = "src/main/resources/com/esprit/hitgym/bordericons/";
                File targetDir = new File(targetDirectory);
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                String fileName = selectedFile.getName();
                File targetFile = new File(targetDirectory + fileName);
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                imageUrl = "/com/esprit/hitgym/bordericons/" + fileName;
                imageView.setImage(new Image(targetFile.toURI().toString()));
            } catch (IOException e) {
                System.err.println("Error copying image file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void saveEquipment() {
        String name = nameField.getText();
        String category = categoryField.getValue();

        if (name.isEmpty() || category == null || imageUrl == null) {
            System.out.println("Please fill all fields and upload an image.");
            return;
        }

        Equipment equipment = new Equipment(name, imageUrl, category);
        equipmentService.addEquipment(equipment);

        // Close the window after saving
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}