package com.example.parkingbookingsystems.Controller;

import com.example.parkingbookingsystems.AdminController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.parkingbookingsystems.security.PasswordUtils;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button register_btn;

    @FXML
    private Button returnchoose;

    @FXML
    private Button loginBtn;

    @FXML
    private void close() {
        System.exit(0);
    }

    private PreparedStatement prepare;
    private ResultSet result;
    private double x = 0;
    private double y = 0;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @FXML
    public void logAdmin() throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ?";

        Database db = new Database();
        Connection connect = db.connectdb();

        try {
            if (connect != null) {
                PreparedStatement prepare = connect.prepareStatement(sql);
                prepare.setString(1, username.getText());

                ResultSet result = prepare.executeQuery();

                if (username.getText().isEmpty() || password.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill in all fields");
                    alert.showAndWait();
                } else {
                    if (result.next()) {
                        String hashed = result.getString("password");
                        if (PasswordUtils.hashPassword(password.getText()).equals(hashed)) {

                            // Set the first name and last name based on the user_id
                            this.firstName = result.getString("firstName");
                            this.lastName = result.getString("lastName");
                            this.email = result.getString("email");
                            this.phoneNumber = result.getString("phoneNumber");

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Login successful");
                            alert.showAndWait();

                            loginBtn.getScene().getWindow().hide();
                            Platform.runLater(() -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminProfile.fxml"));
                                    Parent root = loader.load();

                                    AdminController controller = loader.getController();

                                    // Set the username display in the new controller
                                    controller.setFirstName(this.firstName);
                                    controller.setLastName(this.lastName);
                                    controller.setEmail(this.email);
                                    controller.setPhone(this.phoneNumber);
                                    controller.setUsernameDisplayAdmin();

                                    Scene scene = new Scene(root);
                                    Stage stage = new Stage();

                                    root.setOnMousePressed(event -> {
                                        x = event.getSceneX();
                                        y = event.getSceneY();
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
                                }
                            });
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid username or password");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid username or password");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Database connection failed");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (result != null) result.close();
            if (prepare != null) prepare.close();
            if (connect != null) connect.close();
        }
    }

    public void returnToChoose() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/Choose.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) returnchoose.getScene().getWindow();
            currentStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/RegisterAdmin.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) register_btn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}