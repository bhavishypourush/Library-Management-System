
        package com.library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Librarian dashboard for the Online Library Management System.
 *
 * Provides book management, member management, transaction history,
 * notifications, and an inventory report.
 */
public class LibrarianFrame extends JFrame {

    // Table models store the rows displayed in the dashboard.
    private final DefaultTableModel bookModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Author", "ISBN", "Genre", "Available"}, 0
    );

    private final DefaultTableModel memberModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Membership ID"}, 0
    );

    private final JTable bookTable = new JTable(bookModel);
    private final JTable memberTable = new JTable(memberModel);

    public LibrarianFrame(String name) {
        setTitle("Library Management System - Librarian");
        setSize(1120, 720);
        setMinimumSize(new Dimension(950, 620));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);
        root.add(topBar(name), BorderLayout.NORTH);

        // Each tab provides a separate library management function.
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(AppTheme.TEXT);

        tabs.addTab("  Books  ", bookPanel());
        tabs.addTab("  Members  ", memberPanel());
        tabs.addTab("  Transactions  ", transactionPanel());
        tabs.addTab("  Notifications  ", notificationPanel());
        tabs.addTab("  Inventory Report  ", reportPanel());

        root.add(tabs, BorderLayout.CENTER);
        add(root);

        // Populate the main tables when the dashboard opens.
        loadBooks();
        loadMembers();

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

        JLabel subtitle = new JLabel(
                "Librarian Dashboard  •  Manage your library with ease"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(190, 203, 225));

        text.add(title);
        text.add(Box.createVerticalStrut(3));
        text.add(subtitle);

        JLabel user = new JLabel("●  " + name);
        user.setFont(new Font("Segoe UI", Font.BOLD, 13));
        user.setForeground(Color.WHITE);

        bar.add(text, BorderLayout.WEST);
        bar.add(user, BorderLayout.EAST);

        return bar;
    }

    /**
     * Creates the book management tab and its action buttons.
     */
    private JPanel bookPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Book Management",
                        "Add, edit, remove and view books in the library catalog."
                ),
                BorderLayout.NORTH
        );

        JButton add = AppTheme.button("+  Add Book", AppTheme.BLUE);
        JButton edit = AppTheme.secondaryButton("Edit Selected");
        JButton delete = AppTheme.secondaryButton("Delete Selected");
        JButton refresh = AppTheme.secondaryButton("↻  Refresh");

        delete.setForeground(AppTheme.RED);
        AppTheme.styleTable(bookTable);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(
                AppTheme.toolbar(add, edit, delete, refresh),
                BorderLayout.NORTH
        );
        body.add(
                AppTheme.card(new JScrollPane(bookTable)),
                BorderLayout.CENTER
        );

        panel.add(body, BorderLayout.CENTER);

        add.addActionListener(event -> addBook());
        edit.addActionListener(event -> editBook());
        delete.addActionListener(event -> deleteBook());
        refresh.addActionListener(event -> loadBooks());

        return panel;
    }

    /**
     * Creates the member management tab.
     */
    private JPanel memberPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Member Management",
                        "Maintain member accounts and membership information."
                ),
                BorderLayout.NORTH
        );

        JButton add = AppTheme.button("+  Add Member", AppTheme.BLUE);
        JButton edit = AppTheme.secondaryButton("Edit Selected");
        JButton delete = AppTheme.secondaryButton("Delete Selected");
        JButton refresh = AppTheme.secondaryButton("↻  Refresh");

        delete.setForeground(AppTheme.RED);
        AppTheme.styleTable(memberTable);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(
                AppTheme.toolbar(add, edit, delete, refresh),
                BorderLayout.NORTH
        );
        body.add(
                AppTheme.card(new JScrollPane(memberTable)),
                BorderLayout.CENTER
        );

        panel.add(body, BorderLayout.CENTER);

        add.addActionListener(event -> addMember());
        edit.addActionListener(event -> editMember());
        delete.addActionListener(event -> deleteMember());
        refresh.addActionListener(event -> loadMembers());

        return panel;
    }

    /**
     * Creates the transaction history tab.
     */
    private JPanel transactionPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Transactions",
                        "Track every borrowing and return transaction."
                ),
                BorderLayout.NORTH
        );

        JTextArea area = new JTextArea();
        area.setEditable(false);
        AppTheme.styleTextArea(area);

        JButton refresh = AppTheme.button(
                "↻  Refresh Transactions", AppTheme.BLUE
        );

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(
                AppTheme.card(new JScrollPane(area)),
                BorderLayout.CENTER
        );
        body.add(AppTheme.toolbar(refresh), BorderLayout.SOUTH);

        panel.add(body, BorderLayout.CENTER);

        // Load transaction history from the database.
        Runnable load = () -> {
            area.setText("");

            String sql = """
                    SELECT t.id, b.title, u.name,
                           t.borrow_date, t.return_date
                    FROM transactions t
                    JOIN books b ON t.book_id = b.id
                    JOIN users u ON t.member_id = u.id
                    ORDER BY t.id DESC
                    """;

            try (Connection connection = DB.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                area.append("TRANSACTION HISTORY\n\n");

                while (resultSet.next()) {
                    area.append(String.format(
                            "#%d    %-28s  %-20s  Borrow: %s  Return: %s%n",
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("name"),
                            resultSet.getDate("borrow_date"),
                            resultSet.getDate("return_date")
                    ));
                }

            } catch (SQLException ex) {
                showError(ex);
            }
        };

        refresh.addActionListener(event -> load.run());
        load.run();

        return panel;
    }

    /**
     * Creates the notification form used by librarians.
     */
    private JPanel notificationPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Notifications",
                        "Send reminders and updates directly to members."
                ),
                BorderLayout.NORTH
        );

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextField memberId = new JTextField();
        JTextField message = new JTextField();

        AppTheme.styleField(memberId);
        AppTheme.styleField(message);
        message.setPreferredSize(new Dimension(430, 38));

        JButton send = AppTheme.button(
                "Send Notification", AppTheme.BLUE
        );

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 7, 7, 7);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        g.gridy = 0;
        form.add(label("Member ID"), g);

        g.gridx = 1;
        form.add(memberId, g);

        g.gridx = 0;
        g.gridy = 1;
        form.add(label("Message"), g);

        g.gridx = 1;
        form.add(message, g);

        g.gridx = 2;
        form.add(send, g);

        panel.add(AppTheme.card(form), BorderLayout.CENTER);

        send.addActionListener(event -> {
            String idText = memberId.getText().trim();
            String notificationMessage = message.getText().trim();

            if (idText.isEmpty() || notificationMessage.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter member ID and message.",
                        "Missing Details",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Validate the ID before sending it to the database.
            final int id;

            try {
                id = Integer.parseInt(idText);

                if (id <= 0) {
                    throw new NumberFormatException("ID must be positive.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid positive member ID.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String sql = """
                    INSERT INTO notifications (member_id, message)
                    VALUES (?, ?)
                    """;

            try (Connection connection = DB.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, id);
                statement.setString(2, notificationMessage);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Notification sent successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                memberId.setText("");
                message.setText("");

            } catch (SQLException ex) {
                showError(ex);
            }
        });

        return panel;
    }

    /**
     * Creates an inventory report using separate, safely closed
     * database resources for each count query.
     */
    private JPanel reportPanel() {
        JPanel panel = AppTheme.page();

        panel.add(
                AppTheme.header(
                        "Inventory Report",
                        "Quick overview of books and borrowing activity."
                ),
                BorderLayout.NORTH
        );

        JTextArea area = new JTextArea();
        area.setEditable(false);
        AppTheme.styleTextArea(area);

        JButton refresh = AppTheme.button(
                "Generate Report", AppTheme.BLUE
        );

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(
                AppTheme.card(new JScrollPane(area)),
                BorderLayout.CENTER
        );
        body.add(AppTheme.toolbar(refresh), BorderLayout.SOUTH);

        panel.add(body, BorderLayout.CENTER);

        Runnable generateReport = () -> {
            try (Connection connection = DB.getConnection()) {

                // Close each ResultSet and Statement before the next query.
                int totalBooks = queryCount(
                        connection, "SELECT COUNT(*) FROM books"
                );

                int availableBooks = queryCount(
                        connection,
                        "SELECT COUNT(*) FROM books WHERE available = TRUE"
                );

                int totalTransactions = queryCount(
                        connection, "SELECT COUNT(*) FROM transactions"
                );

                area.setText(
                        "LIBRARY INVENTORY REPORT\n\n"
                                + "Total books       : " + totalBooks + "\n"
                                + "Available books   : " + availableBooks + "\n"
                                + "Borrowed books    : "
                                + (totalBooks - availableBooks) + "\n"
                                + "Total transactions: " + totalTransactions
                                + "\n\nGenerated from the live library database."
                );

            } catch (SQLException ex) {
                area.setText("Unable to generate report: " + ex.getMessage());
            }
        };

        refresh.addActionListener(event -> generateReport.run());
        generateReport.run();

        return panel;
    }

    /**
     * Executes a COUNT query and safely closes its JDBC resources.
     */
    private int queryCount(Connection connection, String sql)
            throws SQLException {

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return 0;
        }
    }

    /**
     * Creates consistently styled labels for forms.
     */
    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(AppTheme.TEXT);
        return label;
    }

    /**
     * Reloads the book table from the database.
     */
    private void loadBooks() {
        bookModel.setRowCount(0);

        String sql = """
                SELECT id, title, author, isbn, genre, available
                FROM books
                ORDER BY id
                """;

        try (Connection connection = DB.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                bookModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("isbn"),
                        resultSet.getString("genre"),
                        resultSet.getBoolean("available")
                });
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Reloads member accounts from the database.
     */
    private void loadMembers() {
        memberModel.setRowCount(0);

        String sql = """
                SELECT id, name, email, membership_id
                FROM users
                WHERE role = 'MEMBER'
                """;

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                memberModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("membership_id")
                });
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Adds a book using the information entered in the form.
     */
    private void addBook() {
        JTextField title = new JTextField();
        JTextField author = new JTextField();
        JTextField isbn = new JTextField();
        JTextField genre = new JTextField();

        JTextField[] fields = {title, author, isbn, genre};
        String[] labels = {"Title", "Author", "ISBN", "Genre"};

        if (!form("Add Book", labels, fields)) {
            return;
        }

        String sql = """
                INSERT INTO books (title, author, isbn, genre)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, title.getText().trim());
            statement.setString(2, author.getText().trim());
            statement.setString(3, isbn.getText().trim());
            statement.setString(4, genre.getText().trim());

            statement.executeUpdate();
            loadBooks();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Updates the selected book's details.
     */
    private void editBook() {
        int row = bookTable.getSelectedRow();

        if (row < 0) {
            showSelectionWarning("Select a book first.");
            return;
        }

        JTextField title = new JTextField(
                (String) bookModel.getValueAt(row, 1)
        );
        JTextField author = new JTextField(
                (String) bookModel.getValueAt(row, 2)
        );
        JTextField isbn = new JTextField(
                (String) bookModel.getValueAt(row, 3)
        );
        JTextField genre = new JTextField(
                (String) bookModel.getValueAt(row, 4)
        );

        if (!form(
                "Edit Book",
                new String[]{"Title", "Author", "ISBN", "Genre"},
                new JTextField[]{title, author, isbn, genre}
        )) {
            return;
        }

        String sql = """
                UPDATE books
                SET title = ?, author = ?, isbn = ?, genre = ?
                WHERE id = ?
                """;

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, title.getText().trim());
            statement.setString(2, author.getText().trim());
            statement.setString(3, isbn.getText().trim());
            statement.setString(4, genre.getText().trim());
            statement.setInt(5, (int) bookModel.getValueAt(row, 0));

            statement.executeUpdate();
            loadBooks();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Deletes the selected book after confirmation.
     */
    private void deleteBook() {
        int row = bookTable.getSelectedRow();

        if (row < 0) {
            showSelectionWarning("Select a book first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete the selected book?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, (int) bookModel.getValueAt(row, 0));
            statement.executeUpdate();

            loadBooks();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Creates a member account.
     *
     * NOTE: The fixed demo password is only a classroom-project shortcut.
     * A real application must use securely hashed passwords and a
     * proper account setup or password-reset process.
     */
    private void addMember() {
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField membershipId = new JTextField();

        if (!form(
                "Add Member",
                new String[]{"Name", "Email", "Membership ID"},
                new JTextField[]{name, email, membershipId}
        )) {
            return;
        }

        String memberCode = membershipId.getText().trim();

        if (name.getText().trim().isEmpty()
                || email.getText().trim().isEmpty()
                || memberCode.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Complete all member fields.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = """
                INSERT INTO users
                    (username, password, role, name, email, membership_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, memberCode);
            statement.setString(2, "1234"); // Demo only; not production-safe.
            statement.setString(3, "MEMBER");
            statement.setString(4, name.getText().trim());
            statement.setString(5, email.getText().trim());
            statement.setString(6, memberCode);

            statement.executeUpdate();
            loadMembers();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Updates the selected member's name and email address.
     */
    private void editMember() {
        int row = memberTable.getSelectedRow();

        if (row < 0) {
            showSelectionWarning("Select a member first.");
            return;
        }

        JTextField name = new JTextField(
                (String) memberModel.getValueAt(row, 1)
        );
        JTextField email = new JTextField(
                (String) memberModel.getValueAt(row, 2)
        );

        if (!form(
                "Edit Member",
                new String[]{"Name", "Email"},
                new JTextField[]{name, email}
        )) {
            return;
        }

        if (name.getText().trim().isEmpty()
                || email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Name and email cannot be empty.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name.getText().trim());
            statement.setString(2, email.getText().trim());
            statement.setInt(3, (int) memberModel.getValueAt(row, 0));

            statement.executeUpdate();
            loadMembers();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Deletes the selected member after confirmation.
     */
    private void deleteMember() {
        int row = memberTable.getSelectedRow();

        if (row < 0) {
            showSelectionWarning("Select a member first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete the selected member?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, (int) memberModel.getValueAt(row, 0));
            statement.executeUpdate();

            loadMembers();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    /**
     * Displays a reusable form with labels and text fields.
     *
     * @return true if the user confirms the form
     */
    private boolean form(
            String title,
            String[] labels,
            JTextField[] fields
    ) {
        JPanel panel = new JPanel(
                new GridLayout(labels.length, 2, 12, 12)
        );
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        for (int i = 0; i < labels.length; i++) {
            panel.add(label(labels[i]));

            AppTheme.styleField(fields[i]);
            panel.add(fields[i]);
        }

        return JOptionPane.showConfirmDialog(
                this,
                panel,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        ) == JOptionPane.OK_OPTION;
    }

    /**
     * Displays a warning when an operation requires a table selection.
     */
    private void showSelectionWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "No Selection",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Displays a database or application error.
     */
    private void showError(Exception exception) {
        JOptionPane.showMessageDialog(
                this,
                exception.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
