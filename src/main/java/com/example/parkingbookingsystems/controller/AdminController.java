package com.example.parkingbookingsystems.controller;

import com.example.parkingbookingsystems.*;
import com.example.parkingbookingsystems.entity.BookingUser;
import com.example.parkingbookingsystems.entity.Parking;
import com.example.parkingbookingsystems.entity.Statistics;
import com.example.parkingbookingsystems.security.PasswordUtils;
import com.example.parkingbookingsystems.service.BookingUserService;
import com.example.parkingbookingsystems.service.ParkingService;
import com.example.parkingbookingsystems.service.StatisticsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {



    @FXML
    private VBox profileArea;

    @FXML
    private Button returnHome;

    @FXML
    private VBox contentAdmin;

    @FXML
    private VBox analyticsBar;

    @FXML
    private VBox parkingLotOverviewPage;




    public void AdminProfile() {
        switchPage(profileArea);
    }


    public void switchPage(VBox targetPage) {
        // Hide all pages
        profileArea.setVisible(false);
        contentAdmin.setVisible(false);
        analyticsBar.setVisible(false);
        parkingLotOverviewPage.setVisible(false);
        // Show the target page
        targetPage.setVisible(true);
    }


    private String loginUsername;

    @FXML
    public void close(ActionEvent event) {
        System.exit(0);
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    @FXML
    private Button AdminName;





    @FXML
    private Label adminEmail;

    @FXML
    private Label adminName;


    @FXML
    private TextField firstNameAdmin;

    @FXML
    private TextField lastNameAdmin;
    @FXML
    private Label adminPhone;

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

        String sql = """
                    SELECT ac.password
                    FROM AdminCredentials ac
                    JOIN [Admin] a ON ac.admin_id = a.admin_id
                    WHERE ac.username = ?
                    """;

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
                    showAlert(Alert.AlertType.ERROR, "Admin not found.");
                    return;
                }
            }

            // Hash the new password
            String newHashedPassword = PasswordUtils.hashPassword(newPassword.getText());

            // Update the password in the AdminCredentials table
            String updateSql = """
                            UPDATE AdminCredentials
                            SET password = ?
                            WHERE username = ?
                            """;
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
                    passChange.setVisible(false);
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

    public void loadAdminInfo() {
        String sql = """
        SELECT a.firstName, a.lastName, a.email, a.phoneNumber, ac.username
        FROM [Admin] a
        JOIN AdminCredentials ac ON a.admin_id = ac.admin_id
        WHERE ac.username = ?
        """;

        try (Connection connect = Database.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            // Set the username parameter
            prepare.setString(1, this.loginUsername);

            // Execute query
            try (ResultSet result = prepare.executeQuery()) {
                if (result.next()) {
                    // Extract fields
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    String email = result.getString("email");
                    String phoneNumber = result.getString("phoneNumber");
                    String username = result.getString("username");

                    // Update UI components
                    AdminName.setText(firstName + " " + lastName);
                    adminName.setText(username);
                    firstNameAdmin.setText(firstName);
                    lastNameAdmin.setText(lastName);
                    adminEmail.setText(email);
                    adminPhone.setText(phoneNumber);
                } else {
                    System.out.println("No admin information found for the given username.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void save() {
        if (!validateInput()) return;

        String sql = """
                    UPDATE [Admin]
                    SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?
                    WHERE admin_id = (
                        SELECT admin_id FROM AdminCredentials WHERE username = ?
                    )
                    """;

        try (Connection connect = Database.connectdb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            // Set parameters
            prepare.setString(1, firstNameAdmin.getText());
            prepare.setString(2, lastNameAdmin.getText());
            prepare.setString(3, adminEmail.getText());
            prepare.setString(4, adminPhone.getText());
            prepare.setString(5, this.loginUsername);

            int rowsAffected = prepare.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Save Successful");
            } else {
                showAlert(Alert.AlertType.ERROR, "No admin found to update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to save data. Please try again.");
        }
    }


    private boolean validateInput() {
        if (firstNameAdmin.getText().isEmpty() || lastNameAdmin.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields");
            return false;
        }
        return true;
    }


    public void User() {
        switchPage(contentAdmin);
        controlUser();
    }


    @FXML
    private TableView<BookingUser> userTableView;

    @FXML
    private TableColumn<BookingUser, Integer> bookingIdColumn;

    @FXML
    private TableColumn<BookingUser, String> firstNameColumn;

    @FXML
    private TableColumn<BookingUser, String> lastNameColumn;


    @FXML
    private TableColumn<BookingUser, String> startTimeColumn;

    @FXML
    private TableColumn<BookingUser, String> endTimeColumn;


    @FXML
    private TableColumn<BookingUser, String> phoneNumberUsers;


    private BookingUserService bookingUserService = new BookingUserService();



    @FXML
    public void controlUser() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        phoneNumberUsers.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        userTableView.setItems(bookingUserService.getBookingUserData());
    }



    public void Analytics() {
        barChart.getData().clear();
        initialize();
        switchPage(analyticsBar);
    }

    @FXML
    private BarChart<String, Number> barChart;



    @FXML
    private Label totalEarning;

    @FXML
    private Label totalOrders;

    @FXML
    private Label totalUsers;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private StatisticsService statisticsService = new StatisticsService();

    @FXML
    public void initialize() {
        xAxis.setLabel("Category");
        yAxis.setLabel("Value");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Statistics");

        for (Statistics stats : statisticsService.getStatisticsData()) {
            series.getData().add(new XYChart.Data<>("Total Order", statisticsService.countTotalBookings()));
            series.getData().add(new XYChart.Data<>("Total Price", statisticsService.SumOfPrice()));
            series.getData().add(new XYChart.Data<>("Total User",  statisticsService.TotalUser()));
        }

        barChart.getData().add(series);

        initializeTotalOrder();
        initializeEarning();
        initializeTotalUser();
    }


    @FXML
    public void initializeTotalOrder() {
        int total = statisticsService.countTotalBookings();
        totalOrders.setText(String.valueOf(total));
    }
    @FXML
    public void initializeEarning() {
        float total = statisticsService.SumOfPrice();
        totalEarning.setText(String.valueOf(total));
    }

    @FXML
    public void initializeTotalUser() {
        int total = statisticsService.TotalUser();
        totalUsers.setText(String.valueOf(total));
    }
    public void Parkinglot() {
        switchPage(parkingLotOverviewPage);
        parkingOverView();
    }


    @FXML
    private TableColumn<Parking, String> adminIdColumn; //comment

    @FXML
    private TableColumn<Parking, String> parkingIdColumn;

    @FXML
    private TableColumn<Parking, String> parkingLocationColumn;

    @FXML
    private TableColumn<Parking, String> parkingNameColumn;

    @FXML
    private TableColumn<Parking, String> parkingStatusColumn;


    @FXML
    private TableView<Parking> tableView;

    private ParkingService parkingService = new ParkingService();

    @FXML
    public void parkingOverView() {
        parkingIdColumn.setCellValueFactory(new PropertyValueFactory<>("parkingId"));
        parkingNameColumn.setCellValueFactory(new PropertyValueFactory<>("parkingName"));
        parkingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        parkingLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("adminId"));

        tableView.setItems(parkingService.getBookingUserData());
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/frontend/LoginAdmin.fxml"));
                Parent root = loader.load();
                loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.setScene(new Scene(root));
                loginStage.show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}