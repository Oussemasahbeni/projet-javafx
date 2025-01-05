package com.example.semesterProject_2022;

import database.DatabaseFunctions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DashboardPanel_Controller implements Initializable {

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
/*--------*/

    @FXML
    private AnchorPane duePane;

    @FXML
    private AnchorPane expiredPane;

    @FXML
    private Text membershipsDue;

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
        for(int i=0;i<2;i++)
        {
            if(i!=1){
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
        for(int i=0;i<1;i++)
        {
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

        for(int i=1;i<3;i++)
        {
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
        // Fetch data from the database
        double[] monthlyRevenues = DatabaseFunctions.getMonthlyRevenues();
        double[] monthlyExpenses = DatabaseFunctions.getMonthlyExpenses();
        double[] monthlyProfits = new double[12];

        for (int i = 0; i < 12; i++) {
            monthlyProfits[i] = monthlyRevenues[i] - monthlyExpenses[i];
        }

        // Update the text fields
        monthlyRevenue.setText(String.valueOf(monthlyRevenues[11])); // Assuming the last month is the current month
        monthlyExpense.setText(String.valueOf(monthlyExpenses[11]));
        monthlyprofit.setText(String.valueOf(monthlyProfits[11]));

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
            noOfCustomers = DatabaseFunctions.getNumberOfCustomers();
        } catch (Exception e) {
            System.out.println(e);
        }
        totalMembers.setText(String.valueOf(noOfCustomers));
    }
}
