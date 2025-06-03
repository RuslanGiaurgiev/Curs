package com.dealer.client.ui;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    public AboutDialog(Frame owner) {
        super(owner, "About Author", true);
        JTextArea info = new JTextArea(
                "Информационно-справочная система дилерского\n" +
                        "магазина автомобилей\n\n" +
                        "Автор: Иванов Иван\n" +
                        "Группа: ИУ5-12Б\n" +
                        "2025"
        );
        info.setEditable(false);
        info.setBackground(getBackground());
        add(info, BorderLayout.CENTER);
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dispose());
        JPanel p = new JPanel(); p.add(ok);
        add(p, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }
}
