package com.example.parkingbookingsystems;

public class PriceSession {
    private static float totalCost;

    public static void setTotalCost(float cost) {
        totalCost = cost;
    }

    public static float getTotalCost() {
        return totalCost;
    }
}