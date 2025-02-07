package com.esprit.hitgym.controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.json.JSONArray; // Import JSONArray from org.json
import org.json.JSONObject; // Import JSONObject from org.json

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomerDashboardPanelController implements Initializable {

    @FXML
    private Text BMIDescription;
    @FXML
    private Text BMIText;
    @FXML
    private Text BMIValue;
    @FXML
    private Button CompletedButton;
    @FXML
    private AnchorPane CompletedPane;
    @FXML
    private Button PendingButton;
    @FXML
    private AnchorPane PendingPane;
    @FXML
    private StackPane QueriesStckPane;

    @FXML
    void CompletedBtn(ActionEvent event) {
        QueriesStckPane.getChildren().get(0).setVisible(true);
        QueriesStckPane.getChildren().get(1).setVisible(false);
        new animatefx.animation.FadeIn(QueriesStckPane).play();
    }

    @FXML
    void PendingBtn(ActionEvent event) {
        QueriesStckPane.getChildren().get(0).setVisible(false);
        QueriesStckPane.getChildren().get(1).setVisible(true);
        new animatefx.animation.FadeIn(QueriesStckPane).play();
    }

    @FXML
    void goToBMIView(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueriesStckPane.getChildren().get(0).setVisible(true);
        QueriesStckPane.getChildren().get(1).setVisible(false);
    }
}