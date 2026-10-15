
        package com.library;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Member dashboard for the Online Library Management System.
 *
 * Provides book search, borrowing, returns, history,
 * profile management and notifications.
 */
public class MemberFrame extends JFrame {

    private final int memberId;
    private final JTextArea output = new JTextArea();
    private JButton searchButton;

    public MemberFrame(int memberId, String name) {
        this.memberId = memberId;

        setTitle("Library Management System - Member");
        setSize(1040, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);
        root.add(topBar(name), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(AppTheme.TEXT);

        tabs.addTab("  Book Search  ", searchPanel());
        tabs.addTab("  Borrow / Return  ", transactionPanel());
        tabs.addTab("  Borrowing History  ", historyPanel());
        tabs.addTab("  Profile  ", profilePanel());
        tabs.addTab("  Notifications  ", notificationPanel());

        root.add(tabs, BorderLayout.CENTER);
        add(root);
        setVisible(true);
    }

    /**
     * Creates the dashboard header.
     */
    private JPanel topBar(String name) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppTheme.NAVY);
        bar.setBorder(BorderFactory.createEmptyBorder(15, 22, 15, 22));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Library Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel(
                "Member Dashboard  •  Search, borrow and manage your library activity"
        );
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(190, 203, 225));

        text.add(title);
        text.add(Box.createVerticalStrut(3));
        text.add(sub);

        JLabel user = new JLabel("●  " + name);
        user.setFont(new Font("Segoe UI", Font.BOLD, 13));
        user.setForeground(Color.WHITE);

        bar.add(text, BorderLayout.WEST);
        bar.add(user, BorderLayout.EAST);

        return bar;
    }

    /**
     * Builds the search interface and connects the button
     * to the database search operation.
     */
    private JPanel searchPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Book Search",
                        "Find books by title, author or genre."
                ),
                BorderLayout.NORTH
        );

        JTextField queryField = new JTextField();
        AppTheme.styleField(queryField);
        queryField.setPreferredSize(new Dimension(430, 40));

        searchButton = AppTheme.button("Search", AppTheme.BLUE);
        JPanel toolbar = AppTheme.toolbar(queryField, searchButton);

        output.setEditable(false);
        AppTheme.styleTextArea(output);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);

        body.add(toolbar, BorderLayout.NORTH);
        body.add(
                AppTheme.card(new JScrollPane(output)),
                BorderLayout.CENTER
        );

        panel.add(body, BorderLayout.CENTER);

        // Search when the button is clicked.
        searchButton.addActionListener(
                event -> search(queryField.getText())
        );

        // Pressing Enter in the search field also starts a search.
        queryField.addActionListener(
                event -> search(queryField.getText())
        );

        // Load every book when the search page opens.
        search("");

        return panel;
    }

    /**
     * Searches book titles, authors and genres.
     *
     * SwingWorker performs database work in a background thread,
     * keeping the Swing user interface responsive.
     */
    private void search(String query) {
        if (searchButton != null) {
            searchButton.setEnabled(false);
        }

        output.setText("Searching books...");

        String sql = """
                SELECT id, title, author, isbn, genre, available
                FROM books
                WHERE title LIKE ?
                   OR author LIKE ?
                   OR genre LIKE ?
                ORDER BY title ASC
                """;

        String searchText = "%" + query.trim() + "%";

        SwingWorker<List<String>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<String> doInBackground() throws SQLException {
                        List<String> results = new ArrayList<>();

                        try (Connection connection = DB.getConnection();
                             PreparedStatement statement =
                                     connection.prepareStatement(sql)) {

                            statement.setString(1, searchText);
                            statement.setString(2, searchText);
                            statement.setString(3, searchText);

                            try (ResultSet resultSet =
                                         statement.executeQuery()) {

                                while (resultSet.next()) {
                                    String book = "ID: "
                                            + resultSet.getInt("id")
                                            + "   |   "
                                            + resultSet.getString("title")
                                            + "   |   "
                                            + resultSet.getString("author")
                                            + "   |   "
                                            + resultSet.getString("genre")
                                            + "   |   Available: "
                                            + resultSet.getBoolean("available")
                                            + "\n";

                                    results.add(book);
                                }
                            }
                        }

                        return results;
                    }

                    /**
                     * Runs on the Swing Event Dispatch Thread after
                     * the background search has completed.
                     */
                    @Override
                    protected void done() {
                        try {
                            List<String> results = get();
                            output.setText("");

                            if (results.isEmpty()) {
                                output.setText("No matching books found.");
                            } else {
                                for (String book : results) {
                                    output.append(book);
                                }
                            }

                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            output.setText("Search was interrupted.");

                        }  catch (ExecutionException ex) {
                        Throwable cause = ex.getCause();

                        // Keep technical details in the console for debugging.
                        if (cause != null) {
                            cause.printStackTrace();
                        } else {
                            ex.printStackTrace();
                        }

                        // Show a friendly message to the user.
                        output.setText(
                                "The database is temporarily unavailable.\n"
                                        + "Please try again in a moment."
                        );

                    } finally {
                        if (searchButton != null) {
                            searchButton.setEnabled(true);
                        }
                    }
                    }
                };

        worker.execute();
    }

    /**
     * Displays borrowing and return controls.
     */
    private JPanel transactionPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Borrow / Return",
                        "Enter a Book ID to borrow or return a book."
                ),
                BorderLayout.NORTH
        );

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JTextField bookId = new JTextField();
        AppTheme.styleField(bookId);

        JButton borrow = AppTheme.button(
                "Borrow Book", AppTheme.BLUE
        );
        JButton returnButton = AppTheme.secondaryButton("Return Book");

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        g.gridy = 0;
        form.add(label("Book ID"), g);

        g.gridx = 1;
        form.add(bookId, g);

        g.gridx = 1;
        g.gridy = 1;
        form.add(AppTheme.toolbar(borrow, returnButton), g);

        panel.add(AppTheme.card(form), BorderLayout.CENTER);

        borrow.addActionListener(
                event -> borrow(bookId.getText())
        );

        returnButton.addActionListener(
                event -> returnBook(bookId.getText())
        );

        return panel;
    }

    /**
     * Borrows a book and records the transaction.
     * Database changes are committed together.
     */
    private void borrow(String idText) {
        try {
            int bookId = Integer.parseInt(idText.trim());

            try (Connection connection = DB.getConnection()) {
                connection.setAutoCommit(false);

                try {
                    boolean available;

                    try (PreparedStatement check =
                                 connection.prepareStatement(
                                         "SELECT available FROM books WHERE id = ?"
                                 )) {

                        check.setInt(1, bookId);

                        try (ResultSet resultSet = check.executeQuery()) {
                            available = resultSet.next()
                                    && resultSet.getBoolean("available");
                        }
                    }

                    if (!available) {
                        connection.rollback();

                        JOptionPane.showMessageDialog(
                                this,
                                "Book is not available.",
                                "Borrow",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    try (PreparedStatement transaction =
                                 connection.prepareStatement(
                                         "INSERT INTO transactions "
                                                 + "(book_id, member_id, borrow_date) "
                                                 + "VALUES (?, ?, CURDATE())"
                                 );
                         PreparedStatement updateBook =
                                 connection.prepareStatement(
                                         "UPDATE books SET available = false "
                                                 + "WHERE id = ? AND available = true"
                                 )) {

                        updateBook.setInt(1, bookId);

                        if (updateBook.executeUpdate() != 1) {
                            connection.rollback();

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Book is no longer available.",
                                    "Borrow",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }

                        transaction.setInt(1, bookId);
                        transaction.setInt(2, memberId);
                        transaction.executeUpdate();
                    }

                    connection.commit();

                    JOptionPane.showMessageDialog(
                            this,
                            "Book borrowed successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (SQLException ex) {
                    connection.rollback();
                    throw ex;
                } finally {
                    connection.setAutoCommit(true);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Book ID.",
                    "Invalid ID",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "The database is temporarily unavailable. Please try again.",
                "Database Unavailable",
                JOptionPane.ERROR_MESSAGE
        );
    }
    }

    /**
     * Returns a book borrowed by the current member.
     */
    private void returnBook(String idText) {
        try {
            int bookId = Integer.parseInt(idText.trim());

            try (Connection connection = DB.getConnection()) {
                connection.setAutoCommit(false);

                try {
                    int changed;

                    try (PreparedStatement transaction =
                                 connection.prepareStatement(
                                         "UPDATE transactions "
                                                 + "SET return_date = CURDATE() "
                                                 + "WHERE book_id = ? "
                                                 + "AND member_id = ? "
                                                 + "AND return_date IS NULL"
                                 )) {

                        transaction.setInt(1, bookId);
                        transaction.setInt(2, memberId);
                        changed = transaction.executeUpdate();
                    }

                    if (changed == 0) {
                        connection.rollback();

                        JOptionPane.showMessageDialog(
                                this,
                                "No active borrowing found.",
                                "Return",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    try (PreparedStatement updateBook =
                                 connection.prepareStatement(
                                         "UPDATE books SET available = true "
                                                 + "WHERE id = ?"
                                 )) {

                        updateBook.setInt(1, bookId);
                        updateBook.executeUpdate();
                    }

                    connection.commit();

                    JOptionPane.showMessageDialog(
                            this,
                            "Book returned successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (SQLException ex) {
                    connection.rollback();
                    throw ex;
                } finally {
                    connection.setAutoCommit(true);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Book ID.",
                    "Invalid ID",
                    JOptionPane.WARNING_MESSAGE
            );

        }  catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "The database is temporarily unavailable. Please try again.",
                "Database Unavailable",
                JOptionPane.ERROR_MESSAGE
        );
    }
    }

    /**
     * Shows borrowing and return history for the logged-in member.
     */
    private JPanel historyPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Borrowing History",
                        "View all books you have borrowed and returned."
                ),
                BorderLayout.NORTH
        );

        JTextArea history = new JTextArea();
        history.setEditable(false);
        AppTheme.styleTextArea(history);

        JButton refresh = AppTheme.button(
                "↻  Refresh History", AppTheme.BLUE
        );

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(
                AppTheme.card(new JScrollPane(history)),
                BorderLayout.CENTER
        );
        body.add(AppTheme.toolbar(refresh), BorderLayout.SOUTH);

        panel.add(body, BorderLayout.CENTER);

        Runnable loadHistory = () -> {
            history.setText("");

            String sql = """
                    SELECT b.title, t.borrow_date, t.return_date
                    FROM transactions t
                    JOIN books b ON t.book_id = b.id
                    WHERE t.member_id = ?
                    ORDER BY t.id DESC
                    """;

            try (Connection connection = DB.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, memberId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        history.append(
                                resultSet.getString("title")
                                        + "\nBorrowed: "
                                        + resultSet.getDate("borrow_date")
                                        + "    Returned: "
                                        + resultSet.getDate("return_date")
                                        + "\n\n"
                        );
                    }
                }

            } catch (SQLException ex) {
            ex.printStackTrace();

            history.setText(
                    "Borrowing history is temporarily unavailable.\n"
                            + "Please try again in a moment."
            );
        }
        };

        refresh.addActionListener(event -> loadHistory.run());
        loadHistory.run();

        return panel;
    }

    /**
     * Loads and updates the current member's profile.
     */
    private JPanel profilePanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Profile",
                        "Update your name and email address."
                ),
                BorderLayout.NORTH
        );

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JTextField name = new JTextField();
        JTextField email = new JTextField();

        AppTheme.styleField(name);
        AppTheme.styleField(email);

        JButton save = AppTheme.button(
                "Save Changes", AppTheme.BLUE
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        g.gridy = 0;
        form.add(label("Name"), g);

        g.gridx = 1;
        form.add(name, g);

        g.gridx = 0;
        g.gridy = 1;
        form.add(label("Email"), g);

        g.gridx = 1;
        form.add(email, g);

        g.gridx = 1;
        g.gridy = 2;
        form.add(save, g);

        String selectSql = "SELECT name, email FROM users WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(selectSql)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    name.setText(resultSet.getString("name"));
                    email.setText(resultSet.getString("email"));
                }
            }

        }  catch (SQLException ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Your profile is temporarily unavailable. Please try again.",
                "Database Unavailable",
                JOptionPane.ERROR_MESSAGE
        );
    }

        save.addActionListener(event -> {
            String updateSql =
                    "UPDATE users SET name = ?, email = ? WHERE id = ?";

            try (Connection connection = DB.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(updateSql)) {

                statement.setString(1, name.getText().trim());
                statement.setString(2, email.getText().trim());
                statement.setInt(3, memberId);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Profile updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to update profile: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(AppTheme.card(form), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Displays notifications belonging to the current member.
     */
    private JPanel notificationPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Notifications",
                        "View reminders and updates from your librarian."
                ),
                BorderLayout.NORTH
        );

        JTextArea notifications = new JTextArea();
        notifications.setEditable(false);
        AppTheme.styleTextArea(notifications);

        String sql = """
                SELECT message, created_at
                FROM notifications
                WHERE member_id = ?
                ORDER BY id DESC
                """;

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    notifications.append(
                            resultSet.getTimestamp("created_at")
                                    + "\n"
                                    + resultSet.getString("message")
                                    + "\n\n"
                    );
                }
            }

        }  catch (SQLException ex) {
        ex.printStackTrace();

        notifications.setText(
                "Notifications are temporarily unavailable.\n"
                        + "Please try again in a moment."
        );
    }

        panel.add(
                AppTheme.card(new JScrollPane(notifications)),
                BorderLayout.CENTER
        );

        return panel;
    }

    /**
     * Creates consistently styled form labels.
     */
    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AppTheme.TEXT);
        return label;
    }
}
