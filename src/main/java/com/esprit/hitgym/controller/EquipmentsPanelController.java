package com.esprit.hitgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class EquipmentsPanelController {
    @FXML
    private Text texttbchanged;

    @FXML
    void changetxt(ActionEvent event) {
        texttbchanged.setText("This is Equipments Panel");

    }

}
