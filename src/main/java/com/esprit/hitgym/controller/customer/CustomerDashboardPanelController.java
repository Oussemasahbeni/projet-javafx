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
    private TextArea chatDisplayArea;
    @FXML
    private TextField messageInput;
    private static final String GEMINI_API_KEY = "AIzaSyADrrYvtyFmts91g-OrSFvajV-VpaWD14E"; // Replace with your actual API key

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

    @FXML
    void sendMessage(ActionEvent event) {
        String message = messageInput.getText();
        if (!message.trim().isEmpty()) {
            appendToChatDisplay("You: " + message); // Display user message
            messageInput.clear();
            getGeminiResponse(message); // Call Gemini API
        }
    }

    private void appendToChatDisplay(String text) {
        chatDisplayArea.appendText(text + "\n");
    }

    private void getGeminiResponse(String message) {
        // Run API call in a separate thread to avoid blocking the UI thread
        Thread apiThread = new Thread(() -> {
            try {
                String geminiResponse = callGeminiApi(message);
                // Update UI on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> {
                    appendToChatDisplay(geminiResponse); // Display directly, no prefix
                });
            } catch (IOException e) {
                javafx.application.Platform.runLater(() -> {
                    appendToChatDisplay("Error communicating with Gym Assistant.");
                });
                e.printStackTrace();
            }
        });
        apiThread.setDaemon(true); // Allow app to exit even if this thread is running
        apiThread.start();
    }


    private String callGeminiApi(String message) throws IOException {
        String encodedApiKey = URLEncoder.encode(GEMINI_API_KEY, StandardCharsets.UTF_8);
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + encodedApiKey;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = String.format("""
                {
                  "contents": [{
                    "parts":[{"text": "%s"}]
                    }]
                   }
                """, message);


        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        String responseString;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            responseString = bufferedReader.lines().collect(Collectors.joining("\n")); // Read complete response
        }

        // Parse JSON response and extract text
        JSONObject responseJson = new JSONObject(responseString);
        JSONArray candidates = responseJson.getJSONArray("candidates");
        if (candidates.length() > 0) {
            JSONObject candidate = candidates.getJSONObject(0);
            JSONObject content = candidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            if (parts.length() > 0) {
                JSONObject part = parts.getJSONObject(0);
                return part.getString("text").trim(); // Extract text and trim whitespace
            }
        }

        return "Sorry, I couldn't understand the response."; // Fallback message


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueriesStckPane.getChildren().get(0).setVisible(true);
        QueriesStckPane.getChildren().get(1).setVisible(false);
        appendToChatDisplay("Gym Assistant: Hello! How can I help you today?"); // Initial greeting
    }
}