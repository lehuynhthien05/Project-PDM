package com.example.parkingbookingsystems;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class UserInterface {
    private String loginUsername;

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


    @FXML
    private void handleHBoxClick(MouseEvent event) {
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


    @FXML
    private void SignOut(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to sign out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/LoginUser.fxml"));
                Parent root = loader.load();

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;




}
