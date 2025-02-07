package com.esprit.hitgym.controller.queries;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class QueryMenuButton extends MenuButton {
    private int ButtonId;
    private String username, email, heading, description;
    private Boolean status;

    // Add a functional interface to handle the View action
    private ViewActionHandler viewActionHandler;

    public interface ViewActionHandler {
        void onView(QueryMenuButton button);
    }

    public void setViewActionHandler(ViewActionHandler handler) {
        this.viewActionHandler = handler;
    }

    // ADDED: Getter for viewActionHandler
    public ViewActionHandler getViewActionHandler() {
        return viewActionHandler;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getButtonId() {
        return ButtonId;
    }

    public void setButtonId(int buttonId) {
        ButtonId = buttonId;
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

    public QueryMenuButton(String s, int buttonId, String username, String email, String heading, String description) {
        super(s);
        ButtonId = buttonId;
        this.username = username;
        this.email = email;
        this.heading = heading;
        this.description = description;

        MenuItem item1 = new MenuItem("View");

        item1.setOnAction(event -> {
            if (viewActionHandler != null) {
                viewActionHandler.onView(this); // Call the handler when "View" is clicked
            }
        });

        getItems().addAll(item1);
    }
}