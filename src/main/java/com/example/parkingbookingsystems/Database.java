package com.example.parkingbookingsystems;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private Connection connect;

    public Connection connectdb() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("123");
        ds.setServerName("huynhthienle");
        ds.setPortNumber(1433);
        ds.setDatabaseName("ParkingArea");

        ds.setURL("jdbc:sqlserver://huynhthienle:1433;databaseName=test;encrypt=true;trustServerCertificate=true");

        try {
            connect = ds.getConnection();
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }
}