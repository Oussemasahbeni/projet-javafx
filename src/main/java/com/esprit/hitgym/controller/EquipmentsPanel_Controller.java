package com.esprit.hitgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class EquipmentsPanel_Controller {
    @FXML
    private Text texttbchanged;

    @FXML
    void changetxt(ActionEvent event) {
        texttbchanged.setText("This is Equipments Panel");

    }

    @FXML
    void resetText(ActionEvent event) {
        texttbchanged.setText("Default Text");
        logAction("Text reset to default.");
    }

    private void logAction(String message) {
        System.out.println("[EquipmentsPanel_Controller] " + message);
    }

}
