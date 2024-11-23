package com.example.parkingbookingsystems;

import com.example.parkingbookingsystems.Controller.BookingUser;
import com.example.parkingbookingsystems.Controller.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingService {
    public ObservableList<Parking> getBookingUserData() {
        ObservableList<Parking> parkingList = FXCollections.observableArrayList();
        String query = "SELECT p.parkingArea_id, p.name, b.status, p.locations, p.admin_id " +
                "FROM Booking b " +
                "JOIN ParkingArea p ON p.parkingArea_id = b.parkingArea_id";

        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String parkingId = resultSet.getString("parkingArea_id");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                String location = resultSet.getString("locations");
                String adminId = resultSet.getString("admin_id");

                Parking parking = new Parking(parkingId, name, status, location, adminId);
                parkingList.add(parking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parkingList;
    }
}
