
package com.esprit.hitgym.controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.net.URL;

public class CustomerDashboardPanelController implements Initializable {

    @FXML
    private Text BMIDescription;
    @FXML
    private Text BMIText;
    @FXML
    private Text BMIValue;
    @FXML
    private Button CompletedButton;
    @FXML
    private AnchorPane CompletedPane;
    @FXML
    private Button PendingButton;
    @FXML
    private AnchorPane PendingPane;
    @FXML
    private StackPane QueriesStckPane;

    @FXML
    void CompletedBtn(ActionEvent event) {
        QueriesStckPane.getChildren().get(0).setVisible(true);
        QueriesStckPane.getChildren().get(1).setVisible(false);
        new animatefx.animation.FadeIn(QueriesStckPane).play();
    }

    @FXML
    void PendingBtn(ActionEvent event) {
        QueriesStckPane.getChildren().get(0).setVisible(false);
        QueriesStckPane.getChildren().get(1).setVisible(true);
        new animatefx.animation.FadeIn(QueriesStckPane).play();
    }

    @FXML
    void goToBMIView(ActionEvent event) {
        calculateBMI();
    }

    private void calculateBMI() {
        String weight = CustomerPanelController.Customer.getWeight(); // Get weight from Customer entity
        double weightValue = 0;
        try {
            weightValue = Double.parseDouble(weight);
        } catch (NumberFormatException e) {
            System.err.println("Invalid weight format: " + weight);
            return; // Exit if weight is invalid
        }
        double heightValue = 1.83; // Static height for now (meters)
        String apiKey = "bd11ce789amsh1225e7c6682a897p1283d5jsn87e886108d08"; // Replace with your actual API key
        String apiHost = "body-mass-index-bmi-calculator.p.rapidapi.com";

        try {
            String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
            String encodedApiHost = URLEncoder.encode(apiHost, StandardCharsets.UTF_8);

            String apiUrl = "https://body-mass-index-bmi-calculator.p.rapidapi.com/metric?weight=" + weightValue + "&height=" + heightValue;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-rapidapi-host", apiHost);
            connection.setRequestProperty("x-rapidapi-key", apiKey);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                double bmi = jsonResponse.getDouble("bmi");

                BMIValue.setText(String.format("%.2f", bmi)); // Format BMI to 2 decimal places
                BMIText.setText("Normal"); // Static status for now
                BMIDescription.setText("Your BMI (Body Mass Index) according to your weight, height and age is perfectly normal. Keep it Up."); // Static description

            } else {
                System.err.println("API request failed: " + responseCode);
                // Handle error cases, e.g., display an error message to the user
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO Exception, e.g., display an error message
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueriesStckPane.getChildren().get(0).setVisible(true);
        QueriesStckPane.getChildren().get(1).setVisible(false);
        calculateBMI(); // Calculate BMI on dashboard load
    }
}