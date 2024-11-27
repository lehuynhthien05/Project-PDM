package com.example.parkingbookingsystems;

import com.example.parkingbookingsystems.security.PasswordUtils;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import com.example.parkingbookingsystems.email.EmailUtils;
import com.example.parkingbookingsystems.phone.PhoneUtils;
import javafx.fxml.FXML;
import org.mindrot.jbcrypt.BCrypt;
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
    @FXML
    private AnchorPane passChange;

    @FXML
    private PasswordField oldPassWord;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField comfirmPassword;
    @FXML
    private Button updatePassBtn;



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

    public void updatePassword() {
        // Validate user input
        if (oldPassWord.getText().isEmpty() || newPassword.getText().isEmpty() || comfirmPassword.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        if (!newPassword.getText().equals(comfirmPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "New password and confirm password do not match.");
            return;
        }



        String sql = "SELECT password FROM [User] WHERE username = ?";

        try (Connection connect = Database.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            // Check if the old password matches the stored password
            prepare.setString(1, this.loginUsername);
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    String hashedPassword = result.getString("password");
                    if (!PasswordUtils.verifyPassword(oldPassWord.getText(), hashedPassword)) {
                        showAlert(Alert.AlertType.ERROR, "Old password is incorrect.");
                        return;
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "User not found.");
                    return;
                }
            }

            // Hash the new password
            String newHashedPassword = PasswordUtils.hashPassword(newPassword.getText());

            // Update the password in the database
            String updateSql = "UPDATE [User] SET password = ? WHERE username = ?";
            try (PreparedStatement updatePrepare = connect.prepareStatement(updateSql)) {
                updatePrepare.setString(1, newHashedPassword);
                updatePrepare.setString(2, this.loginUsername);

                int rowsAffected = updatePrepare.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Password updated successfully.");
                    // Clear the fields
                    oldPassWord.clear();
                    newPassword.clear();
                    comfirmPassword.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to update password.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "An error occurred while updating the password.");
        }
    }



    public void openChangePass() {passChange.setVisible(true);}

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
