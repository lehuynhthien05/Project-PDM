package com.example.parkingbookingsystems.Controller;

public class ParkingSession {
    private static String parkingAreaId;

    public static void setParkingAreaId(String parkingAreaId) {
        ParkingSession.parkingAreaId = parkingAreaId;
    }

    public static String extractParkingAreaId(String slotName) {
        return slotName.substring(0, 2);
    }

    public static String getParkingAreaId() {
        return parkingAreaId;
    }
}
