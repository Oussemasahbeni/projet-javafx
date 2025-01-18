package com.esprit.hitgym.controller.account;

import com.esprit.hitgym.GeneralFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StatusActivePopupController {

    @FXML
    private Button okayButton;

    @FXML
    void okayBtn(ActionEvent event) {
        new GeneralFunctions().close(okayButton);
    }

}
