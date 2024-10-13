package com.example.parkingbookingsystems;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUserController {

    @FXML
    private Button close;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Button return_btn;

    @FXML
    private Button registerBtn;

    @FXML
    public void close(ActionEvent event) {
        System.exit(0);
    }


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private double x = 0;
    private double y = 0;

    @FXML
    void logUser(ActionEvent event) {
        String sql = "SELECT * FROM Usertable WHERE username = ? AND password = ?";
        Database db = new Database();

        try (Connection connect = db.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            // Input validation
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
                return; // Exit if fields are empty
            }

            // Set parameters
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Login successful");
                    alert.showAndWait();

                    loginBtn.getScene().getWindow().hide();
                    Platform.runLater(() -> {
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("/com/example/employeemanagementsystem/dashboard.fxml"));
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();

                            // Dragging functionality
                            root.setOnMousePressed(mouseEvent -> {
                                x = mouseEvent.getSceneX();
                                y = mouseEvent.getSceneY();
                            });

                            root.setOnMouseDragged(mouseEvent -> {
                                stage.setX(mouseEvent.getScreenX() - x);
                                stage.setY(mouseEvent.getScreenY() - y);
                            });

                            stage.initStyle(StageStyle.TRANSPARENT);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            showError("Failed to load dashboard.");
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error occurred.");
        }
    }

    public void registerButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/RegisterUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) registerBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void returnToChoose(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/Choose.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) return_btn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
