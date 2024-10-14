package com.example.parkingbookingsystems;
import com.example.parkingbookingsystems.security.PasswordUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInterface {
    private String loginUsername;
    @FXML
    private Button close;
    @FXML
    private Label updateLabel;

    @FXML
    public void close(ActionEvent event) {
        System.exit(0);
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public void openProfile()
    {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdjustUser.fxml"));
            Parent root = loader.load();

            // Pass the username to the AdjustUserInfo controller
            AdjustUserInfo controller = loader.getController();
            controller.setUsername(loginUsername);
            controller.loadUserInfo();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;




}
