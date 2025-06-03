package com.dealer.client.ui;

import com.dealer.model.Brand;

import javax.swing.*;
import java.awt.*;

public class BrandDialog extends JDialog {
    private final JTextField nameField = new JTextField(20);
    private Brand brand;
    private boolean ok;

    public BrandDialog(Brand existing) {
        super((Frame)null, "Brand", true);
        this.brand = existing != null
                ? new Brand(existing.getId(), existing.getName(), null)
                : new Brand();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(1,2,5,5));
        form.add(new JLabel("Name:"));
        nameField.setText(brand.getName());
        form.add(nameField);
        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton okBtn = new JButton("OK"), cancelBtn = new JButton("Cancel");
        okBtn.addActionListener(e -> { ok=true; apply(); });
        cancelBtn.addActionListener(e -> dispose());
        buttons.add(okBtn); buttons.add(cancelBtn);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void apply() {
        brand.setName(nameField.getText().trim());
        dispose();
    }

    public boolean showDialog() {
        setVisible(true);
        return ok;
    }

    public Brand getBrand() {
        return brand;
    }
}
