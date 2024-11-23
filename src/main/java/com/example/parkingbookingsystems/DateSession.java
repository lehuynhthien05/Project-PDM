package com.example.parkingbookingsystems;

import java.time.LocalDate;

public class DateSession {
    private static String selectedDate;

    public static void setSelectedDate(String date) {
        selectedDate = date;
    }

    public static String getSelectedDate() {
        return selectedDate;
    }
}
