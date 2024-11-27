package com.example.parkingbookingsystems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingUserService {

    public ObservableList<BookingUser> getBookingUserData() {
        ObservableList<BookingUser> bookingUserList = FXCollections.observableArrayList();
        String query = "SELECT b.booking_id, u.firstName, u.lastName, b.startTime, b.endTime, u.phoneNumber " +
                "FROM Booking b " +
                "JOIN [User] u ON b.user_id = u.user_id";

        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String startTime = resultSet.getString("startTime");
                String endTime = resultSet.getString("endTime");
                String phoneNumber = resultSet.getString("phoneNumber");

                BookingUser bookingUser = new BookingUser(bookingId, firstName, lastName, startTime, endTime, phoneNumber);
                bookingUserList.add(bookingUser);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingUserList;
    }
}