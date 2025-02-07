package com.esprit.hitgym.controller.queries;

import com.esprit.hitgym.Entity.Queries;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.service.QueryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class QueriesPanelController implements Initializable {
    private final static int rowsPerPage = 10;

    private final QueryService queryService;

    public QueriesPanelController() {
        this.queryService = new QueryService();
    }

    @FXML
    private TextField keyword;

    @FXML
    private TableView<Queries> queriesView;
    @FXML
    private TableColumn<Queries, Integer> Id;

    @FXML
    private TableColumn<Queries, String> action;

    @FXML
    private TableColumn<Queries, String> description;

    @FXML
    private TableColumn<Queries, String> email;
    @FXML
    private TableColumn<Queries, String> Status;
    @FXML
    private TableColumn<Queries, String> heading;
    @FXML
    private TableColumn<Queries, String> username;
    @FXML
    private Pagination pagination;
    public static ObservableList<Queries> queriesList = FXCollections.observableArrayList();
    ResultSet resultSet = null;

    @FXML
    void refreshBtn() {
        loadData();
    }

    @FXML
    void sortBtn() {
        queriesList.sort(Comparator.comparing(Queries::getLowerUserName, Comparator.naturalOrder()));
        queriesView.setItems(queriesList);
    }

    @FXML
    void sortbtn1() {

        queriesList.sort(Comparator.comparing(Queries::getId, Comparator.naturalOrder()));
        queriesView.setItems(queriesList);
    }

    public static void view() throws IOException {
        new GeneralFunctions().switchSceneModality("ViewQuery.fxml");
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

                if (Queries.getUsername().toLowerCase().indexOf(searchkeyword) > -1) {
                    return true;
                } else if (Queries.getEmail().toLowerCase().indexOf(searchkeyword) > -1) {
                    return true;
                } else if (String.valueOf(Queries.getId()).toLowerCase().indexOf(searchkeyword) > -1) {
                    return true;
                }
                return false;
            });
            SortedList<Queries> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(queriesView.comparatorProperty());
            queriesView.setItems(sortedList);
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
            queriesView.setItems(FXCollections.observableList(queriesList.subList(fromIndex, toIndex)));
        } catch (Exception e) {
            System.out.println("Not Enough Entries");
        }
        return queriesView;
    }

    private void loadData() {
        showRecords();
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        heading.setCellValueFactory(new PropertyValueFactory<>("heading"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        action.setCellValueFactory(new PropertyValueFactory<>("actionBtn"));
        Status.setCellValueFactory(new PropertyValueFactory<>("StatusString"));
    }

    private void showRecords() {
        queriesList.clear();
        try {
            resultSet = queryService.findAllQueries();
            System.out.println("ResultSet from findAllQueries: " + resultSet); // ADDED: Check ResultSet

            if (resultSet != null) { // ADDED: Check if ResultSet is null
                while (resultSet.next()) {
                    boolean status = resultSet.getBoolean("status");
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String heading = resultSet.getString("heading");
                    String description = resultSet.getString("description");

                    QueryMenuButton actionButton = new QueryMenuButton(
                            "Action",
                            id,
                            username,
                            email,
                            heading,
                            description
                    );

                    Queries queryObj = new Queries(status, id, username, email, heading, description, actionButton);
                    queriesList.add(queryObj);
                    System.out.println("Added query to list: " + queryObj); // ADDED: Check added query
                }
            } else {
                System.out.println("ResultSet is null, no data loaded."); // ADDED: Log if ResultSet is null
            }


            queriesView.setItems(queriesList);
            System.out.println("QueriesList size: " + queriesList.size()); // ADDED: Check list size
        } catch (NullPointerException e) {
            System.out.print("NullPointerException in showRecords: " + e);
            e.printStackTrace(); // Print the stack trace for debugging
        } catch (SQLException e) {
            System.out.println("SQLException in showRecords: " + e);
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
}