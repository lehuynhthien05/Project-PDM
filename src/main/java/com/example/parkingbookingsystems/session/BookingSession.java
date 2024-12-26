package com.example.parkingbookingsystems.session;

public class BookingSession {
    private static int bookingId;

    public static int getBookingId() {
        return bookingId;
    }

    public static void setBookingId(int id) {
        bookingId = id;
    }
}