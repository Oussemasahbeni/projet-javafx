package com.esprit.hitgym;

import com.esprit.hitgym.utils.EmailSender;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.mail.MessagingException;
import java.io.IOException;

public class  Main extends Application {

    @Override
    public void start(Stage stage) throws IOException, MessagingException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AdminPanel.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(1280);  // Set the desired width
        stage.setHeight(720);
        stage.show();
        stage.centerOnScreen();
    }


    public static void main(String[] args) {
        launch();
    }
}