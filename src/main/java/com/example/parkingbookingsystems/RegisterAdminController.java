package com.example.parkingbookingsystems;

import com.example.parkingbookingsystems.email.EmailUtils;
import com.example.parkingbookingsystems.phone.PhoneUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Button;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import com.example.parkingbookingsystems.security.PasswordUtils;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterAdminController {

    @FXML
    private TextField register_email;

    @FXML
    private TextField register_firstname;

    @FXML
    private TextField register_lastname;

    @FXML
    private TextField register_password;

    @FXML
    private TextField register_phone;

    @FXML
    private TextField register_username;

    @FXML
    private Button register_btn;

    @FXML
    private Button close;

    @FXML
    private Hyperlink loginbtn;

    public void close(){
        System.exit(0);
    }

    private double x = 0;
    private double y = 0;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void returnAdminLogin() {
        try {
            Stage stage = (Stage) loginbtn.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/parkingbookingsystems/LoginAdmin.fxml"));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerButton() {
        if (!EmailUtils.isValidEmail(register_email.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email address");
            alert.showAndWait();
            return;
        }
        if (!PhoneUtils.isValidPhoneNumber(register_phone.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid 10-digit phone number");
            alert.showAndWait();
            return;
        }
        if (register_email.getText().isEmpty() || register_username.getText().isEmpty() || register_password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else {
            String sql = "INSERT INTO admin (username, password, firstName, lastName, email, phoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
            connect = Database.connectdb();

            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, register_username.getText());

                // Hash the password
                String hashedPassword = PasswordUtils.hashPassword(register_password.getText());
                prepare.setString(2, hashedPassword);

                prepare.setString(3, register_firstname.getText());
                prepare.setString(4, register_lastname.getText());
                prepare.setString(5, register_email.getText());
                prepare.setString(6, register_phone.getText());

                prepare.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Registration Successful");
                alert.showAndWait();

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
    }
}