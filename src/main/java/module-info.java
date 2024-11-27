module com.example.parkingbookingsystems {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mssql.jdbc;
    requires transitive mysql.connector.j;
    requires transitive fontawesomefx;
    requires jBCrypt;


    opens com.example.parkingbookingsystems to javafx.fxml;
    exports com.example.parkingbookingsystems;
    exports com.example.parkingbookingsystems.ResetPassword;
    opens com.example.parkingbookingsystems.ResetPassword to javafx.fxml;
    exports com.example.parkingbookingsystems.controller;
    opens com.example.parkingbookingsystems.controller to javafx.fxml;
    exports com.example.parkingbookingsystems.session;
    opens com.example.parkingbookingsystems.session to javafx.fxml;
    exports com.example.parkingbookingsystems.entity;
    opens com.example.parkingbookingsystems.entity to javafx.fxml;
    exports com.example.parkingbookingsystems.service;
    opens com.example.parkingbookingsystems.service to javafx.fxml;
}