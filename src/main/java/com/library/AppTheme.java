package com.library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class AppTheme {
    public static final Color NAVY = new Color(8, 31, 69);
    public static final Color BLUE = new Color(8, 31, 69);
    public static final Color LIGHT_BLUE = new Color(50, 54, 68);
    public static final Color BG = new Color(255, 255, 255);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(34, 40, 49);
    public static final Color MUTED = new Color(105, 113, 126);
    public static final Color BORDER = new Color(3, 25, 62);
    public static final Color GREEN = new Color(35, 145, 92);
    public static final Color RED = new Color(190, 65, 65);

    private AppTheme() {}

    public static void install() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Button.background", NAVY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
    }

    public static JPanel page() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(18, 22, 22, 22));
        return p;
    }

    public static JPanel header(String title, String subtitle) {
        JPanel h = new JPanel(new BorderLayout(8, 4));
        h.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        t.setForeground(TEXT);
        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        s.setForeground(MUTED);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(t); text.add(Box.createVerticalStrut(3)); text.add(s);
        h.add(text, BorderLayout.WEST);
        return h;
    }

    public static JPanel card(Component component) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(12, 12, 12, 12)));
        card.add(component);
        return card;
    }

    public static JButton button(String text, Color background) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(background);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setForeground(NAVY);
        b.setBackground(new Color(225, 233, 245));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(145, 164, 194)),
                BorderFactory.createEmptyBorder(9, 15, 9, 15)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(LIGHT_BLUE);
        table.setSelectionForeground(TEXT);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        JTableHeader head = table.getTableHeader();
        head.setReorderingAllowed(false);
        head.setPreferredSize(new Dimension(0, 38));
        head.setBackground(NAVY);
        head.setForeground(Color.WHITE);
        head.setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        if (table.getColumnCount() > 0) table.getColumnModel().getColumn(0).setCellRenderer(center);
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setForeground(TEXT);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(14, 14, 14, 14));
    }

    public static void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(250, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
    }

    public static JPanel toolbar(Component... components) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        for (Component c : components) bar.add(c);
        return bar;
    }
}
