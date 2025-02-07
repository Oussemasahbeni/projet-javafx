package com.esprit.hitgym.controller.customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

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

public class AiAssistant implements Initializable {

    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField messageInput;
    @FXML
    private ProgressIndicator progressIndicator;

    private ObservableList<String> chatMessages;

    private static final String GEMINI_API_KEY = "AIzaSyADrrYvtyFmts91g-OrSFvajV-VpaWD14E";

    @FXML
    private void sendMessage() {
        String message = messageInput.getText();
        if (message.isEmpty()) {
            return;
        }

        // Show the progress indicator
        progressIndicator.setVisible(true);

        // Clear the message input field
        messageInput.clear();

        // Append the user's message to the chat display
        appendToChatDisplay("You: " + message);

        // Get the response from the API
        getGeminiResponse(message);
    }

    private void appendToChatDisplay(String text) {
        chatMessages.add(text);
    }

    private void getGeminiResponse(String message) {
        // Run API call in a separate thread to avoid blocking the UI thread
        Thread apiThread = new Thread(() -> {
            try {
                String geminiResponse = callGeminiApi(message);
                // Update UI on the JavaFX Application Thread
                Platform.runLater(() -> {
                    appendToChatDisplay("AI: " + geminiResponse);
                    progressIndicator.setVisible(false); // Hide the progress indicator
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    appendToChatDisplay("Error communicating with Gym Assistant.");
                    progressIndicator.setVisible(false); // Hide the progress indicator
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

        String systemicMessage = "You are a gym expert and a gym trainer. Please provide advice and answers accordingly.";
        String jsonInputString = String.format("""
            {
              "contents": [{
                "parts":[{"text": "%s"}]
                }]
               }
            """, systemicMessage + " " + message);

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
        chatMessages = FXCollections.observableArrayList();
        chatListView.setItems(chatMessages);
        chatListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            if (item.startsWith("You:")) {
                                setStyle("-fx-background-color: #d1e7dd; -fx-padding: 10; -fx-background-radius: 10;");
                            } else if (item.startsWith("AI:")) {
                                setStyle("-fx-background-color: #d1e0e7; -fx-padding: 10; -fx-background-radius: 10;");
                            }
                        }
                    }
                };
            }
        });
        appendToChatDisplay("Gym Assistant: Hello! How can I help you today?"); // Initial greeting
    }
}