package com.library;

import java.sql.*;

public class DB {
    private static final String URL =
            "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root123"; // CHANGE THIS

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
