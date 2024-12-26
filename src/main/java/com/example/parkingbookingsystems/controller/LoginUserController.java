package com.example.parkingbookingsystems.controller;

import com.example.parkingbookingsystems.Database;
import com.example.parkingbookingsystems.session.UserSession;
import com.example.parkingbookingsystems.security.PasswordUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

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
    private Text usernameDisplay;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private double x = 0;
    private double y = 0;

    //    private String firstName;
//    private String lastName;
    private int currentUserId;

    public void close(){
        System.exit(0);
    }


    @FXML
    public void logUser() throws SQLException {
        String sql = "SELECT * FROM [UserCredentials] WHERE username = ?";
        Database db = new Database();
        connect = db.connectdb();

        try {
            if (connect != null) {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username.getText());

                result = prepare.executeQuery();

                if (username.getText().isEmpty() || password.getText().isEmpty()) {
                    showError("Please fill in all fields");
                } else {
                    if (result.next()) {
                        String hashed = result.getString("password");
                        if (PasswordUtils.verifyPassword(password.getText() , hashed)) {


                            String loginUsername = result.getString("username");
                            UserSession.setCurrentUserId(result.getInt("user_id"));
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(null);
                            alert.setContentText("Login successful");
                            alert.showAndWait();

                            loginBtn.getScene().getWindow().hide();
                            Platform.runLater(() -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/frontend/ContentAreaAndUser.fxml"));
                                    Parent root = loader.load();

                                    UserInterface controller = loader.getController();

                                    //Pass the username to the UserInterface controller
                                    controller.setLoginUsername(loginUsername);
                                    controller.loadUserInfo();

//                                    controller.setUsernameDisplay();


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
                            showError("Invalid username or password");
                        }
                    } else {
                        showError("Invalid username or password");
                    }
                }
            } else {
                showError("Database connection failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/frontend/RegisterUser.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/frontend/Choose.fxml"));
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