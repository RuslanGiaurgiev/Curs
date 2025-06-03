package com.dealer.client.ui;

import com.dealer.client.ApiClient;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class MainFrame extends JFrame {
    private final ApiClient api;

    public MainFrame() {
        super("Dealer Client");
        // Basic-Auth
        api = new ApiClient("http://localhost:8080", "admin", "admin123");
        createMenu();
        initUI();
        loadWindowPrefs();
    }

    private void createMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu app = new JMenu("Application");
        JMenuItem about = new JMenuItem("About Author");
        about.addActionListener(e -> new AboutDialog(this).setVisible(true));
        app.add(about);
        mb.add(app);
        setJMenuBar(mb);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Brands", new BrandPanel(api));
        tabs.addTab("Car Models", new CarModelPanel(api));
        add(tabs, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void loadWindowPrefs() {
        Preferences p = Preferences.userNodeForPackage(MainFrame.class);
        int x = p.getInt("x", 100), y = p.getInt("y", 100),
                w = p.getInt("w", 800), h = p.getInt("h", 600);
        setBounds(x, y, w, h);
    }

    @Override
    public void dispose() {
        Preferences p = Preferences.userNodeForPackage(MainFrame.class);
        Rectangle b = getBounds();
        p.putInt("x", b.x);
        p.putInt("y", b.y);
        p.putInt("w", b.width);
        p.putInt("h", b.height);
        super.dispose();
    }
}
