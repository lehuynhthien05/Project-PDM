package com.example.parkingbookingsystems.Controller;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import com.example.parkingbookingsystems.email.EmailUtils;
import com.example.parkingbookingsystems.phone.PhoneUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdjustUserInfo {
    private String loginUsername;

    @FXML
    private TextField user_email;
    @FXML
    private TextField user_firstname;
    @FXML
    private TextField user_lastname;
    @FXML
    private TextField user_phone;
    @FXML
    private TextField user_username;
    @FXML
    private Button close;
    @FXML
    private Button save_btn;

    public void setUsername(String user_name) {
        this.loginUsername = user_name;
    }

    public void close(ActionEvent event) {
        // Get the current stage (window) and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void loadUserInfo() {
        String sql = "SELECT * FROM [User] WHERE username = ?";
        try (Connection connect = Database.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, this.loginUsername);

            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    user_username.setText(result.getString("username"));
                    user_firstname.setText(result.getString("firstName"));
                    user_lastname.setText(result.getString("lastName"));
                    user_email.setText(result.getString("email"));
                    user_phone.setText(result.getString("phoneNumber"));
                } else {
                    System.out.println("No user found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (!validateInput()) return;

        String sql = "UPDATE [User] SET username = ?, firstName = ?, lastName = ?, email = ?, phoneNumber = ? WHERE username = ?";
        try (Connection connect = Database.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, user_username.getText());
            prepare.setString(2, user_firstname.getText());
            prepare.setString(3, user_lastname.getText());
            prepare.setString(4, user_email.getText());
            prepare.setString(5, user_phone.getText());
            prepare.setString(6, this.loginUsername);

            int rowsAffected = prepare.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Save Successful");
            } else {
                showAlert(Alert.AlertType.ERROR, "No user found to update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to save data. Please try again.");
        }
    }

    private boolean validateInput() {
        if (user_email.getText().isEmpty() || user_username.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields");
            return false;
        }
        if (!EmailUtils.isValidEmail(user_email.getText())) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid email address");
            return false;
        }
        if (!PhoneUtils.isValidPhoneNumber(user_phone.getText())) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid 10-digit phone number");
            return false;
        }
        return true;
    }
}
