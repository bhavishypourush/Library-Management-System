
        package com.library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class LoginFrame extends JFrame {

    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private JButton signInButton;

    public LoginFrame() {
        setTitle("Library Management System");
        setSize(760, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(AppTheme.BG);
        root.add(brandPanel());
        root.add(loginPanel());

        add(root);
        getRootPane().setDefaultButton(signInButton);
        setVisible(true);
    }

    private JPanel brandPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppTheme.NAVY);
        panel.setBorder(new EmptyBorder(40, 45, 40, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel icon = new JLabel("LIBRARY");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        icon.setForeground(new Color(185, 211, 255));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        panel.add(icon, gbc);

        JLabel title = new JLabel("Library Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(title, gbc);

        JLabel subtitle = new JLabel(
                "Manage books, members and borrowing from one simple workspace."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(205, 214, 230));
        gbc.gridy = 2;
        panel.add(subtitle, gbc);

        JLabel tagline = new JLabel("Secure • Simple • Organized");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tagline.setForeground(new Color(145, 177, 235));
        gbc.gridy = 3;
        gbc.insets = new Insets(28, 0, 0, 0);
        panel.add(tagline, gbc);

        return panel;
    }

    private JPanel loginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(AppTheme.BG);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(30, 34, 30, 34)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(AppTheme.TEXT);

        JLabel subtitle = new JLabel(
                "Sign in to continue to your dashboard"
        );
        subtitle.setForeground(AppTheme.MUTED);

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));

        card.add(label("Username"));
        AppTheme.styleField(username);
        card.add(username);
        card.add(Box.createVerticalStrut(16));

        card.add(label("Password"));
        AppTheme.styleField(password);
        card.add(password);
        card.add(Box.createVerticalStrut(24));

        signInButton = AppTheme.button("Sign In", AppTheme.BLUE);
        signInButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );
        signInButton.addActionListener(event -> login());
        card.add(signInButton);

        card.add(Box.createVerticalStrut(18));

        JLabel hint = new JLabel(
                "Demo: admin / admin123  •  member / member123"
        );
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(AppTheme.MUTED);
        card.add(hint);

        outer.add(card);
        return outer;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AppTheme.TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void login() {
        String enteredUsername = username.getText().trim();
        char[] enteredPassword = password.getPassword();

        if (enteredUsername.isEmpty() || enteredPassword.length == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );
            java.util.Arrays.fill(enteredPassword, '\0');
            return;
        }

        // Copy the password for the background task, then clear
        // the temporary array returned by the password field.
        String passwordValue = new String(enteredPassword);
        java.util.Arrays.fill(enteredPassword, '\0');

        signInButton.setEnabled(false);

        SwingWorker<LoginResult, Void> worker = new SwingWorker<>() {

            @Override
            protected LoginResult doInBackground() throws SQLException {
                String sql = """
                        SELECT id, role, name
                        FROM users
                        WHERE username = ? AND password = ?
                        """;

                try (
                        Connection connection = DB.getConnection();
                        PreparedStatement statement =
                                connection.prepareStatement(sql)
                ) {
                    statement.setString(1, enteredUsername);
                    statement.setString(2, passwordValue);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            return new LoginResult(
                                    resultSet.getInt("id"),
                                    resultSet.getString("role"),
                                    resultSet.getString("name")
                            );
                        }

                        return null;
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    LoginResult result = get();

                    if (result == null) {
                        JOptionPane.showMessageDialog(
                                LoginFrame.this,
                                "Invalid username or password.",
                                "Login failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                        password.setText("");
                        password.requestFocusInWindow();
                        return;
                    }

                    dispose();

                    if ("LIBRARIAN".equalsIgnoreCase(result.role)) {
                        new LibrarianFrame(result.name);
                    } else if ("MEMBER".equalsIgnoreCase(result.role)) {
                        new MemberFrame(result.id, result.name);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "This account has an unsupported role.",
                                "Login failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                        new LoginFrame();
                    }

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();

                    JOptionPane.showMessageDialog(
                            LoginFrame.this,
                            "Login was interrupted. Please try again.",
                            "Login error",
                            JOptionPane.ERROR_MESSAGE
                    );

                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();

                    String message = cause instanceof SQLException
                            ? "Unable to connect to the database. "
                            + "Please check the database configuration."
                            : "An unexpected error occurred during login.";

                    JOptionPane.showMessageDialog(
                            LoginFrame.this,
                            message,
                            "Connection error",
                            JOptionPane.ERROR_MESSAGE
                    );

                } finally {
                    signInButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    /**
     * Holds the authenticated user's basic details.
     */
    private static class LoginResult {
        private final int id;
        private final String role;
        private final String name;

        private LoginResult(int id, String role, String name) {
            this.id = id;
            this.role = role;
            this.name = name;
        }
    }
}
