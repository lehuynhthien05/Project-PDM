package com.example.parkingbookingsystems;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static Connection connect;

    public static Connection connectdb() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("Huynhthien123");
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName("ParkingBookingSystem");

        ds.setTrustServerCertificate(true);
        try {
            connect = ds.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }
}