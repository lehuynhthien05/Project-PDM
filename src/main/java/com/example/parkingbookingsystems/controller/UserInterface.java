    package com.example.parkingbookingsystems.controller;
    import com.example.parkingbookingsystems.Database;
    import com.example.parkingbookingsystems.email.EmailUtils;
    import com.example.parkingbookingsystems.phone.PhoneUtils;
    import com.example.parkingbookingsystems.security.PasswordUtils;
    import com.example.parkingbookingsystems.session.*;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;
    import javafx.stage.StageStyle;
    import javafx.stage.Window;

    import java.io.BufferedWriter;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.sql.*;
    import java.time.LocalDate;
    import java.util.*;

    public class UserInterface {
        private String loginUsername;


    //    @FXML
    //    private Button booking_returnbtn;

        @FXML
        private Text selectedSlotName;



        @FXML
        private Button backToBooking;

        @FXML
        private AnchorPane paymentCheck;

        @FXML
        private HBox home;

        @FXML
        private HBox Booking;

        @FXML
        private VBox profileArea;

        @FXML
        private VBox contentHome;

        @FXML
        private VBox pickSlot;



        public void switchPage(VBox targetPage) {
            // Hide all pages
            profileArea.setVisible(false);
            contentHome.setVisible(false);
            pickSlot.setVisible(false);
            paymentAndBill.setVisible(false);
            // Show the target page
            targetPage.setVisible(true);
        }



        @FXML
        public void close(ActionEvent event) {
            System.exit(0);
        }

        public void setLoginUsername(String loginUsername) {
            this.loginUsername = loginUsername;
        }

        @FXML
        private Button nameUser;



        private String firstName;
        private String lastName;
        private Stage homeStage;



        public void UserProfile() {
            switchPage(profileArea);
        }

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
                        SELECT uc.password
                        FROM UserCredentials uc
                        JOIN [User] u ON uc.user_id = u.user_id
                        WHERE uc.username = ?
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
                        showAlert(Alert.AlertType.ERROR, "User not found.");
                        return;
                    }
                }

                // Hash the new password
                String newHashedPassword = PasswordUtils.hashPassword(newPassword.getText());

                // Update the password in the UserCredentials table
                String updateSql = """
                                    UPDATE UserCredentials
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

        public void loadUserInfo() {
            String sql = """
            SELECT u.firstName, u.lastName, u.email, u.phoneNumber, uc.username
            FROM [User] u
            JOIN UserCredentials uc ON u.user_id = uc.user_id
            WHERE uc.username = ?
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
                        userName.setText(username);
                        nameUser.setText(firstName + " " + lastName);
                        firstNameUser.setText(firstName);
                        lastNameUser.setText(lastName);
                        userEmail.setText(email);
                        userPhone.setText(phoneNumber);
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
            UPDATE [User]
            SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?
            WHERE user_id = (
                SELECT user_id FROM UserCredentials WHERE username = ?
            )
            """;

            try (Connection connect = Database.connectdb();
                 PreparedStatement prepare = connect.prepareStatement(sql)) {

                prepare.setString(1, firstNameUser.getText());
                prepare.setString(2, lastNameUser.getText());
                prepare.setString(3, userEmail.getText());
                prepare.setString(4, userPhone.getText());
                prepare.setString(5, this.loginUsername);

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

        public void Home() {
            switchPage(contentHome);
        }

        //Booking Interface
        public void Booking() {
            switchPage(pickSlot);
        }

        private Set<String> selectedSlots = new HashSet<>();

        @FXML
        private Text totalSlot;

        @FXML
        private Text totalAmount;

        // Method to refresh slotName and totalSlots
        private void refreshSlotInfo() {
            // Update selected slot name
            selectedSlotName.setText(String.join(", ", selectedSlots));

            // Update total slot count
            totalSlot.setText(String.valueOf(selectedSlots.size()));

            // Calculate total money
            int total = selectedSlots.size() * 100;
            totalAmount.setText(String.valueOf(total));

            handleTotalCost(total);
        }


        @FXML
        private DatePicker entryDate;

        @FXML
        private void handleDateSelection() {
            LocalDate selectedDate = entryDate.getValue();
            if (selectedDate != null) {
                DateSession.setSelectedDate(selectedDate.toString());
            } else {
                System.out.println();
            }
        }

        @FXML
        private void onCheckDateButtonClick() {
            handleDateSelection();
        }

        @FXML
        private DatePicker existDate;

        @FXML
        private void handleDateSelectionExit() {
            LocalDate selectedDate = existDate
                    .getValue();
            if (selectedDate != null) {
                DateEndSession.setSelectedDate(selectedDate.toString());
            } else {
                System.out.println();
            }
        }

        @FXML
        private void onCheckDateButtonClickEndTime() {
            handleDateSelectionExit();
        }

        // Select slot
        @FXML
        private void selectParkingSlot(MouseEvent event) {
            Node source = (Node) event.getSource();
            String slotName = source.getId();

            if (selectedSlots.contains(slotName)) {
                // Deselect the slot
                selectedSlots.remove(slotName);
                source.getStyleClass().remove("rectangle-selected");
                source.getStyleClass().add("rectangle-available");
                totalPrice -= 100; // Subtract 100 from total cost
            } else {
                if (selectedSlots.size() < 1) {
                    // Select the slot1
                    selectedSlots.add(slotName);
                    source.getStyleClass().remove("rectangle-available");
                    source.getStyleClass().add("rectangle-selected");
                    totalPrice += 100; // Add 100 to total cost

                    // Store the parking area ID
                    ParkingSession.setParkingAreaId(slotName);
                } else {
                    // Show alert if more than 2 slots are selected
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Selection Limit");
                    alert.setHeaderText(null);
                    alert.setContentText("You can only select 1 slot.");
                    alert.showAndWait();
                }
            }
            refreshSlotInfo();
        }




        @FXML
        private Text priceOrder;

        @FXML
        private Text totalOrderPrice;

        @FXML
        private Text totalSlot_Payment;
        @FXML
        private Text slotName;

        private int totalPrice;


        public void setSelectedSlots() {
            totalSlot_Payment.setText("1");
        }


        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
            priceOrder.setText("$ 100");

            //Calculate total money
            int total = selectedSlots.size() * 100;
            totalOrderPrice.setText(String.valueOf(total));
        }

        //Update SelectedSlotName
        public void updateSelectedSlotName() {
            selectedSlotName.setText(String.join(", ", selectedSlots));
        }

        //Update SelectedSlotName
        public void updateSlotNameInPayment() {
            slotName.setText(String.join(", ", selectedSlots));
        }
        private void writeSelectedSlotsToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("selectedSlots.txt", true))) {
                for (String slot : selectedSlots) {
                    writer.write(slot);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        public int getTotalCost() {
            if (totalAmount == null || totalAmount.getText().isEmpty()) {
                return 0; // Default value if totalAmount is not set or empty
            } else {
                String text = totalAmount.getText();
                if (text.matches("-?\\d+(\\.\\d+)?")) { // Check if the text is a valid number
                    return Integer.parseInt(text);
                } else {
                    return 0;
                }
            }
        }

        @FXML
        private void handleTotalCost(int totalAmount) {
            if (totalAmount != 0) {
                PriceSession.setTotalCost(getTotalCost());
            } else {
                System.out.println();
            }
        }

        public String getStatus() {
            return "Booked";
        }

        private Connection connect;
        private PreparedStatement prepare;
        private ResultSet result;


        private int executeSQLCommandBooking() {
            String sqlGetMaxId = "SELECT MAX(booking_id) FROM Booking";
            String sqlInsertBooking = "INSERT INTO Booking (booking_id, user_id, parkingArea_id, startTime, endTime, totalCost, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            int newBookingId = -1;

            try (Connection conn = Database.connectdb()) {
                // Get the current max booking_id
                try (PreparedStatement pstmtGetMaxId = conn.prepareStatement(sqlGetMaxId);
                     ResultSet rs = pstmtGetMaxId.executeQuery()) {
                    newBookingId = 1;
                    if (rs.next()) {
                        newBookingId = rs.getInt(1) + 1;
                    }

                    // Insert the new booking
                    try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertBooking, Statement.RETURN_GENERATED_KEYS)) {
                        pstmtInsert.setInt(1, newBookingId);
                        pstmtInsert.setInt(2, UserSession.getCurrentUserId());
                        pstmtInsert.setString(3, ParkingSession.getParkingAreaId());
                        pstmtInsert.setString(4, DateSession.getSelectedDate());
                        pstmtInsert.setString(5, DateEndSession.getSelectedDate());
                        pstmtInsert.setFloat(6, PriceSession.getTotalCost());
                        pstmtInsert.setString(7, getStatus());

                        BookingSession.setBookingId(newBookingId); // Set the booking ID in the session


                        int affectedRows = pstmtInsert.executeUpdate();
                        if (affectedRows == 0) {
                            throw new SQLException("Creating booking failed, no rows affected.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return newBookingId;
        }

        private void executeSQLCommandPayment() {
            String sqlGetMaxId = "SELECT MAX(payment_id) FROM Payment";
            String sqlInsertPayment = "INSERT INTO Payment (payment_id, booking_id, amount, paymentDate, paymentMethod, status) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = Database.connectdb()) {
                // Get the current max payment_id
                try (PreparedStatement pstmtGetMaxId = conn.prepareStatement(sqlGetMaxId);
                     ResultSet rs = pstmtGetMaxId.executeQuery()) {
                    int newPaymentId = 1;
                    if (rs.next()) {
                        newPaymentId = rs.getInt(1) + 1;
                    }

                    // Ensure the booking_id exists in the Booking table
                    int bookingId = BookingSession.getBookingId();
                    String sqlCheckBooking = "SELECT COUNT(*) FROM Booking WHERE booking_id = ?";
                    try (PreparedStatement pstmtCheckBooking = conn.prepareStatement(sqlCheckBooking)) {
                        pstmtCheckBooking.setInt(1, bookingId);
                        try (ResultSet rsCheck = pstmtCheckBooking.executeQuery()) {
                            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                                // Insert the new payment
                                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertPayment)) {
                                    pstmtInsert.setInt(1, newPaymentId);
                                    pstmtInsert.setInt(2, bookingId);
                                    pstmtInsert.setFloat(3, PriceSession.getTotalCost());
                                    pstmtInsert.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                                    pstmtInsert.setString(5, getSelectedPaymentMethod());
                                    pstmtInsert.setString(6, "Completed");

                                    pstmtInsert.executeUpdate();
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

            // Booked button action
        public void BookedBtn(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Booking");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to book this slot?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Execute the SQL command
                    executeSQLCommandBooking();
                    Payment();

                    updateSlotNameInPayment();

                    setSelectedSlots();

                    setTotalPrice(100); // Set total price to 100


                    // Write selected slots to file
                    writeSelectedSlotsToFile();


                    refreshSlotInfo();




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        @FXML
        private VBox paymentAndBill;

        //Payment Interface
        public void Payment() {
            switchPage(paymentAndBill);
        }

        @FXML
        private ComboBox<String> paymentMethod;

        private boolean isPaymentMethodInitialized = false;

        @FXML
        public void initialize() {
            if (!isPaymentMethodInitialized) {
                if (paymentMethod != null) {
                    paymentMethod.getItems().addAll("Credit", "Visa", "ATM");
                    paymentMethod.setValue("Credit"); // Set default value
                    isPaymentMethodInitialized = true; // Mark as initialized
                } else {
                    System.out.println();
                }
            }
        }

        private String getSelectedPaymentMethod() {
            return paymentMethod.getValue();
        }

        private int generateRandomBookingId() {
            Random random = new Random();
            return random.nextInt(100); // Generate a random number between 0 and 999999
        }

        public void PaymentCheck() throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Payment");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to proceed with the payment?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Ensure all necessary values are set before executing the SQL command
                if (UserSession.getCurrentUserId() != -1 && !ParkingSession.getParkingAreaId().equals("-1") && DateSession.getSelectedDate() != null && DateEndSession.getSelectedDate() != null && PriceSession.getTotalCost() > 0) {
                    executeSQLCommandPayment();
                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Payment Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Your payment has been processed successfully.");
                    successAlert.showAndWait();

                    // Update the style of selected slots
                    updateSelectedSlotsStyle();

                    paymentCheck.setVisible(true);
                } else {
                    // Show error message if any value is missing
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Payment Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("An error occurred while processing your payment. Please try again.");
                    errorAlert.showAndWait();
                }
            }
        }


        @FXML
        private Button continueBooking;


        public void ContinueBooking() {
            try {
                // Hide the Payment screen
                paymentCheck.setVisible(false);

                // Switch to the pickSlot page
                switchPage(pickSlot);

                // Clear the selected slots and update the UI
                selectedSlots.clear();

                // Reset the count of booked slots
                selectedSlots.clear();

                // Clear the selected date
                existDate.setValue(null);
                DateEndSession.setSelectedDate(null);
                entryDate.setValue(null);
                DateSession.setSelectedDate(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateSelectedSlotsStyle() {
            for (String slotId : selectedSlots) {
                Node slot = pickSlot.lookup("#" + slotId);
                if (slot != null) {
                    slot.getStyleClass().remove("rectangle-available");
                    slot.getStyleClass().add("rectangle-booked");
                }
            }
        }

        private void updateDeselectedSlotsStyle() {
            for (String slotId : selectedSlots) {
                Node slot = pickSlot.lookup("#" + slotId);
                if (slot != null) {
                    slot.getStyleClass().remove("rectangle-booked");
                    slot.getStyleClass().add("rectangle-available");
                }
            }
        }
        private void deleteLatestBooking() {
            String sql = "DELETE FROM Booking WHERE booking_id = (SELECT MAX(booking_id) FROM Booking)";

            try (Connection conn = Database.connectdb();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @FXML
        private Button cancelPayment;

        public void ReturnBooking() {
            try {
                // Clear the selected slots and reset their styles
                deleteLatestBooking();
                updateDeselectedSlotsStyle();

                switchPage(pickSlot);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/frontend/loginUser.fxml"));
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
