package com.example.parkingbookingsystems;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class UserInterface {
    private String loginUsername;


    @FXML
    private Button booking_returnbtn;

    @FXML
    public void close(ActionEvent event) {
        System.exit(0);
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }





    public void UserProfile() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdjustUser.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Home() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/ContentAreaAndUser.fxml"));
            Parent root = loader.load();


            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Booking Interface
    public void Booking() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPickslot.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BookedBtn(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Booking");
        alert.setHeaderText(null);

        alert.setContentText("Are you sure you want to book this slot?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPayment.fxml"));
                Parent root = loader.load();

                // Close the current stage
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Booking_ReturnBtn() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/ContentAreaAndUser.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Payment Interface
    public void Payment() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPayment.fxml"));
            Parent root = loader.load();


            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PaymentCheck() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Payment");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to proceed with the payment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/PaymentCheck .fxml"));
                Parent root = loader.load();

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ContinueBooking() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPickslot.fxml"));
            Parent root = loader.load();


            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReturnBooking() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPickslot.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/LoginUser.fxml"));
                Parent root = loader.load();
                loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.setScene(new Scene(root));
                loginStage.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;




}
