package com.example.parkingbookingsystems.Controller;
import com.example.parkingbookingsystems.SlotFileUtil.SlotFileUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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


    @FXML
    private Button booking_returnbtn;

    @FXML
    private Text selectedSlotName;

    @FXML
    private Button backToBooking;

    @FXML
    private HBox home;

    @FXML
    private HBox Booking;

    @FXML
    public void close(ActionEvent event) {
        System.exit(0);
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    @FXML
    private Text usernameDisplay;

    public void setUsernameDisplay(String username) {
        usernameDisplay.setText(username);
    }

    private String firstName;
    private String lastName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsernameDisplay(String lastName, String firstName) {
        usernameDisplay.setText(lastName + " " + firstName);
    }


    public void UserProfile() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdjustUser.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Home() {
        try {
            Stage contentAreaStage = (Stage) home.getScene().getWindow();
            contentAreaStage.hide();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/ContentAreaAndUser.fxml"));
            Parent root = loader.load();


            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Booking Interface
    public void Booking() {
        try {
            // Hide the ContentAreaAndUserProfile screen
            Stage contentAreaStage = (Stage) Booking.getScene().getWindow();
            contentAreaStage.hide();

            // Open the booking stage
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPickslot.fxml"));

            Parent root = loader.load();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private DatePicker bookingDate;

    @FXML
    private void handleDateSelection() {
        LocalDate selectedDate = bookingDate.getValue();
        if (selectedDate != null) {
            DateSession.setSelectedDate(selectedDate.toString());
        } else {
            System.out.println("No date selected");
        }
    }

    @FXML
    private void onCheckDateButtonClick() {
        handleDateSelection();
    }

    @FXML
    private DatePicker endTime;

    @FXML
    private void handleDateSelectionExit() {
        LocalDate selectedDate = endTime
                .getValue();
        if (selectedDate != null) {
            DateEndSession.setSelectedDate(selectedDate.toString());
        } else {
            System.out.println("No date selected");
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
                // Select the slot
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



    public void setUsernameDisplay() {
        usernameDisplay.setText(lastName + " " + firstName);
    }


    @FXML
    private Text priceOrder;

    @FXML
    private Text totalOrder;

    @FXML
    private Text totalSlot_Payment;

    private int totalPrice;


    public void setSelectedSlots(Set<String> selectedSlots) {
        this.selectedSlots = selectedSlots;
        if (totalSlot_Payment != null) {
            totalSlot_Payment.setText(String.valueOf(selectedSlots.size()));
        } else {
            System.out.println();
        }
    }


    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        priceOrder.setText("$ 100");

        //Calculate total money
        int total = selectedSlots.size() * 100;
        totalOrder.setText(String.valueOf(total));
    }

    //Update SelectedSlotName
    public void updateSelectedSlotName() {
        selectedSlotName.setText(String.join(", ", selectedSlots));
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

    private void executeSQLCommandBooking() {
        String sql = "INSERT INTO [Booking] (user_id, parkingArea_id, startTime, endTime, totalCost, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connectdb();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, UserSession.getCurrentUserId());
            pstmt.setString(2, ParkingSession.getParkingAreaId());
            pstmt.setString(3, DateSession.getSelectedDate());
            pstmt.setString(4, DateEndSession.getSelectedDate());
            pstmt.setFloat(5, PriceSession.getTotalCost());
            pstmt.setString(6, getStatus());

            pstmt.executeUpdate(); // Execute the statement first

            // Retrieve the generated keys
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    BookingSession.setBookingId(bookingId);
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeSQLCommandPayment() {
        String sql = "INSERT INTO [Payment] (booking_id, amount, paymentDate, paymentMethod, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connectdb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, BookingSession.getBookingId());
            pstmt.setFloat(2, PriceSession.getTotalCost());
            pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setString(4, getSelectedPaymentMethod());
            pstmt.setString(5, "Completed");

            pstmt.executeUpdate();
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

                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPayment.fxml"));
                Parent root = loader.load();

                // Get the controller of the payment interface
                UserInterface paymentController = loader.getController();
                paymentController.setSelectedSlots(selectedSlots);
                paymentController.setTotalPrice(100); // Set total price to 100
                paymentController.updateSelectedSlotName();

                // Update the style of selected slots
                updateSelectedSlotsStyle();

                // Write selected slots to file
                writeSelectedSlotsToFile();

                // Clear the selected slots and update the UI
                selectedSlots.clear();
                refreshSlotInfo();

                // Close the current stage
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Booking_ReturnBtn() {
        try {
            Stage contentAreaStage = (Stage) booking_returnbtn.getScene().getWindow();
            contentAreaStage.hide();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/ContentAreaAndUser.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //Payment Interface
    public void Payment() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPayment.fxml"));
            Parent root = loader.load();


            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                // Open the booking stage

                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/paymentCheck .fxml"));
                Parent root = loader.load();

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
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
    private VBox pickSlot;


    public void ContinueBooking() {
        updateSelectedSlotsStyle();
        try {
            // Load selected slots from file
            selectedSlots = SlotFileUtil.loadSelectedSlotsFromFile();

            // Open the booking stage
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPickslot.fxml"));
            Parent root = loader.load();

            // Get the controller of the booking interface
            UserInterface bookingController = loader.getController();
            bookingController.setSelectedSlots(selectedSlots);
            bookingController.updateSelectedSlotName();
            bookingController.updateSelectedSlotsStyle();

            // Reset the count of booked slots
            selectedSlots.clear();
            bookingController.updateSelectedSlotName();
            bookingController.updateSelectedSlotsStyle();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
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

    private void deleteLatestBooking() {
        String sql = "DELETE FROM Booking WHERE booking_id = (SELECT MAX(booking_id) FROM Booking)";

        try (Connection conn = Database.connectdb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ReturnBooking() {
        try {
            Stage contentAreaStage = (Stage) backToBooking.getScene().getWindow();
            contentAreaStage.hide();

            deleteLatestBooking();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/contentAndPickslot.fxml"));
            Parent root = loader.load();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/LoginUser.fxml"));
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
