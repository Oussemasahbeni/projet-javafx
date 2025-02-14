package com.esprit.hitgym.controller.dashboard;

import com.esprit.hitgym.Entity.Customer;
import com.esprit.hitgym.Entity.Queries;
import com.esprit.hitgym.controller.member.MembersDetailCardController;
import com.esprit.hitgym.controller.queries.QueryMenuButton;
import com.esprit.hitgym.service.CustomerService;
import com.esprit.hitgym.service.ExpensesService;
import com.esprit.hitgym.service.QueryService;
import com.esprit.hitgym.service.RevenueService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardPanelController implements Initializable {

    @FXML
    private TableView<Queries> queriesTable;

    @FXML
    private TableColumn<Queries, String> queryIdColumn;
    @FXML
    private TableColumn<Queries, String> queryNameColumn;
    @FXML
    private TableColumn<Queries, String> queryDetailsColumn;

    private ObservableList<Queries> queriesList = FXCollections.observableArrayList();

    private QueryService queryService = new QueryService();
    /*-Buttons-*/
    @FXML
    private Button PendingButton;
    @FXML
    private Button CompletedButton;
    @FXML
    private Button dueButton;
    @FXML
    private Button expiredButton;
    @FXML
    private Button recentButton;
    @FXML
    private Button HistoryButton;
    @FXML
    private Button RecentButtonExpenses;
    @FXML
    private Button InStockButton;
    @FXML
    private Button OutofStockButton;

    @FXML
    private FlowPane membersFlowPane;

    @FXML
    private FlowPane queryFlowPane;
    /*--------*/

/*
    @FXML
    private AnchorPane duePane;

    @FXML
    private AnchorPane expiredPane;
*//*

    @FXML
    private Text membershipsDue;*/

    @FXML
    private StackPane memberstckpane;
    @FXML
    public ScrollPane scrollpanedashboard = new ScrollPane();
    @FXML
    private Text monthlyExpense;

    @FXML
    private Text monthlyMembers;


    @FXML
    private Text monthlyRevenue;

    @FXML
    private Text monthlyprofit;

    @FXML
    private Text pendingPayments;

    @FXML
    private StackPane ExpensestckPane;
    @FXML
    private AnchorPane recentPane;
    @FXML
    private StackPane itemstckpane;
    @FXML
    private StackPane querystckpane;

    @FXML
    private LineChart<String, Number> monthlyProfitChart;

    @FXML
    private BarChart<String, Number> monthlyExpenseChart;
    @FXML
    private Text totalMembers;
    private int noOfCustomers;

    private final RevenueService revenueService;
    private final ExpensesService expensesService;
    private final CustomerService customerService;

    public DashboardPanelController() {
        this.revenueService = new RevenueService();
        this.expensesService = new ExpensesService();
        this.customerService = new CustomerService();
    }

    @FXML
    void CompeleteBtn(ActionEvent event) {
        System.out.println(querystckpane.getChildren());
        CompletedButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 12 0 0 0;");
        querystckpane.getChildren().get(1).setVisible(true);
        new animatefx.animation.FadeIn(querystckpane).play();
        querystckpane.getChildren().get(0).setVisible(false);
        PendingButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 0 0 0 0;");
    }

    @FXML
    void Pendingbtn(ActionEvent event) {
        PendingButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 0 0 0 0;");
        querystckpane.getChildren().get(0).setVisible(true);
        new animatefx.animation.FadeIn(querystckpane).play();
        querystckpane.getChildren().get(1).setVisible(false);
        CompletedButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 12 0 0 0;");
    }

    @FXML
    void duebtn(ActionEvent event) {
        dueButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 0 0 0 0;");
        memberstckpane.getChildren().get(1).setVisible(true);
        new animatefx.animation.FadeIn(memberstckpane).play();
        for (int i = 0; i < 2; i++) {
            if (i != 1) {
                memberstckpane.getChildren().get(i).setVisible(false);
            }
        }
        expiredButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 0 0 0 0;");
        recentButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 12 0 0 0;");

    }

    @FXML
    void expiredbtn(ActionEvent event) {
        expiredButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 0 0 0 0;");
        memberstckpane.getChildren().get(2).setVisible(true);
        new animatefx.animation.FadeIn(memberstckpane).play();
        for (int i = 0; i < 1; i++) {
            memberstckpane.getChildren().get(i).setVisible(false);
        }
        dueButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 0 0 0 0;");
        recentButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 12 0 0 0;");

    }

    @FXML
    void recentBtn(ActionEvent event) {
        System.out.println(memberstckpane.getChildren());
        recentButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 12 0 0 0;");
        memberstckpane.getChildren().get(0).setVisible(true);
        new animatefx.animation.FadeIn(memberstckpane).play();

        for (int i = 1; i < 3; i++) {
            memberstckpane.getChildren().get(i).setVisible(false);
        }
        dueButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 0 0 0 0;");
        expiredButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 0 0 0 0;");
    }


    @FXML
    void RecentExpBtn(ActionEvent event) {
        RecentButtonExpenses.setStyle("-fx-background-color: #03032c; -fx-background-radius: 12 0 0 0;");
        ExpensestckPane.getChildren().get(0).setVisible(true);
        new animatefx.animation.FadeIn(ExpensestckPane).play();
        ExpensestckPane.getChildren().get(1).setVisible(false);
        HistoryButton.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 0 0 0 0;");
    }

    @FXML
    void HistoryBtn(ActionEvent event) {
        HistoryButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 0 0 0 0;");
        ExpensestckPane.getChildren().get(1).setVisible(true);
        new animatefx.animation.FadeIn(ExpensestckPane).play();
        ExpensestckPane.getChildren().get(0).setVisible(false);
        RecentButtonExpenses.setStyle("-fx-background-color: #8E9DB5; -fx-background-radius: 12 0 0 0;");

    }

    @FXML
    void InStockBtn(ActionEvent event) {
        InStockButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 12 0 0 0;");
        itemstckpane.getChildren().get(0).setVisible(true);
        new animatefx.animation.FadeIn(itemstckpane).play();
        itemstckpane.getChildren().get(1).setVisible(false);
        OutofStockButton.setStyle("-fx-background-color: #8e9db5; -fx-background-radius: 0 0 0 0;");
    }

    @FXML
    void OutofStockBtn(ActionEvent event) {
        InStockButton.setStyle("-fx-background-color: #839db5; -fx-background-radius: 12 0 0 0;");
        itemstckpane.getChildren().get(0).setVisible(false);
        new animatefx.animation.FadeIn(itemstckpane).play();
        itemstckpane.getChildren().get(1).setVisible(true);
        OutofStockButton.setStyle("-fx-background-color: #03032c; -fx-background-radius: 0 0 0 0;");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCustomers();


        // Fetch data from the database
        double[] monthlyExpenses = expensesService.getMonthlyExpenses();
        double[] monthlyProfits = revenueService.getMonthlyRevenues();
        double[] monthlyRevenues = new double[12];

        for (int i = 0; i < 12; i++) {
            monthlyRevenues[i] = monthlyProfits[i] + monthlyExpenses[i];
        }

        // Calculate the sum of the arrays
        double totalRevenue = Arrays.stream(monthlyRevenues).sum();
        double totalExpense = Arrays.stream(monthlyExpenses).sum();
        double totalProfit = totalRevenue - totalExpense;

        // Update the text fields with the sums
        monthlyRevenue.setText(String.valueOf(totalRevenue));
        monthlyExpense.setText(String.valueOf(totalExpense));
        monthlyprofit.setText(String.valueOf(totalProfit));

        // Update the members card
        for (int i = 1; i < 3; i++) {
            memberstckpane.getChildren().get(i).setVisible(false);
        }

        // Update the monthly profit chart
        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Monthly Profit in Dinars");
        for (int i = 0; i < 12; i++) {
            profitSeries.getData().add(new XYChart.Data<>(String.valueOf(i + 1), monthlyProfits[i]));
        }
        monthlyProfitChart.getData().add(profitSeries);

        // Update the monthly expense chart
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Monthly Expense in Dinars");
        for (int i = 0; i < 12; i++) {
            expenseSeries.getData().add(new XYChart.Data<>(String.valueOf(i + 1), monthlyExpenses[i]));
        }
        monthlyExpenseChart.getData().add(expenseSeries);

        // Fetch and update the total number of customers
        try {
            noOfCustomers = customerService.findNumberOfCustomers();
        } catch (Exception e) {
            System.out.println(e);
        }
        totalMembers.setText(String.valueOf(noOfCustomers));


        // Charger les queries depuis le service
        loadQueriess();
    }

    private void loadQueriess() {
        Task<List<Queries>> loadCustomersTask = new Task<>() {
            @Override
            protected List<Queries> call() {
                return queryService.getAllQueries(); // Récupérer la liste des clients
            }
        };

        loadCustomersTask.setOnSucceeded(event -> {
            List<Queries> queries = loadCustomersTask.getValue();
            queryFlowPane.getChildren().clear(); // Nettoyer avant d'ajouter de nouveaux clients

            for (Queries customer : queries) {
                HBox customerCard = createQueryCard(customer);
                queryFlowPane.getChildren().add(customerCard);
            }
        });

        loadCustomersTask.setOnFailed(event -> {
            System.err.println("Erreur lors du chargement des clients: " + loadCustomersTask.getException().getMessage());
        });

        new Thread(loadCustomersTask).start();
    }

    private void loadCustomers() {
        Task<List<Customer>> loadCustomersTask = new Task<>() {
            @Override
            protected List<Customer> call() {
                return customerService.findAllCustomersObs(); // Récupérer la liste des clients
            }
        };

        loadCustomersTask.setOnSucceeded(event -> {
            List<Customer> customers = loadCustomersTask.getValue();
            membersFlowPane.getChildren().clear(); // Nettoyer avant d'ajouter de nouveaux clients

            for (Customer customer : customers) {
                HBox customerCard = createCustomerCard(customer);
                membersFlowPane.getChildren().add(customerCard);
            }
        });

        loadCustomersTask.setOnFailed(event -> {
            System.err.println("Erreur lors du chargement des clients: " + loadCustomersTask.getException().getMessage());
        });

        new Thread(loadCustomersTask).start();
    }


    private void showMemberDetails(Customer customer) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/hitgym/membersDetailCard.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer les informations du client
            MembersDetailCardController controller = loader.getController();
            controller.FullName = customer.getFirstName() + " " + customer.getLastName();
            controller.Weight = customer.getWeight();
            controller.Address = customer.getAddress();
            controller.Emails = customer.getEmail();






            // Créer et afficher la popup
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Details");
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquer la fenêtre principale
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private HBox createCustomerCard(Customer customer) {
        HBox card = new HBox();
        card.getStyleClass().add("myownHBOX"); // Appliquer le style CSS
        card.setPadding(new Insets(10));
        card.setSpacing(20);
        card.setPrefHeight(100);
        card.setPrefWidth(493);

        // VBox pour organiser le nom, le numéro et le statut en colonne
        VBox textContainer = new VBox();
        textContainer.setSpacing(5);

        // Nom et prénom du client
        Text nameText = new Text(customer.getFirstName() + " " + customer.getLastName());
        nameText.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Numéro du client
        Text numberText = new Text("Number: " + customer.getPhoneNumber());
        numberText.setFont(Font.font("System", FontWeight.NORMAL, 14));
        numberText.setFill(Color.DARKGRAY);

        // Statut du client
        Text statusText = new Text(customer.isActive() ? "Active" : "Pending");
        statusText.setFont(Font.font("System", FontPosture.ITALIC, 14));

        // Changer la couleur du texte en fonction du statut
        if (customer.isActive()) {
            statusText.setFill(Color.GREEN); // 🟢 Vert si actif
        } else {
            statusText.setFill(Color.RED); // 🔴 Rouge si en attente
        }

        // Ajouter les textes au conteneur vertical
        textContainer.getChildren().addAll(nameText, numberText, statusText);
        HBox.setMargin(textContainer, new Insets(20, 0, 0, 20));

        // Bouton "View"
        Button viewButton = new Button("View");
        viewButton.setPrefSize(111, 25);
        viewButton.getStyleClass().add("myownbuttons"); // Appliquer le CSS
        HBox.setMargin(viewButton, new Insets(30, 20, 0, 80));

        // Ajouter les éléments à la carte
        card.getChildren().addAll(textContainer, viewButton);
        // Action du bouton pour ouvrir la popup
        viewButton.setOnAction(event -> showMemberDetails(customer));


        return card;
    }

    private HBox createQueryCard(Queries customer) {

        HBox card = new HBox();

        card.getStyleClass().add("myownHBOX"); // Appliquer le style CSS
        card.setPadding(new Insets(10));
        card.setSpacing(20);
        card.setPrefHeight(100);

        card.setPrefWidth(493);

        // VBox pour organiser le nom, le numéro et le statut en colonne
        VBox textContainer = new VBox();
        textContainer.setSpacing(5);

        // Nom et prénom du client
        Text nameText = new Text(customer.getEmail());
        nameText.setFont(Font.font("System", FontWeight.BOLD, 18));



        // Ajouter les textes au conteneur vertical
        textContainer.getChildren().addAll(nameText);
        HBox.setMargin(textContainer, new Insets(20, 0, 0, 20));

        // Bouton "View"
        Button viewButton = new Button("View");
        viewButton.setPrefSize(111, 25);
        viewButton.getStyleClass().add("myownbuttons"); // Appliquer le CSS
        HBox.setMargin(viewButton, new Insets(30, 20, 0, 80));

        // Ajouter les éléments à la carte
        card.getChildren().addAll(textContainer, viewButton);
        // Action du bouton pour ouvrir la popup
        //viewButton.setOnAction(event -> showMemberDetails(customer));


        return card;
    }

    private String formatDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private void handleViewCustomer(Customer customer) {
        // Handle view action here
        System.out.println("Viewing customer: " + customer.getFirstName());
    }
}

