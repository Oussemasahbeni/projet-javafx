package com.esprit.hitgym.controller;

import com.esprit.hitgym.Entity.BMI; import com.esprit.hitgym.controller.customer.CustomerPanelController; import com.esprit.hitgym.service.BmiService; import com.esprit.hitgym.service.CommonService; import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections; import javafx.collections.ObservableList; import javafx.event.ActionEvent; import javafx.fxml.FXML; import javafx.fxml.Initializable; import javafx.scene.Node; import javafx.scene.control.*; import javafx.scene.control.cell.PropertyValueFactory; import org.json.JSONObject;

import java.io.BufferedReader; import java.io.IOException; import java.io.InputStreamReader;
import java.net.HttpURLConnection; import java.net.URL; import java.net.URLEncoder;
import java.nio.charset.StandardCharsets; import java.sql.Date;
import java.sql.ResultSet; import java.sql.SQLException;
import java.time.LocalDate; import java.util.ResourceBundle;

public class BMIViewPanelController implements Initializable {

    private static final int rowsPerPage = 10;

    @FXML
    private TableColumn<BMI, Double> BMI; // fx:id remains "BMI" in your FXML

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
    private final ObservableList<BMI> bmiList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the pagination page factory and load data
        pagination.setPageFactory(this::createPage);
        loadData();

        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        // Use lambda to avoid reflection issues when accessing getBmiValue()
        BMI.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBmiValue()));
        RecordedDate.setCellValueFactory(new PropertyValueFactory<>("recordedDate"));
        RecordedMonth.setCellValueFactory(new PropertyValueFactory<>("recordedMonth"));

        // Hide input fields initially
        weightField.setVisible(false);
        heightField.setVisible(false);
        calculateAndSaveButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, bmiList.size());
        ObservableList<BMI> pageItems = FXCollections.observableArrayList(bmiList.subList(fromIndex, toIndex));
        BMIView.setItems(pageItems);
        return BMIView;
    }

    private void loadData() {
        bmiList.clear();
        int customerId = CustomerPanelController.Customer.getCustomerId();
        try {
            ResultSet resultSet = bmiService.findBmiByCustomerId(customerId);
            while (resultSet.next()) {
                // Constructor order: (weight, recordedDate, id, height, bmiValue)
                BMI bmi = new BMI(
                        resultSet.getDouble("weight"),
                        resultSet.getDate("recorded_date"),
                        resultSet.getInt("id"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("bmi_value")
                );
                bmiList.add(bmi);
            }
            int pageCount = (int) Math.ceil((double) bmiList.size() / rowsPerPage);
            pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
            pagination.setCurrentPageIndex(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTableData() {
        Platform.runLater(() -> {
            loadData();
            // Refresh the current page by re-setting the page factory and current page index
            int currentPage = pagination.getCurrentPageIndex();
            pagination.setPageFactory(this::createPage);
            pagination.setCurrentPageIndex(currentPage);
            BMIView.refresh();
        });
    }

    @FXML
    void refreshbtn(ActionEvent event) {
        refreshTableData();
    }

    @FXML
    void calculateAndSaveBMI(ActionEvent event) {
        String weightText = weightField.getText();
        String heightText = heightField.getText();

        try {
            double weightValue = Double.parseDouble(weightText);
            double heightValue = Double.parseDouble(heightText);

            if (weightValue <= 0 || heightValue <= 0) {
                errorLabel.setText("Weight and height must be positive values.");
                errorLabel.setVisible(true);
                return;
            }

            String apiKey = "bd11ce789amsh1225e7c6682a897p1283d5jsn87e886108d08"; // Replace with your API key
            String apiHost = "body-mass-index-bmi-calculator.p.rapidapi.com";

            String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
            String encodedApiHost = URLEncoder.encode(apiHost, StandardCharsets.UTF_8);

            String apiUrl = "https://body-mass-index-bmi-calculator.p.rapidapi.com/metric?weight="
                    + weightValue + "&height=" + heightValue;
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

                // Create BMI object (constructor order: weight, recordedDate, id, height, bmiValue)
                BMI bmi = new BMI(
                        weightValue,
                        Date.valueOf(LocalDate.now()),
                        CommonService.generateId("bmi"),
                        heightValue,
                        bmiValue
                );
                int customerId = CustomerPanelController.Customer.getCustomerId();
                bmiService.add(bmi, customerId);

                // Unconditionally refresh the table view
                refreshTableData();
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
        // Implement sorting logic if needed.
    }

}