package com.example.parkingbookingsystems.phone;

public class PhoneUtils {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10}");
    }
}