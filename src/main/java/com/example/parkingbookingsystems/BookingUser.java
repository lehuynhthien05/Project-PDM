package com.example.parkingbookingsystems;

public class BookingUser {
    private int bookingId;
    private String firstName;
    private String lastName;
    private String startTime;
    private String endTime;
    private String phoneNumber;

    public BookingUser(int bookingId, String firstName, String lastName, String startTime, String endTime, String phoneNumber) {
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.phoneNumber = phoneNumber;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
