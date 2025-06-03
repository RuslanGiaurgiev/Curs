package com.dealer.client.ui;

import com.dealer.client.ApiClient;
import com.dealer.model.Brand;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class BrandPanel extends JPanel {
    private final ApiClient api;
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel();
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final JTextField filterField = new JTextField(15);

    public BrandPanel(ApiClient api) {
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
        JButton filterBtn = new JButton("Filter");
        filterBtn.addActionListener(e -> {
            String txt = filterField.getText();
            if (txt.isBlank()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txt, 1));
        });
        top.add(add); top.add(edit); top.add(del);
        top.add(new JLabel("Search name:")); top.add(filterField); top.add(filterBtn);
        top.add(refresh);
        add(top, BorderLayout.NORTH);
    }

    private void initTable() {
        model.setColumnIdentifiers(new Object[]{"ID", "Name"});
        table.setModel(model);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            List<Brand> list = api.listBrands();
            for (Brand b : list) {
                model.addRow(new Object[]{b.getId(), b.getName()});
            }
        } catch (Exception ex) {
            showErr("Error loading brands", ex);
        }
    }

    private void onAdd() {
        BrandDialog dlg = new BrandDialog(null);
        if (dlg.showDialog()) {
            try {
                api.createBrand(dlg.getBrand());
                loadData();
            } catch (Exception ex) { showErr("Error creating brand", ex); }
        }
    }

    private void onEdit() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        Long id = (Long) model.getValueAt(sorter.convertRowIndexToModel(r), 0);
        String name = (String) model.getValueAt(sorter.convertRowIndexToModel(r), 1);
        BrandDialog dlg = new BrandDialog(new com.dealer.model.Brand(id, name, null));
        if (dlg.showDialog()) {
            try {
                api.updateBrand(dlg.getBrand());
                loadData();
            } catch (Exception ex) { showErr("Error updating brand", ex); }
        }
    }

    private void onDelete() {
        int r = table.getSelectedRow();
        if (r<0 || JOptionPane.showConfirmDialog(this,"Delete?","Confirm",
                JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        Long id = (Long) model.getValueAt(sorter.convertRowIndexToModel(r), 0);
        try {
            api.deleteBrand(id);
            loadData();
        } catch (Exception ex) { showErr("Error deleting brand", ex); }
    }

    private void showErr(String title, Exception ex) {
        JOptionPane.showMessageDialog(this,
                title + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
