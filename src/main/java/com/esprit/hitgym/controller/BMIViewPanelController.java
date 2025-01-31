package com.esprit.hitgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BMIViewPanelController {

    @FXML
    private TableColumn<?, ?> BMI;

    @FXML
    private TableView<?> BMIView;

    @FXML
    private TableColumn<?, ?> Id;

    @FXML
    private TableColumn<?, ?> RecordedDate;

    @FXML
    private TableColumn<?, ?> RecordedMonth;

    @FXML
    private TableColumn<?, ?> Weight;

    @FXML
    private Button addbutton;

    @FXML
    private TextField keyword;

    @FXML
    private Pagination pagination;

    @FXML
    private Button refreshButton;

    @FXML
    private Button sortButton;

    @FXML
    private Button sortButton1;

    @FXML
    void addBMI(ActionEvent event) throws IOException {

    }

    @FXML
    void refreshbtn(ActionEvent event) {

    }

    @FXML
    void sortbtn1(ActionEvent event) {

    }

}
