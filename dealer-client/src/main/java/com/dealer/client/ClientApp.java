package com.dealer.client.ui;

import javax.swing.SwingUtilities;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
