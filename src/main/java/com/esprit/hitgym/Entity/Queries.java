package com.esprit.hitgym.Entity;

import com.esprit.hitgym.GeneralFunctions;
import com.esprit.hitgym.controller.queries.QueriesReplyController;
import com.esprit.hitgym.controller.queries.QueryMenuButton;
import com.esprit.hitgym.helpers.CustomDate;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.sql.Date;

public class Queries {
    private int id;
    private String username;
    private String email;
    private String heading;
    private String description;
    private Boolean status;
    private String StatusString;
    private Date current_date;

    private QueryMenuButton actionBtn;
    // REMOVED: private MenuItem item1 = new MenuItem("View"); // REMOVE DUPLICATE VIEW ITEM
    private MenuItem item3 = new MenuItem("Reply");

    // ADDED: Reply field
    private String reply;


    public String getStatusString() {
        return StatusString;
    }

    public void setStatusString(String statusString) {
        StatusString = statusString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QueryMenuButton getActionBtn() {
        return actionBtn;
    }

    public void setActionBtn(QueryMenuButton actionBtn) {
        this.actionBtn = actionBtn;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }

    // ADDED: Getter and Setter for reply
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }


    public Queries(Boolean status, int id, String username, String email, String heading, String description, QueryMenuButton actionBtn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.heading = heading;
        this.description = description;
        this.status = status;
        if (status) {
            StatusString = "Completed";
        } else {
            StatusString = "Pending";
        }
        this.actionBtn = actionBtn;
        this.actionBtn.setStyle("-fx-background-color: #00C2FF; -fx-background-radius: 12px;");
        this.actionBtn.setTextFill(Paint.valueOf("White"));

        // REMOVED: actionBtn.getItems().addAll(item1, item3); // REMOVE DUPLICATE VIEW ITEM
        actionBtn.getItems().addAll(item3); // KEEP ONLY REPLY OPTION

        // REMOVED: item1 Action Handler - View action now handled only by QueryMenuButton
        /*item1.setOnAction(event ->
        {
            // VIEW ACTION IS NOW HANDLED VIA DIALOG IN QueriesPanelController - DO NOTHING HERE.
             if (actionBtn.getViewActionHandler() != null) { // Ensure handler is set
                actionBtn.getViewActionHandler().onView(actionBtn); // Call the handler
            }
        });*/


        item3.setOnAction(event ->
        {
            QueriesReplyController.Id = actionBtn.getButtonId();
            try {
                new GeneralFunctions().switchSceneModality("QueryReplyForm.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public Queries(int id, String username, String email, String heading, String description, Boolean status) {
        this.id = id;
        this.description = description;
        this.email = email;
        this.username = username;
        this.heading = heading;
        this.status = status;
        this.current_date = CustomDate.getCurrentDate();
    }


    public String getLowerUserName() {
        return getUsername().toLowerCase();
    }

}