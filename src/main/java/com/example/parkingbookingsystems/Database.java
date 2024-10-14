package com.example.parkingbookingsystems;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static Connection connect;

    public static Connection connectdb() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("123");
        ds.setServerName("huynhthienle");
        ds.setPortNumber(1433);
        ds.setDatabaseName("ParkingBookingSystem");

        ds.setURL("jdbc:sqlserver://huynhthienle:1433;databaseName=test;encrypt=true;trustServerCertificate=true");

        try {
            connect = ds.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }
}