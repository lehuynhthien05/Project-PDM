package com.example.parkingbookingsystems.entity;

public class Parking {
    private String parkingId;
    private String parkingName;
    private String status;
    private String location;
    private String adminId;

    public Parking(String parkingId, String parkingName, String status, String location, String adminId) {
        this.parkingId = parkingId;
        this.parkingName = parkingName;
        this.status = status;
        this.location = location;
        this.adminId = adminId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getAdminId() {
        return adminId;
    }
}
