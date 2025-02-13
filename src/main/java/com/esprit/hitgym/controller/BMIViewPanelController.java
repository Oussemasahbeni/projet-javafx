package com.esprit.hitgym.controller;

import com.esprit.hitgym.Entity.BMI;
import com.esprit.hitgym.controller.customer.CustomerPanelController;
import com.esprit.hitgym.service.BmiService;
import com.esprit.hitgym.service.CommonService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BMIViewPanelController implements Initializable {

    private final static int rowsPerPage = 10;


    @FXML
    private TableColumn<BMI, Double> BMI;

    @FXML
    private TableView<BMI> BMIView;

    @FXML
    private TableColumn<BMI, Integer> Id;

    @FXML
    private TableColumn<BMI, Date> RecordedDate;

    @FXML
    private TableColumn<BMI, String> RecordedMonth;

    @FXML
    private TableColumn<BMI, Double> Weight;

    @FXML
    private Button addbutton;

    @FXML
    private Button calculateAndSaveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField keyword;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;


    @FXML
    private Pagination pagination;

    @FXML
    private Button refreshButton;

    @FXML
    private Button sortButton;

    @FXML
    private Button sortButton1;

    @FXML
    private Label errorLabel;

    private final BmiService bmiService = new BmiService();
    private ObservableList<BMI> bmiList = FXCollections.observableArrayList();


    public void refreshTableData() {
        Platform.runLater(this::loadData); // Ensure loadData is run on UI thread
    }


    @FXML
    void refreshbtn(ActionEvent event) {
        refreshTableData();
    }

    @FXML
    void calculateAndSaveBMI(ActionEvent event) {
        String weightText = weightField.getText();
        String heightText = heightField.getText();

        double weightValue = 0;
        double heightValue = 0;

        try {
            weightValue = Double.parseDouble(weightText);
            heightValue = Double.parseDouble(heightText);

            if (weightValue <= 0 || heightValue <= 0) {
                errorLabel.setText("Weight and height must be positive values.");
                errorLabel.setVisible(true);
                return;
            }


            String apiKey = "bd11ce789amsh1225e7c6682a897p1283d5jsn87e886108d08"; // Replace with your actual API key
            String apiHost = "body-mass-index-bmi-calculator.p.rapidapi.com";


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
                double bmiValue = jsonResponse.getDouble("bmi");

                BMI bmi = new BMI(
                        weightValue,
                        Date.valueOf(LocalDate.now()),
                        CommonService.generateId("bmi"),
                        bmiValue,
                        heightValue
                );
                int customerId = CustomerPanelController.Customer.getCustomerId();
                bmiService.add(bmi, customerId);
                refreshTableData(); // Refresh the table in BMIViewPanelController - Now calls Platform.runLater internally
                clearInputFields();


            } else {
                errorLabel.setText("API request failed. Please try again.");
                errorLabel.setVisible(true);
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid weight or height format. Please enter numbers.");
            errorLabel.setVisible(true);
        } catch (IOException e) {
            errorLabel.setText("Error communicating with BMI API.");
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        weightField.clear();
        heightField.clear();
        errorLabel.setVisible(false);
    }

    @FXML
    void cancel(ActionEvent event) {
        clearInputFields();
    }


    @FXML
    void addBMI(ActionEvent event) {
        if (addbutton.getText().equals("Add")) {
            addbutton.setText("Cancel");
            weightField.setVisible(true);
            heightField.setVisible(true);
            calculateAndSaveButton.setVisible(true);
            cancelButton.setVisible(true);
        } else if (addbutton.getText().equals("Cancel")) {
            addbutton.setText("Add");
            weightField.setVisible(false);
            heightField.setVisible(false);
            calculateAndSaveButton.setVisible(false);
            cancelButton.setVisible(false);
            clearInputFields();
        }
    }


    @FXML
    void sortbtn1(ActionEvent event) {
        // Sorting logic if needed
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pagination.setPageFactory(this::createPage);
        loadData();
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        BMI.setCellValueFactory(new PropertyValueFactory<>("bmiValue")); // Use bmiValue here
        RecordedDate.setCellValueFactory(new PropertyValueFactory<>("recordedDate"));
        RecordedMonth.setCellValueFactory(new PropertyValueFactory<>("recordedMonth"));

        weightField.setVisible(false);
        heightField.setVisible(false);
        calculateAndSaveButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, bmiList.size());
        try {
            BMIView.setItems(FXCollections.observableList(bmiList.subList(fromIndex, toIndex)));
        } catch (Exception e) {
            System.out.println("Not Enough Entries");
        }
        return BMIView;
    }

    private void loadData() {
        bmiList.clear();
        int customerId = CustomerPanelController.Customer.getCustomerId();
        try {
            ResultSet resultSet = bmiService.findBmiByCustomerId(customerId);
            while (resultSet.next()) {
                BMI bmi = new BMI(
                        resultSet.getDouble("weight"),
                        resultSet.getDate("recorded_date"),
                        resultSet.getInt("id"),
                        resultSet.getDouble("bmi_value"),
                        resultSet.getDouble("height")
                );
                bmiList.add(bmi);
            }
            pagination.setPageCount((int) Math.ceil((double)bmiList.size() / rowsPerPage));
            if (bmiList.size() > 0) {
                pagination.setCurrentPageIndex(0);
            } else {
                BMIView.setItems(FXCollections.observableArrayList()); // Clear table if no data
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }}