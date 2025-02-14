package com.esprit.hitgym.controller.dashboard;

import com.esprit.hitgym.Entity.Expense;
import com.esprit.hitgym.Entity.Revenue;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.service.ExpensesService;
import com.esprit.hitgym.service.RevenueService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ExpensesTableController implements Initializable {

    private final static int rowsPerPage = 10;
    @FXML
    private TableColumn<Expense, String> Description;

    @FXML
    private TableColumn<Expense, Integer> Amount;

    @FXML
    private TableView<Expense> ExpenseView;

    @FXML
    private TableColumn<Expense, java.sql.Date> ExpenseDate;

    @FXML
    private TableColumn<Expense, Integer> Id;

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
    public static ObservableList<Expense> ExpenseList = FXCollections.observableArrayList();
    ResultSet resultSet = null;

    private final ExpensesService expensesService;

    public ExpensesTableController() {
        this.expensesService = new ExpensesService();
    }

    @FXML
    void addExpenseButton() throws IOException {

        new GeneralFunctions().switchSceneModalityCallback("AddExpense.fxml", this::refreshData);
    }

    @FXML
    void refreshbtn(ActionEvent event) {
        refreshData();
    }

    @FXML
    void sortbtn(ActionEvent event) {

        ExpenseList.sort(Comparator.comparing(Expense::getCreatedDate, Comparator.naturalOrder()));
        ExpenseView.setItems(ExpenseList);
    }

    @FXML
    void sortbtn1(ActionEvent event) {
        ExpenseList.sort(Comparator.comparing(Expense::getId, Comparator.naturalOrder()));
        ExpenseView.setItems(ExpenseList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pagination.setPageFactory(this::createPage);

        loadData();

        /*------Searching With Keryword Logic----------*/
        FilteredList<Expense> filteredList = new FilteredList<>(ExpenseList, b -> true);

        keyword.textProperty().addListener((observable, oldvalue, newvalue) ->
        {

            filteredList.setPredicate(expense -> {
                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null) {
                    return true;
                }
                String searchkeyword = newvalue.toLowerCase();

                if (String.valueOf(expense.getAmount()).contains(searchkeyword)) {
                    return true;
                } else if (String.valueOf(expense.getId()).contains(searchkeyword)) {
                    return true;
                } else {
                    return false;
                }

            });
            SortedList<Expense> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(ExpenseView.comparatorProperty());
            ExpenseView.setItems(sortedList);
        });

    }

    private Node createPage(Integer pageIndex) {
        if (ExpenseList.size() > 0 && ExpenseList.size() <= 10) {
            pagination.setPageCount(1);
        } else if (ExpenseList.size() > 10 && ExpenseList.size() <= 20) {
            pagination.setPageCount(2);
        } else if (ExpenseList.size() > 20 && ExpenseList.size() <= 30) {
            pagination.setPageCount(3);
        } else if (ExpenseList.size() > 30 && ExpenseList.size() <= 40) {
            pagination.setPageCount(4);
        } else if (ExpenseList.size() > 40 && ExpenseList.size() <= 50) {
            pagination.setPageCount(5);
        } else if (ExpenseList.size() > 50 && ExpenseList.size() <= 60) {
            pagination.setPageCount(6);
        } else if (ExpenseList.size() > 60 && ExpenseList.size() <= 70) {
            pagination.setPageCount(7);
        }
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, ExpenseList.size());
        try {
            ExpenseView.setItems(FXCollections.observableList(ExpenseList.subList(fromIndex, toIndex)));
        } catch (Exception e) {
            System.out.println("Not Enough Entries");
        }
        return ExpenseView;
    }

    private void loadData() {
        showrecords();
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        ExpenseDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
    }

    private void showrecords() {
        ExpenseList.clear();
        try {
            resultSet = expensesService.getAllExpenses();
            while (resultSet.next()) {

                // Add the data to ExpenseList
                ExpenseList.add(
                        new Expense(
                                resultSet.getInt("id"),
                                resultSet.getString("description"),
                                resultSet.getInt("amount"),
                                resultSet.getDate("selected_date")));
            }
            System.out.println(ExpenseList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshData(){
        Platform.runLater(() -> {
            keyword.setText("");
            loadData();
            int currentPage = pagination.getCurrentPageIndex();
            pagination.setPageFactory(this::createPage);
            pagination.setCurrentPageIndex(currentPage);
            ExpenseView.refresh();
        });
    }
}

