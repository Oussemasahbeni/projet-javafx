package com.esprit.hitgym.controller.queries;

import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.db.DatabaseFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class QueriesReply_Controller implements Initializable {

    public static Integer Id = 0;

    @FXML
    private Text Heading;

    @FXML
    private TextArea ReplyBox;

    @FXML
    private Text description;

    @FXML
    private Button exitButton;
    private String reply;

    @FXML
    void ReplyQueryBtn(ActionEvent event) {

        reply = ReplyBox.getText();

        DatabaseFunctions.saveToDb(reply, Id);

        Stage stage = (Stage) exitButton.getScene().getWindow();

        stage.close();
    }

    @FXML
    void exit(ActionEvent event) {
        new GeneralFunctions().close(exitButton);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Heading.setText(" ");
        description.setText(" ");

    }

}