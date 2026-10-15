package com.library;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppTheme.install();
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
