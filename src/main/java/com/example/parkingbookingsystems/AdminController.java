package com.example.parkingbookingsystems;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;

public class AdminController {

    @FXML
    private Label adminEmail;

    @FXML
    private Label adminName;

    @FXML
    private Label adminName1;

    @FXML
    private Label adminPhone;

    @FXML
    private HBox adminProfile;

    @FXML
    private Button returnHome;

    @FXML
    private HBox analytic;

    @FXML
    private HBox overview;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsernameDisplayAdmin() {
        adminName.setText(lastName + " " + firstName);
        adminName1.setText(lastName + " " + firstName);
        adminEmail.setText(email);
        adminPhone.setText(phone);
    }


    public void AdminProfile() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminProfile.fxml"));
            Parent root = loader.load();

            //hide
            Stage contentAreaStage = (Stage) adminProfile.getScene().getWindow();
            contentAreaStage.hide();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void User() {
        try {
            Stage contentAreaStage = (Stage) adminProfile.getScene().getWindow();
            contentAreaStage.hide();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminControlUser.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Analytics() {
        try {
            Stage contentAreaStage = (Stage) analytic.getScene().getWindow();
            contentAreaStage.hide();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminAnalysis.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Parkinglot() {
        try {
            Stage contentAreaStage = (Stage) overview.getScene().getWindow();
            contentAreaStage.hide();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminParkingLotOverview.fxml"));
            Parent root = loader.load();

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
                // Close all other open stages except loginStage
                Window window = ((Node) event.getSource()).getScene().getWindow();
                window.hide();


                // Open the login stage
                Stage loginStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/loginAdmin.fxml"));
                Parent root = loader.load();
                loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.setScene(new Scene(root));
                loginStage.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void returnToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminProfile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) returnHome.getScene().getWindow();
            currentStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}