module com.esprit.semesterproject_2022 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.codec;
    requires com.mailjet.api;
    requires AnimateFX;
    requires org.jetbrains.annotations;

    opens com.esprit.hitgym to javafx.fxml;
    exports com.esprit.hitgym;

    exports com.esprit.hitgym.model;
    opens com.esprit.hitgym.model to javafx.fxml;
}