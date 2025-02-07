package com.esprit.hitgym.controller.customer;

import com.esprit.hitgym.Entity.Queries;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.controller.queries.QueryMenuButton;
import com.esprit.hitgym.service.QueryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class CustomerQueriesFormPanelController implements Initializable {

    private final static int rowsPerPage = 10;

    private final QueryService queryService;

    public CustomerQueriesFormPanelController() {
        this.queryService = new QueryService();
    }

    @FXML
    private TableView<Queries> employeesView; // Renamed to employeesView but is actually for queries

    @FXML
    private TableColumn<Queries, Integer> Id;

    @FXML
    private TableColumn<Queries, String> Status;

    @FXML
    private TableColumn<Queries, String> Heading;

    @FXML
    private TableColumn<Queries, String> Description;

    @FXML
    private TableColumn<Queries, String> action; // Should be QueryMenuButton but FXML might not handle it directly

    @FXML
    private Button addbutton;

    @FXML
    private TextField keyword;

    @FXML
    private Pagination pagination;

    @FXML
    private Button refreshButton;

    @FXML
    private Button sortButton;

    @FXML
    private Button sortButton1;

    public static ObservableList<Queries> queriesList = FXCollections.observableArrayList();
    ResultSet resultSet = null;

    @FXML
    void CreateQuery(ActionEvent event) throws IOException {
        new GeneralFunctions().switchSceneModality("CreateQuery.fxml");
    }

    @FXML
    void refreshbtn(ActionEvent event) {
        keyword.setText("");
        loadData();
    }

    @FXML
    void sortbtn1(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pagination.setPageFactory(this::createPage);
        loadData();

        /*------Searching With Keryword Logic----------*/
        FilteredList<Queries> filteredList = new FilteredList<>(queriesList, b -> true);
        keyword.textProperty().addListener((observable, oldvalue, newvalue) ->
        {
            filteredList.setPredicate(Queries -> {
                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null) {
                    return true;
                }
                String searchkeyword = newvalue.toLowerCase();

                if (Queries.getHeading().toLowerCase().indexOf(searchkeyword) > -1) {
                    return true;
                } else if (Queries.getDescription().toLowerCase().indexOf(searchkeyword) > -1) {
                    return true;
                } else if (String.valueOf(Queries.getId()).toLowerCase().indexOf(searchkeyword) > -1) {
                    return true;
                }
                return false;
            });
            SortedList<Queries> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(employeesView.comparatorProperty());
            employeesView.setItems(sortedList);
        });
    }

    private Node createPage(int pageIndex) {
        if (queriesList.size() > 0 && queriesList.size() <= 10) {
            pagination.setPageCount(1);
        } else if (queriesList.size() > 10 && queriesList.size() <= 20) {
            pagination.setPageCount(2);
        } else if (queriesList.size() > 20 && queriesList.size() <= 30) {
            pagination.setPageCount(3);
        } else if (queriesList.size() > 30 && queriesList.size() <= 40) {
            pagination.setPageCount(4);
        } else if (queriesList.size() > 40 && queriesList.size() <= 50) {
            pagination.setPageCount(5);
        } else if (queriesList.size() > 50 && queriesList.size() <= 60) {
            pagination.setPageCount(6);
        } else if (queriesList.size() > 60 && queriesList.size() <= 70) {
            pagination.setPageCount(7);
        }
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, queriesList.size());
        try {
            employeesView.setItems(FXCollections.observableList(queriesList.subList(fromIndex, toIndex)));
        } catch (Exception e) {
            System.out.println("Not Enough Entries");
        }
        return employeesView;
    }

    private void loadData() {
        String username = CustomerPanelController.Customer.getUserName(); // Get logged-in username
        System.out.println("Fetching queries for username: " + username); // Log username

        showRecords(username); // Pass username to showRecords

        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Heading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        action.setCellValueFactory(new PropertyValueFactory<>("actionBtn"));
        Status.setCellValueFactory(new PropertyValueFactory<>("StatusString"));
    }

    private void showRecords(String username) { // Accept username as parameter
        queriesList.clear();
        try {
            resultSet = queryService.findQuery(username); // Fetch queries by username

            int queryCount = 0; // Counter for logging

            while (resultSet.next()) {
                queryCount++;
                boolean status = resultSet.getBoolean("status");
                int id = resultSet.getInt("id");
                String fetchedUsername = resultSet.getString("username");
                String email = resultSet.getString("email");
                String heading = resultSet.getString("heading");
                String description = resultSet.getString("description");

                QueryMenuButton actionButton = new QueryMenuButton(
                        "Action",
                        id,
                        fetchedUsername,
                        email,
                        heading,
                        description
                );

                Queries queryObj = new Queries(status, id, fetchedUsername, email, heading, description, actionButton);
                queriesList.add(queryObj);
            }

            System.out.println("Number of queries fetched: " + queryCount); // Log query count
            employeesView.setItems(queriesList);

        } catch (NullPointerException e) {
            System.out.print(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}