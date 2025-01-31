package com.esprit.hitgym.controller.auth;

import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.controller.LoadingScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginSignUpController {

    // local variables not from FXML (**declare non-fxml fields here**)

    /*X and Y coordinates for dragging window (Start) */
    private double x = 0;
    private double y = 0;
    /*--------------------------------------- (End)*/

    // ------------------

    @FXML
    private AnchorPane pane;
    @FXML
    private Button exit;
    LoadingScreenController obj = new LoadingScreenController();

    public void switchToSignUp(ActionEvent e) throws IOException {
        new GeneralFunctions().switchScene(e, "SignUp.fxml");
    }

    public void switchToMemberLogin(ActionEvent e) throws IOException {
        new GeneralFunctions().switchScene(e, "MemberLogin_Form.fxml");
    }

    public void switchToAdminLogin(ActionEvent e) throws IOException {
        new GeneralFunctions().switchScene(e, "AdminLoginForm.fxml");
    }

    public void switchToExplore(ActionEvent e) throws IOException {
        new GeneralFunctions().switchScene(e, "Explore.fxml");
    }


    @FXML
    public void close() {
        new GeneralFunctions().close(exit);
    }


    /*DRAGGING WINDOW LOGIC STARTS HERE -----------*/
    @FXML
    void dragged(MouseEvent event) {
        obj.stage = (Stage) pane.getScene().getWindow();
        obj.stage.setX(event.getScreenX() - x);
        obj.stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    /*DRAGGING WINDOW LOGIC ENDS HERE -----------*/

}
