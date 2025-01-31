package com.esprit.hitgym.controller.member;

import com.esprit.hitgym.Entity.Customer;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.service.CustomerService;
import com.esprit.hitgym.view.CustomMenuButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MembersPanelController implements Initializable {

    // Making the field public so, it can be accessible without getter setters
    public static int deletingId = -1;

    private final static int DataSize = 100;
    private final static int rowsPerPage = 10;
    @FXML
    private Pagination pagination;

    Stage membercardstage;

    Customer customer = null;

    private final CustomerService customerService;

    public MembersPanelController() {
        this.customerService = new CustomerService();
    }

    private String FullName;
    @FXML
    private TableColumn<Customer, Integer> Id;
    @FXML
    private TableColumn<Customer, MenuButton> action;

    @FXML
    private TableColumn<Customer, String> email;

    @FXML
    public TableView<Customer> membersView;

    @FXML
    private TableColumn<Customer, String> FirstName;
    @FXML
    private TableColumn<Customer, String> LastName;

    @FXML
    private TableColumn<Customer, String> cin;

    @FXML
    private TableColumn<Customer, String> phone;

    @FXML
    private TableColumn<Customer, Integer> plan;

    @FXML
    private TextField keyword;

    public static ObservableList<Customer> memberslist = FXCollections.observableArrayList();
    ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*Button*/

        pagination.setPageFactory(this::createPage);

        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*------Searching With Keryword Logic----------*/
        FilteredList<Customer> filteredList = new FilteredList<>(memberslist, b -> true);

        keyword.textProperty().addListener((observable, oldvalue, newvalue) ->
        {

            filteredList.setPredicate(customer -> {
                if (newvalue.isBlank()) {
                    return true;
                }
                String searchkeyword = newvalue.toLowerCase();

                if (customer.getFullname().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else if (customer.getFirstName().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else if (customer.getLastName().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else if (customer.getPhoneNumber().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else if (customer.getCinNumber().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else if (String.valueOf(customer.getId()).contains(searchkeyword)) {
                    return true;
                } else if ((String.valueOf(customer.getMonthlyPlan()).toLowerCase().contains(searchkeyword))) {
                    return true;
                } else if (customer.getEmail().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else {
                    return false;
                }

            });


            SortedList<Customer> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(membersView.comparatorProperty());
            membersView.setItems(sortedList);
        });
        /*----Search with Keyword Logic Ends HERE---------*/

    }

    @FXML
    void sortbtn(ActionEvent event) {

        memberslist.sort(Comparator.comparing(Customer::tolowerfirstname, Comparator.naturalOrder()));
        membersView.setItems(memberslist);

    }

    private Node createPage(int pageIndex) {
        if (!memberslist.isEmpty() && memberslist.size() <= 10) {
            pagination.setPageCount(1);
        } else if (memberslist.size() > 10 && memberslist.size() <= 20) {
            pagination.setPageCount(2);
        } else if (memberslist.size() > 20 && memberslist.size() <= 30) {
            pagination.setPageCount(3);
        } else if (memberslist.size() > 30 && memberslist.size() <= 40) {
            pagination.setPageCount(4);
        } else if (memberslist.size() > 40 && memberslist.size() <= 50) {
            pagination.setPageCount(5);
        } else if (memberslist.size() > 50 && memberslist.size() <= 60) {
            pagination.setPageCount(6);
        } else if (memberslist.size() > 60 && memberslist.size() <= 70) {
            pagination.setPageCount(7);
        }
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, memberslist.size());
        try {
            membersView.setItems(FXCollections.observableList(memberslist.subList(fromIndex, toIndex)));
        } catch (Exception e) {
            System.out.println("Not Enough Entries");
        }
        return membersView;
    }

    public void view() throws IOException {
        new GeneralFunctions().switchSceneModality("membersDetailCard.fxml");
    }

    public void loadData() throws SQLException {
        showrecords();
        Id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        cin.setCellValueFactory(new PropertyValueFactory<>("cinNumber"));
        plan.setCellValueFactory(new PropertyValueFactory<>("monthlyPlan"));
        action.setCellValueFactory(new PropertyValueFactory<>("actionBtn"));
    }

    void showrecords() throws SQLException {
        memberslist.clear();
        try {
            resultSet = customerService.findAllCustomers();


            while (resultSet.next()) {
                memberslist.add(new Customer(true, resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("email"), resultSet.getString("phone_number"), resultSet.getString("cin"), Integer.parseInt(resultSet.getString("monthly_plan")), new CustomMenuButton(resultSet.getInt("id"), "Action", resultSet.getString("first_name") + " " + resultSet.getString("last_name"), resultSet.getString("weight"), "XYZ", resultSet.getString("email"), resultSet.getString("username"), resultSet.getString("monthly_plan"))));

                membersView.setItems(memberslist);
            }
        } catch (NullPointerException e) {
            System.out.println("Connection to Database Cannot Be Established");
        }
    }

    @FXML
    void sortbtn1(ActionEvent event) {
        memberslist.sort(Comparator.comparing(Customer::getId, Comparator.naturalOrder()));
        membersView.setItems(memberslist);

    }

    @FXML
    void refreshbtn(ActionEvent event) throws SQLException {

        keyword.setText("");
        showrecords();

    }

}
