package com.example.parkingbookingsystems;

public class Statistics {
    private int totalOrder;
    private double totalPrice;
    private int totalUser;

    public Statistics(int totalOrder, double totalPrice, int totalUser) {
        this.totalOrder = totalOrder;
        this.totalPrice = totalPrice;
        this.totalUser = totalUser;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalUser() {
        return totalUser;
    }
}
