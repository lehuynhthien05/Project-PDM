package com.example.parkingbookingsystems;
import com.example.parkingbookingsystems.SlotFileUtil.SlotFileUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

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
        } else {
            if (selectedSlots.size() < 2) {
                // Select the slot
                selectedSlots.add(slotName);
                source.getStyleClass().remove("rectangle-available");
                source.getStyleClass().add("rectangle-selected");
            } else {
                // Show alert if more than 2 slots are selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Limit");
                alert.setHeaderText(null);
                alert.setContentText("You can only select up to 2 slots.");
                alert.showAndWait();
            }
        }

        refreshSlotInfo();
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

    // Booked button action
    public void BookedBtn(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Booking");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to book this slot?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
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

    public void PaymentCheck() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Payment");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to proceed with the payment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/PaymentCheck .fxml"));
                Parent root = loader.load();

                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(root));
                primaryStage.show();

            } catch (Exception e) {
                e.printStackTrace();
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

    public void ReturnBooking() {
        try {
            Stage contentAreaStage = (Stage) backToBooking.getScene().getWindow();
            contentAreaStage.hide();

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


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;




}
