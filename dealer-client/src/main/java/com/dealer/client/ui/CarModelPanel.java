package com.dealer.client.ui;

import com.dealer.client.ApiClient;
import com.dealer.model.CarModel;
import com.dealer.model.Brand;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class CarModelPanel extends JPanel {
    private final ApiClient api;
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel();
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final JTextField filterField = new JTextField(8);

    public CarModelPanel(ApiClient api) {
        this.api = api;
        setLayout(new BorderLayout());
        buildToolbar();
        initTable();
        loadData();
    }

    private void buildToolbar() {
        JPanel top = new JPanel();
        JButton add = new JButton("Add"), edit = new JButton("Edit"), del = new JButton("Delete");
        add.addActionListener(e -> onAdd());
        edit.addActionListener(e -> onEdit());
        del.addActionListener(e -> onDelete());
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());
        JButton filterBtn = new JButton("Price <= ");
        filterBtn.addActionListener(e -> {
            try {
                double v = Double.parseDouble(filterField.getText());
                sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, v, 2));
            } catch (NumberFormatException ex) {
                sorter.setRowFilter(null);
            }
        });
        top.add(add); top.add(edit); top.add(del);
        top.add(new JLabel("Max price:")); top.add(filterField); top.add(filterBtn);
        top.add(refresh);
        add(top, BorderLayout.NORTH);
    }

    private void initTable() {
        model.setColumnIdentifiers(new Object[]{"ID","Name","Price","Brand"});
        table.setModel(model);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            List<CarModel> list = api.listModels();
            for (CarModel m : list) {
                model.addRow(new Object[]{
                        m.getId(), m.getName(), m.getPrice(), m.getBrand().getName()
                });
            }
        } catch (Exception ex) {
            showErr("Error loading models", ex);
        }
    }

    private void onAdd() {
        CarModelDialog dlg = new CarModelDialog(null, api);
        if (dlg.showDialog()) {
            try {
                api.createModel(dlg.getModel());
                loadData();
            } catch (Exception ex) {
                showErr("Error creating model", ex);
            }
        }
    }

    private void onEdit() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        int mr = sorter.convertRowIndexToModel(r);
        Long id = (Long) model.getValueAt(mr, 0);
        String name = (String) model.getValueAt(mr, 1);
        Double price = (Double) model.getValueAt(mr, 2);
        String brandName = (String) model.getValueAt(mr, 3);

        Brand brand;
        try {
            List<Brand> brands = api.listBrands();
            brand = brands.stream()
                    .filter(b -> b.getName().equals(brandName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception ex) {
            showErr("Error fetching brands", ex);
            return;
        }

        CarModel existing = new CarModel(id, name, price, brand);
        CarModelDialog dlg = new CarModelDialog(existing, api);
        if (dlg.showDialog()) {
            try {
                api.updateModel(dlg.getModel());
                loadData();
            } catch (Exception ex) {
                showErr("Error updating model", ex);
            }
        }
    }

    private void onDelete() {
        int r = table.getSelectedRow();
        if (r < 0 ||
                JOptionPane.showConfirmDialog(this, "Delete?", "Confirm",
                        JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        Long id = (Long) model.getValueAt(sorter.convertRowIndexToModel(r), 0);
        try {
            api.deleteModel(id);
            loadData();
        } catch (Exception ex) {
            showErr("Error deleting model", ex);
        }
    }

    private void showErr(String title, Exception ex) {
        JOptionPane.showMessageDialog(this,
                title + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
