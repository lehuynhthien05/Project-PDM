package com.example.parkingbookingsystems.service;

import com.example.parkingbookingsystems.Database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean checkEmailExists(String email) {
        String query = "SELECT * FROM Usertable WHERE email = ?";
        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendResetLink(String email) {
        String token = generateToken();
        saveToken(email, token);

        String resetLink = "http://localhost:8080/reset_password?token=" + token;

    }

    private String generateToken() {
        // Generate a random token
        return java.util.UUID.randomUUID().toString();
    }

    private void saveToken(String email, String token) {
        String query = "UPDATE Usertable SET reset_token = ? WHERE email = ?";
        try (Connection connection = Database.connectdb();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, token);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}