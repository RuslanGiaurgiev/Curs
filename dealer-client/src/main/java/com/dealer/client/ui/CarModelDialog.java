package com.dealer.client.ui;

import com.dealer.client.ApiClient;
import com.dealer.model.Brand;
import com.dealer.model.CarModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CarModelDialog extends JDialog {
    private final JTextField nameField = new JTextField(15);
    private final JTextField priceField = new JTextField(8);
    private final JComboBox<Brand> brandBox = new JComboBox<>();
    private CarModel model;
    private boolean ok;

    public CarModelDialog(CarModel existing, ApiClient api) {
        super((Frame)null, "Car Model", true);
        try {
            List<Brand> brands = api.listBrands();
            brands.forEach(brandBox::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Cannot load brands:\n"+ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (existing != null) {
            model = new CarModel(
                    existing.getId(),
                    existing.getName(),
                    existing.getPrice(),
                    existing.getBrand()
            );
            nameField.setText(model.getName());
            priceField.setText(Double.toString(model.getPrice()));
            brandBox.setSelectedItem(model.getBrand());
        } else {
            model = new CarModel();
        }
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(3,2,5,5));
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Price:")); form.add(priceField);
        form.add(new JLabel("Brand:")); form.add(brandBox);
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
        try {
            model.setName(nameField.getText().trim());
            model.setPrice(Double.parseDouble(priceField.getText().trim()));
            model.setBrand((Brand)brandBox.getSelectedItem());
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean showDialog() {
        setVisible(true);
        return ok;
    }

    public CarModel getModel() {
        return model;
    }
}
