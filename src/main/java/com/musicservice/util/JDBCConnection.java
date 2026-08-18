package com.musicservice.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JDBCConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/music_service";
    private static final String USER = "postgres";
    private static final String PASSWORD = "alltoowell";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}