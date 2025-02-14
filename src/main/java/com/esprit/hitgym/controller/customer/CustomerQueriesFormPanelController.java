package com.esprit.hitgym.controller.customer;

import com.esprit.hitgym.Entity.Queries;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.controller.queries.QueryMenuButton;
import com.esprit.hitgym.service.EmployeeService;
import com.esprit.hitgym.service.QueryService;
import com.esprit.hitgym.utils.EmailSender;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerQueriesFormPanelController implements Initializable {

    private final static int rowsPerPage = 10;

    private final QueryService queryService;
    private final EmployeeService employeeService;
    private final EmailSender emailSender;

    public CustomerQueriesFormPanelController() {
        this.queryService = new QueryService();
        this.emailSender = new EmailSender();
        this.employeeService = new EmployeeService();
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
    private TableColumn<Queries, QueryMenuButton> action; // Changed type to QueryMenuButton

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
        new GeneralFunctions().switchSceneModalityCallback("CreateQuery.fxml", () -> {
            keyword.setText("");
            loadData();
            try {
                sendEmailToAdmins();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
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
        Platform.runLater(() -> {
            showRecords(username); // Pass username to showRecords

            Id.setCellValueFactory(new PropertyValueFactory<>("id"));
            Description.setCellValueFactory(new PropertyValueFactory<>("description"));
            Heading.setCellValueFactory(new PropertyValueFactory<>("heading"));
            action.setCellValueFactory(new PropertyValueFactory<>("actionBtn"));
            Status.setCellValueFactory(new PropertyValueFactory<>("StatusString"));
        });



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
                String reply = resultSet.getString("reply"); // Fetch reply

                QueryMenuButton actionButton = new QueryMenuButton(
                        "Action",
                        id,
                        fetchedUsername,
                        email,
                        heading,
                        description
                );

                // **ADD THIS BLOCK - SET ViewActionHandler for Customer View**
                actionButton.setViewActionHandler(button -> showReplyDialog(button, reply)); // Pass reply here


                Queries queryObj = new Queries(status, id, fetchedUsername, email, heading, description, actionButton);
                queryObj.setReply(reply); // Set reply in Queries object
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

    // New method to show the reply in a dialog (Same as in QueriesPanelController)
    private void showReplyDialog(QueryMenuButton button, String replyText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Query Reply");
        alert.setHeaderText("Reply from Staff");

        if (replyText != null && !replyText.isEmpty()) {
            alert.setContentText(replyText);
        } else {
            alert.setContentText("No reply yet for this query.");
        }

        alert.showAndWait();
    }

    private void sendEmailToAdmins() throws MessagingException {
        // Fetch all employee emails
        List<String> employeeEmails = this.employeeService.getAllEmployeeEmails();

        // Subject and email content
        String subject = "New Query Submitted by a Customer";

        String emailContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html lang=\"en\" dir=\"ltr\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=yes\">" +
                "<meta name=\"x-apple-disable-message-reformatting\">" +
                "<meta name=\"format-detection\" content=\"telephone=no, date=no, address=no, email=no, url=no\">" +
                "</head>" +
                "<body style=\"background-color:#f6f9fc;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Helvetica Neue',Ubuntu,sans-serif;margin:0;padding:0\">" +
                "<div style=\"table-layout:fixed;width:100%\">" +
                "<div style=\"margin:0 auto;max-width:600px\">" +
                "<table align=\"center\" width=\"100%\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"max-width:600px;background-color:#ffffff;margin:0 auto;padding:20px 0 48px;margin-bottom:64px\">" +
                "<tbody>" +
                "<tr>" +
                "<td align=\"center\" style=\"padding:0 48px\">" +
                "<h2 style=\"font-size:24px;line-height:32px;color:#333333;margin:16px 0;text-align:center\">New Customer Query Received</h2>" +
                "<p style=\"font-size:16px;line-height:24px;margin:16px 0;color:#525f7f;text-align:center\">" +
                "A new query has been submitted by a customer. Please review and respond as soon as possible.</p>" +
                "<p style=\"font-size:16px;line-height:24px;margin:16px 0;color:#525f7f;text-align:center\">" +
                "<strong>Query Details:</strong></p>" +
                "<ul style=\"text-align:center;list-style:none;padding:0;\">" +
                "<li style=\"margin:5px 0;\"><strong>Heading:</strong> " + this.queryService.getLatestHeading() + "</li>" +
                "<li style=\"margin:5px 0;\"><strong>Description:</strong> A new query has been submitted. Check the system for details.</li>" +
                "</ul>" +
                "<hr style=\"border:none;border-top:1px solid #eaeaea;width:100%;border-color:#e6ebf1;margin:20px 0\">" +
                "<p style=\"font-size:16px;line-height:24px;margin:16px 0;color:#525f7f;text-align:center\">" +
                "Please log in to the system to respond.</p>" +
                "<p style=\"font-size:16px;line-height:24px;margin:16px 0;color:#525f7f;text-align:center\">" +
                "Best Regards,<br><strong>HitGym Team</strong></p>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        // Send email to all employees
        for (String email : employeeEmails) {
            emailSender.sendEmail(email, subject, emailContent);
        }
    }
}