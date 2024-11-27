package com.example.parkingbookingsystems.controller;

import com.example.parkingbookingsystems.entity.Parking;
import com.example.parkingbookingsystems.service.ParkingService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ParkingController {
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
    private Button returnHome;

    @FXML
    private TableView<Parking> tableView;

    private ParkingService parkingService = new ParkingService();

    @FXML
    public void initialize() {
        parkingIdColumn.setCellValueFactory(new PropertyValueFactory<>("parkingId"));
        parkingNameColumn.setCellValueFactory(new PropertyValueFactory<>("parkingName"));
        parkingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        parkingLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("adminId"));

        tableView.setItems(parkingService.getBookingUserData());
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

            Stage currentStage = (Stage) returnHome.getScene().getWindow();
            currentStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
