package bukantugasakhir;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Stack;

public class MainApp extends JFrame {

    // --- Komponen GUI ---
    private JPanel contentPane;
    private JTextField txtNama, txtBeban, txtSet, txtRepetisi;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalVolume, lblTitle;


    private RoundedPanel panelInput, panelTotal;
    private RoundedButton btnSimpan, btnUpdate, btnHapus;
    private ThemeToggleButton btnToggleMode;
    private RoundedScrollPane scrollPane;
    private JLabel[] labels = new JLabel[4];

    private Stack<AngkatBeban> dataLatihan = new Stack<>(); //Struktur Data (Modul 7)
    private boolean isDarkMode = false;

    // --- PALET WARNA MONOKROM ---
    // Light Mode
    private Color lightBg = new Color(250, 250, 250);
    private Color lightPanel = Color.WHITE;
    private Color lightText = Color.BLACK;

    // Dark Mode
    private Color darkBg = new Color(18, 18, 18);
    private Color darkPanel = new Color(30, 30, 30);
    private Color darkText = Color.WHITE;

    // Font
    private Font fontHeader = new Font("Poppins", Font.BOLD, 26);
    private Font fontLabel = new Font("Poppins", Font.BOLD, 13);
    private Font fontInput = new Font("Poppins", Font.PLAIN, 14);

    public MainApp() {
        setTitle("Workout Tracker");
        setSize(700, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // --- 1. HEADER ---
        lblTitle = new JLabel("WORKOUT TRACKER");
        lblTitle.setFont(fontHeader);
        lblTitle.setBounds(35, 20, 300, 40);
        contentPane.add(lblTitle);

        btnToggleMode = new ThemeToggleButton();
        btnToggleMode.setBounds(600, 20, 50, 40);
        btnToggleMode.addActionListener(e -> toggleTheme());
        contentPane.add(btnToggleMode);

        // --- 2. PANEL INPUT ---
        panelInput = new RoundedPanel(25, lightPanel);
        panelInput.setLayout(null);
        panelInput.setBounds(30, 80, 625, 170);
        contentPane.add(panelInput);

        labels[0] = addLabel(panelInput, "Nama Gerakan", 20, 20);
        txtNama = addRoundedTextField(panelInput, 20, 45, 280);

        labels[1] = addLabel(panelInput, "Beban (kg)", 20, 90);
        txtBeban = addRoundedTextField(panelInput, 20, 115, 280);

        labels[2] = addLabel(panelInput, "Set", 320, 20);
        txtSet = addRoundedTextField(panelInput, 320, 45, 280);

        labels[3] = addLabel(panelInput, "Repetisi", 320, 90);
        txtRepetisi = addRoundedTextField(panelInput, 320, 115, 280);

        // --- 3. TOMBOL ---
        btnSimpan = new RoundedButton("SIMPAN", Color.BLACK, Color.WHITE);
        btnSimpan.setBounds(30, 270, 120, 45);
        btnSimpan.addActionListener(e -> simpanData());
        contentPane.add(btnSimpan);

        btnUpdate = new RoundedButton("PERBARUI", Color.GRAY, Color.WHITE);
        btnUpdate.setBounds(160, 270, 120, 45);
        btnUpdate.addActionListener(e -> updateData());
        contentPane.add(btnUpdate);

        btnHapus = new RoundedButton("HAPUS", new Color(80, 80, 80), Color.WHITE);
        btnHapus.setBounds(290, 270, 120, 45);
        btnHapus.addActionListener(e -> hapusData());
        contentPane.add(btnHapus);

        // --- 4. TABEL ---
        String[] columnNames = {"Gerakan", "Beban", "Set", "Rep", "Volume"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        table.setFont(fontInput);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihBarisUntukEdit();
            }
        });

        scrollPane = new RoundedScrollPane(table, 25, lightPanel);
        scrollPane.setBounds(30, 330, 625, 220);
        contentPane.add(scrollPane);

        // --- 5. FOOTER ---
        panelTotal = new RoundedPanel(20, Color.BLACK);
        panelTotal.setBounds(30, 570, 625, 60);
        panelTotal.setLayout(null);
        contentPane.add(panelTotal);

        lblTotalVolume = new JLabel("TOTAL VOLUME: 0 KG");
        lblTotalVolume.setFont(new Font("Poppins", Font.BOLD, 18));
        lblTotalVolume.setForeground(Color.WHITE);
        lblTotalVolume.setBounds(30, 0, 500, 60);
        panelTotal.add(lblTotalVolume);

        applyTheme(); // Set tema awal
    }

    // --- LOGIC METHODS ---
    private void simpanData() {
        if (!validasiInput()) return;
        AngkatBeban latihan = buatObjekDariInput();
        dataLatihan.push(latihan);
        tableModel.addRow(new Object[]{
                latihan.getNamaGerakan(), latihan.getBeban(),
                latihan.getSet(), latihan.getRepetisi(), latihan.hitungVolume()
        });
        selesaiAksi();
    }

    private void updateData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) { //Pengkondisian (Modul 2)
            JOptionPane.showMessageDialog(this, "Pilih baris dulu!"); return;
        }
        if (!validasiInput()) return;
        AngkatBeban latihanBaru = buatObjekDariInput();
        dataLatihan.set(selectedRow, latihanBaru);
        tableModel.setValueAt(latihanBaru.getNamaGerakan(), selectedRow, 0);
        tableModel.setValueAt(latihanBaru.getBeban(), selectedRow, 1);
        tableModel.setValueAt(latihanBaru.getSet(), selectedRow, 2);
        tableModel.setValueAt(latihanBaru.getRepetisi(), selectedRow, 3);
        tableModel.setValueAt(latihanBaru.hitungVolume(), selectedRow, 4);
        selesaiAksi();
        JOptionPane.showMessageDialog(this, "Data diperbarui!");
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            tableModel.removeRow(row);
            if (row < dataLatihan.size()) dataLatihan.remove(row);
            selesaiAksi();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data dulu.");
        }
    }

    private void pilihBarisUntukEdit() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtNama.setText(tableModel.getValueAt(row, 0).toString());
            txtBeban.setText(tableModel.getValueAt(row, 1).toString());
            txtSet.setText(tableModel.getValueAt(row, 2).toString());
            txtRepetisi.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    private boolean validasiInput() {
        if (txtNama.getText().isEmpty() || txtBeban.getText().isEmpty() ||
                txtSet.getText().isEmpty() || txtRepetisi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data belum lengkap!"); return false;
        }
        return true;
    }

    private AngkatBeban buatObjekDariInput() {
        try {
            return new AngkatBeban(txtNama.getText(), Double.parseDouble(txtBeban.getText()),
                    Integer.parseInt(txtSet.getText()), Integer.parseInt(txtRepetisi.getText()));
        } catch (NumberFormatException e) { return new AngkatBeban("-", 0, 0, 0); }
    }

    private void selesaiAksi() {
        hitungStatistik();
        txtNama.setText(""); txtBeban.setText(""); txtSet.setText(""); txtRepetisi.setText("");
        table.clearSelection();
    }

    private void hitungStatistik() {
        double total = 0;
        for (AngkatBeban ab : dataLatihan) total += ab.hitungVolume(); //Perulangan (Modul 3)
        lblTotalVolume.setText("TOTAL VOLUME: " + total + " KG");
    }

    // --- THEME LOGIC ---
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
        btnToggleMode.repaint();
    }

    private void applyTheme() {
        Color bg = isDarkMode ? darkBg : lightBg;
        Color panel = isDarkMode ? darkPanel : lightPanel;
        Color text = isDarkMode ? darkText : lightText;
        Color inputBg = isDarkMode ? new Color(45, 45, 45) : Color.WHITE;

        // Warna Tombol Utama (Hitam di Light Mode, Putih di Dark Mode)
        Color btnColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color btnText = isDarkMode ? Color.BLACK : Color.WHITE;

        contentPane.setBackground(bg);
        lblTitle.setForeground(text);

        for(JLabel l : labels) if(l != null) l.setForeground(isDarkMode ? Color.GRAY : Color.GRAY);

        panelInput.setBgColor(panel);
        scrollPane.setBgColor(panel);
        scrollPane.getViewport().setBackground(panel);

        // Footer & Tombol Simpan
        panelTotal.setBgColor(btnColor);
        lblTotalVolume.setForeground(btnText);

        // Update Tombol Simpan dengan warna kontras
        btnSimpan.setColors(btnColor, btnText);

        updateTextFieldStyle(txtNama, inputBg, text);
        updateTextFieldStyle(txtBeban, inputBg, text);
        updateTextFieldStyle(txtSet, inputBg, text);
        updateTextFieldStyle(txtRepetisi, inputBg, text);

        table.setBackground(panel);
        table.setForeground(text);
        table.setGridColor(isDarkMode ? new Color(60,60,60) : new Color(230,230,230));
        table.setSelectionBackground(isDarkMode ? Color.GRAY : new Color(220, 220, 220));
        table.setSelectionForeground(isDarkMode ? Color.WHITE : Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(btnColor);
        header.setForeground(btnText);
        header.setFont(new Font("Poppins", Font.BOLD, 14));

        btnToggleMode.setBgColor(isDarkMode ? new Color(60, 60, 60) : new Color(220, 220, 220));
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateTextFieldStyle(JTextField tf, Color bg, Color fg) {
        tf.setBackground(bg); tf.setForeground(fg); tf.setCaretColor(fg);
    }

    private JLabel addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(fontLabel);
        label.setBounds(x, y, 200, 20);
        panel.add(label);
        return label;
    }

    private JTextField addRoundedTextField(JPanel panel, int x, int y, int width) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, width, 35);
        tf.setFont(fontInput);
        tf.setOpaque(false);
        tf.setBorder(new RoundedBorder(15));
        panel.add(tf);
        return tf;
    }

    // Main Method Start
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SplashScreen splash = new SplashScreen();
        splash.startAnimation();
    }

    // --- CUSTOM COMPONENTS  ---

    class ThemeToggleButton extends JButton {
        private int radius = 40; private Color bgColor = Color.WHITE;
        public ThemeToggleButton() {
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        public void setBgColor(Color color) { this.bgColor = color; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor); g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            if (isDarkMode) {
                g2.setColor(Color.WHITE); int size = 16;
                int x = (getWidth()-size)/2; int y = (getHeight()-size)/2;
                g2.fillOval(x, y, size, size);
            } else {
                g2.setColor(Color.BLACK); int size = 20; int x = (getWidth()-size)/2; int y = (getHeight()-size)/2;
                Area moon = new Area(new Ellipse2D.Double(x, y, size, size));
                Area shadow = new Area(new Ellipse2D.Double(x+6, y-2, size, size));
                moon.subtract(shadow); g2.fill(moon);
            }
            g2.dispose(); super.paintComponent(g);
        }
    }

    class RoundedButton extends JButton {
        private int radius = 25;
        private Color bgColor;
        private Color fgColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bgColor = bg;
            this.fgColor = fg;
            setBackground(bg);

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Poppins", Font.BOLD, 14));
            setForeground(fg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(bgColor.darker()); repaint(); }
                public void mouseExited(MouseEvent e) { setBackground(bgColor); repaint(); }
            });
        }

        public void setColors(Color bg, Color fg) {
            this.bgColor = bg;
            this.fgColor = fg;
            setBackground(bg); // UPDATE background JComponent saat tema berubah
            setForeground(fg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gunakan getBackground() agar sinkron dengan perubahan tema
            g2.setColor(getBackground());

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundedScrollPane extends JScrollPane {
        private int radius; private Color bgColor;
        public RoundedScrollPane(Component view, int radius, Color bgColor) {
            super(view); this.radius = radius; this.bgColor = bgColor;
            setOpaque(false); getViewport().setOpaque(false); setBorder(null);
        }
        public void setBgColor(Color color) { this.bgColor = color; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor); g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(new Color(200, 200, 200)); g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
        @Override public void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            super.paintChildren(g2); g2.dispose();
        }
    }

    class RoundedPanel extends JPanel {
        private int radius; private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) { this.radius = radius; this.bgColor = bgColor; setOpaque(false); }
        public void setBgColor(Color color) { this.bgColor = color; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor); g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(isDarkMode ? new Color(60,60,60) : new Color(220, 220, 220));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
    }

    class RoundedBorder extends AbstractBorder {
        private int radius; public RoundedBorder(int radius) { this.radius = radius; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isDarkMode ? new Color(80,80,80) : Color.LIGHT_GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(5, 10, 5, 10); }
    }
}