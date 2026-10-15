package com.library;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DB {
    private static final String SERVER_URL =
            "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DATABASE_URL =
            "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";

    private static String password = normalizePassword(System.getenv("LIBRARY_DB_PASSWORD"));
    private static boolean initialized = false;

    private DB() { }

    public static Connection getConnection() throws SQLException {
        ensureInitialized();
        return DriverManager.getConnection(DATABASE_URL, USER, password);
    }

    private static synchronized void ensureInitialized() throws SQLException {
        if (initialized) return;

        boolean prompted = false;
        if (password == null) {
            password = askForPassword();
            prompted = true;
        }

        while (true) {
            if (password == null) {
                throw new SQLException("Database setup cancelled. Enter the MySQL password to continue.");
            }

            try (Connection connection = DriverManager.getConnection(SERVER_URL, USER, password)) {
                String schema = readSchema();
                try (Statement statement = connection.createStatement()) {
                    for (String sql : schema.split(";")) {
                        String command = sql.trim();
                        if (!command.isEmpty()) statement.execute(command);
                    }
                }
                initialized = true;
                return;
            } catch (SQLException e) {
                if (!prompted) {
                    password = askForPassword();
                    prompted = true;
                    continue;
                }
                password = null;
                throw new SQLException(
                        "Could not connect to MySQL or initialize the library database. "
                                + "Check that MySQL Server is running and the MySQL username/password are correct. "
                                + "Details: " + e.getMessage(), e);
            } catch (Exception e) {
                password = null;
                throw new SQLException("Could not load schema.sql: " + e.getMessage(), e);
            }
        }
    }

    private static String askForPassword() {
        String entered = JOptionPane.showInputDialog(
                null,
                "Enter the MySQL password for user '" + USER + "'.\n"
                        + "This is used locally and is not saved in the project.",
                "Library Database Setup",
                JOptionPane.QUESTION_MESSAGE);
        return normalizePassword(entered);
    }

    private static String normalizePassword(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private static String readSchema() throws Exception {
        try (InputStream input = DB.class.getResourceAsStream("/schema.sql")) {
            if (input == null) throw new IllegalStateException("schema.sql was not found in application resources.");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}
