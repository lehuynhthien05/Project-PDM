package com.example.parkingbookingsystems;
import javafx.scene.control.Label;
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
    private Label userEmail;
    @FXML
    private TextField firstNameUser;
    @FXML
    private TextField lastNameUser;
    @FXML
    private Label userPhone;
    @FXML
    private Label userName;
    @FXML
    private Button close;
    @FXML
    private Button saveInfor;

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
                    userName.setText(result.getString("username"));
                    firstNameUser.setText(result.getString("firstName"));
                    lastNameUser.setText(result.getString("lastName"));
                    userEmail.setText(result.getString("email"));
                    userPhone.setText(result.getString("phoneNumber"));
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

            prepare.setString(1, userName.getText());
            prepare.setString(2, firstNameUser.getText());
            prepare.setString(3, lastNameUser.getText());
            prepare.setString(4, userEmail.getText());
            prepare.setString(5, userPhone.getText());
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
        if (userEmail.getText().isEmpty() || userName.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields");
            return false;
        }
        if (!EmailUtils.isValidEmail(userEmail.getText())) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid email address");
            return false;
        }
        if (!PhoneUtils.isValidPhoneNumber(userPhone.getText())) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid 10-digit phone number");
            return false;
        }
        return true;
    }
}
