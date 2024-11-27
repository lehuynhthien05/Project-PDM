package com.example.parkingbookingsystems.Controller;

public class BookingSession {
    private static int bookingId;

    public static void setBookingId(int bookingId) {
        BookingSession.bookingId = bookingId;
    }

    public static int getBookingId() {
        return bookingId;
    }
}