package com.esprit.hitgym.controller.queries;

import com.esprit.hitgym.Entity.Queries;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.helpers.CustomDate;
import com.esprit.hitgym.service.CommonService;
import com.esprit.hitgym.service.QueryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert; // Import Alert

import java.io.IOException;

public class CreateQueryController {

    @FXML
    private TextArea DescriptionBox;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField HeadingField;

    @FXML
    private TextField UsernameField;

    @FXML
    private Button exitButton;

    private final QueryService queryService = new QueryService(); // Initialize QueryService

    @FXML
    void CreateQueryBtn(ActionEvent event) {
        String heading = HeadingField.getText();
        String email = EmailField.getText();
        String username = UsernameField.getText();
        String descriptionText = DescriptionBox.getText();

        if (heading.isEmpty() || email.isEmpty() || username.isEmpty() || descriptionText.isEmpty()) {
            showAlert("Error", "Please fill in all fields."); // Show error alert
            return; // Stop further processing
        }

        try {
            Queries newQuery = new Queries(
                    CommonService.generateId("queries"), // Generate ID
                    username,
                    email,
                    heading,
                    descriptionText,
                    false // Default status is pending (false)
            );
            queryService.addQuery(newQuery); // Save to database
            showAlert("Success", "Query created successfully!"); // Show success alert
            clearFields(); // Clear the form
        } catch (Exception e) {
            showAlert("Error", "Failed to create query. Please try again."); // Show error alert
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        HeadingField.setText("");
        EmailField.setText("");
        UsernameField.setText("");
        DescriptionBox.setText("");
    }


    @FXML
    void exit(ActionEvent event) {
        new GeneralFunctions().close(exitButton);
    }
}