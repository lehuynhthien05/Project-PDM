package com.example.parkingbookingsystems.Controller;

public class DateEndSession {
    private static String selectedDate;

    public static void setSelectedDate(String date) {
        selectedDate = date;
    }

    public static String getSelectedDate() {
        return selectedDate;
    }
}
