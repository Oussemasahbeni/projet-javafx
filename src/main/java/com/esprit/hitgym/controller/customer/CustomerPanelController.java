package com.esprit.hitgym.controller.customer;

import com.esprit.hitgym.Changefxml;
import com.esprit.hitgym.Entity.Customer;
import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.controller.dashboard.DashboardPanelController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerPanelController implements Initializable {

    public static Customer Customer = new Customer();

    @FXML
    private StackPane stackPane;
    @FXML
    private Button closeBtn;
    @FXML
    private Button maxBtn;
    @FXML
    private Button logout;
    @FXML
    private AnchorPane navPanel;
    @FXML
    private Button restoreBtn;

    private static int Menu_Counter = 0;

    // Panel management
    private final ArrayList<Changefxml> panels = new ArrayList<>();

    /*--- ChangeFxml Class objects ---*/
    private final Changefxml dashboardPanel = new Changefxml();
    private final Changefxml BMIViewPanel = new Changefxml();
    private final Changefxml accountSettingsPanel = new Changefxml();
    private final Changefxml QueriesFormPanel = new Changefxml();
    private final Changefxml equipmentsPanel = new Changefxml();
    private final Changefxml FAQPanel = new Changefxml();
    private final Changefxml aiAssistantPanel = new Changefxml();

    @FXML
    void close() {
        new GeneralFunctions().close(closeBtn);
    }

    @FXML
    void max() {
        new GeneralFunctions().maxmin(maxBtn);
    }

    @FXML
    void restore() {
        new GeneralFunctions().restoring(restoreBtn);
    }

    @FXML
    void menuBar() {
        if (Menu_Counter == 0) {
            animatePanel(navPanel, 0);
            animatePanel(stackPane, -80);
            Menu_Counter = 1;
        } else {
            animatePanel(navPanel, -186);
            animatePanel(stackPane, -186);
            Menu_Counter = 0;
        }
    }

    private void animatePanel(AnchorPane pane, int position) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), pane);
        transition.setToX(position);
        transition.play();
    }

    private void animatePanel(StackPane pane, int position) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), pane);
        transition.setToX(position);
        transition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load FXML files
        dashboardPanel.getfxml("CustomerDashboardPanel.fxml");
        BMIViewPanel.getfxml("BMIViewPanel.fxml");
        QueriesFormPanel.getfxml("CustomerQueriesFormPanel.fxml");
        equipmentsPanel.getfxml("EquipmentsPanel.fxml");
        accountSettingsPanel.getfxml("AccountSettingsPanel.fxml");
        FAQPanel.getfxml("FAQPanel.fxml");
        aiAssistantPanel.getfxml("AiAssistant.fxml");

        // Add panels to the list
        panels.add(accountSettingsPanel);
        panels.add(BMIViewPanel);
        panels.add(QueriesFormPanel);
        panels.add(equipmentsPanel);
        panels.add(FAQPanel);
        panels.add(dashboardPanel);
        panels.add(aiAssistantPanel);

        // Add panels to StackPane
        for (Changefxml panel : panels) {
            stackPane.getChildren().add(panel.pane);
        }

        // Initially show only the dashboard
        setActivePanel(5);

        // Animate stack pane on startup
        new animatefx.animation.FadeIn(stackPane).play();

        // Initial menu state
        if (Menu_Counter == 0) {
            navPanel.setTranslateX(-186);
            stackPane.setTranslateX(-186);
        }
    }

    private void setActivePanel(int activeIndex) {
        for (int i = 0; i < panels.size(); i++) {
            stackPane.getChildren().get(i).setVisible(i == activeIndex);
        }
        new animatefx.animation.FadeIn(stackPane).play();
    }

    @FXML
    void logoutBtn(ActionEvent e) throws IOException {
        new GeneralFunctions().close(logout);
        new GeneralFunctions().switchScene("LoginSignUp.fxml");
        File file = new File("credentials.properties");
        if (file.exists()) {
            file.delete();
        }
    }

    @FXML
    void AccountSettingsBtn() {
        setActivePanel(0);
    }

    @FXML
    void DashboardBtn() {
        setActivePanel(5);
    }

    @FXML
    void BMIBtn() {
        setActivePanel(1);
    }

    @FXML
    void EquipmentsBtn() {
        setActivePanel(3);
    }

    @FXML
    void FaqBtn() {
        setActivePanel(4);
    }

    @FXML
    void QueriesBtn() {
        setActivePanel(2);
    }

    @FXML
    public void aiAssistant() {
        setActivePanel(6);
    }
}
