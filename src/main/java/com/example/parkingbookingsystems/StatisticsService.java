package com.example.parkingbookingsystems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StatisticsService {

    public ObservableList<Statistics> getStatisticsData() {
        // Replace with actual data fetching logic
        ObservableList<Statistics> statisticsList = FXCollections.observableArrayList();
        statisticsList.add(new Statistics(100, 5000.0, 50));
        return statisticsList;
    }

    public int countTotalBookings() {
        int totalBookings = 0;
        String query = "SELECT COUNT(*) FROM Booking";

        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalBookings = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBookings;
    }
    public float SumOfPrice() {
        int totalPrice = 0;
        String query = "SELECT SUM(totalCost) FROM Booking";

        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalPrice = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPrice;
    }
    public int TotalUser() {
        int total = 0;
        String query = "SELECT COUNT(user_id) FROM [User]";

        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}