package com.example.parkingbookingsystems.controller;

public class ParkingSession {
    private static String parkingAreaId;

    public static void setParkingAreaId(String parkingAreaId) {
        ParkingSession.parkingAreaId = parkingAreaId;
    }

    public static String extractParkingAreaId(String slotName) {
        // Implement logic to extract parking area ID from slot name
        // This is a dummy implementation, replace it with actual logic
        return slotName.substring(0, 2);
    }

    public static String getParkingAreaId() {
        return parkingAreaId;
    }
}
