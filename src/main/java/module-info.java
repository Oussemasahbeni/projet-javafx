module com.esprit.semesterproject_2022 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.codec;
    requires com.mailjet.api;
    requires AnimateFX;
    requires org.jetbrains.annotations;
    requires jbcrypt;

    opens com.esprit.hitgym to javafx.fxml;
    exports com.esprit.hitgym;

    exports com.esprit.hitgym.Entity;
    opens com.esprit.hitgym.Entity to javafx.fxml;
    exports com.esprit.hitgym.controller;
    opens com.esprit.hitgym.controller to javafx.fxml;
    exports com.esprit.hitgym.view;
    opens com.esprit.hitgym.view to javafx.fxml;
    exports com.esprit.hitgym.controller.auth;
    opens com.esprit.hitgym.controller.auth to javafx.fxml;
    exports com.esprit.hitgym.controller.customer;
    opens com.esprit.hitgym.controller.customer to javafx.fxml;
    exports com.esprit.hitgym.controller.employees;
    opens com.esprit.hitgym.controller.employees to javafx.fxml;
    exports com.esprit.hitgym.controller.member;
    opens com.esprit.hitgym.controller.member to javafx.fxml;
    exports com.esprit.hitgym.controller.dashboard;
    opens com.esprit.hitgym.controller.dashboard to javafx.fxml;
    exports com.esprit.hitgym.controller.queries;
    opens com.esprit.hitgym.controller.queries to javafx.fxml;
    exports com.esprit.hitgym.controller.account;
    opens com.esprit.hitgym.controller.account to javafx.fxml;
    exports com.esprit.hitgym.controller.explore;
    opens com.esprit.hitgym.controller.explore to javafx.fxml;
    exports com.esprit.hitgym.utils;
    opens com.esprit.hitgym.utils to javafx.fxml;
}