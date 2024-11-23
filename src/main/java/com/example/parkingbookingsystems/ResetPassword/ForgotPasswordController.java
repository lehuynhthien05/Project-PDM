package com.example.parkingbookingsystems.ResetPassword;

import com.example.parkingbookingsystems.Controller.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ForgotPasswordController {

    @FXML
    private TextField emailOrPhone;

    @FXML
    private void handleSubmit() {
        String contact = emailOrPhone.getText();
        if (contact.isEmpty()) {
            showAlert("Error", "Please enter your email or phone number.");
            return;
        }

        String token = UUID.randomUUID().toString();
        saveToken(contact, token);
        sendResetLink(contact, token);
    }

    private void saveToken(String contact, String token) {
        String sql = "UPDATE admin SET reset_token = ? WHERE email = ? OR phone = ?";
        try (Connection connect = Database.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, token);
            prepare.setString(2, contact);
            prepare.setString(3, contact);
            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendResetLink(String contact, String token) {
        // Send the reset link to the user's email or phone number
        String resetLink = "http://yourapp.com/reset_password?token=" + token;
        System.out.println("Reset link: " + resetLink);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}