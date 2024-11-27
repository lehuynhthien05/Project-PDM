package com.example.parkingbookingsystems.controller;

public class UserSession {
    public static int currentUserId = 0;

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
}
