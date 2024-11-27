package com.example.parkingbookingsystems;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class BookingUserController {

    @FXML
    private Button returnHome;

    @FXML
    private TableView<BookingUser> tableView;

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
    public void initialize() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        phoneNumberUsers.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        tableView.setItems(bookingUserService.getBookingUserData());
    }

    public void returnToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parkingbookingsystems/AdminProfile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
