package tugasakhir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class MainAppForm {

    // ==================================================================================
    // BAGIAN 1: DEKLARASI VARIABEL & DATA
    // ==================================================================================

    // [Modul 7] Struktur Data: Menggunakan Stack (LIFO - Last In First Out) untuk menyimpan objek.
    private Stack<AngkatBeban> dataLatihan = new Stack<>();

    // [Modul 1] Variabel & Tipe Data: Model untuk tabel agar data bisa ditampilkan di GUI.
    private DefaultTableModel tableModel;

    // Font Configuration (Variabel Global untuk Styling)
    private Font fontGlobal = new Font("Poppins", Font.PLAIN, 14);
    private Font fontBold = new Font("Poppins", Font.BOLD, 14);

    // Komponen GUI (Dideklarasikan di sini agar bisa diakses oleh semua method)
    public JPanel mainPanel;
    private JPanel panelInput;
    private JPanel panelTotal;
    private JTextField txtNama, txtBeban, txtSet, txtRepetisi;
    private JButton btnSimpan, btnUpdate, btnHapus;
    private JTable table;
    private JLabel lblTotalVolume;
    private JButton btnHapusSemua;
    private JScrollPane scrollPane;
    private JLabel lblTitle;


    // ==================================================================================
    // BAGIAN 2: MAIN METHOD & CONSTRUCTOR (ENTRY POINT)
    // ==================================================================================

    // [Modul 4] Method Main: Pintu gerbang utama program berjalan.
    public static void main(String[] args) {
        // Mengatur rendering font agar halus (Antialiasing)
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Menjalankan Splash Screen terlebih dahulu
        LoadingScreen splash = new LoadingScreen();
        splash.startAnimation();
    }

    // [Modul 5] Constructor: Method yang pertama kali dijalankan saat Class ini dipanggil.
    public MainAppForm() {
        // 1. Setup Awal Tabel
        createTableColumns();
        styleTable();

        // 2. Styling Panel Utama (Padding & Background)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Styling Label Total
        lblTotalVolume.setForeground(Color.WHITE);
        lblTotalVolume.setFont(new Font("Poppins", Font.BOLD, 16));

        // 3. [Modul 8] Event Handling: Menghubungkan tombol dengan logika method
        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnHapusSemua.addActionListener(e -> hapusSemuaData());

        // Event Listener untuk klik baris tabel
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { pilihBaris(); }
        });
    }


    // ==================================================================================
    // BAGIAN 3: LOGIKA SISTEM (CRUD, VALIDASI, PERHITUNGAN)
    // ==================================================================================

    // [Modul 4] Method: Logika untuk menyimpan data baru (CREATE)
    private void simpanData() {
        // [Modul 2] Pengkondisian: Cek apakah input kosong?
        if (!validasiInputKosong()) return;

        // Membuat objek baru dari inputan user
        AngkatBeban latihan = buatObjek();

        // [Modul 2] Pengkondisian: Jika objek null (berarti ada input negatif), hentikan proses
        if (latihan == null) return;

        // [Modul 7] Stack: Memasukkan data ke tumpukan (Push)
        dataLatihan.push(latihan);

        // Update tampilan
        refreshTable();
        hitungStatistik();
        clearInput();
        JOptionPane.showMessageDialog(mainPanel, "Data berhasil disimpan!");
    }

    // [Modul 4] Method: Logika untuk memperbarui data (UPDATE)
    private void updateData() {
        int row = table.getSelectedRow(); // Ambil baris yang dipilih

        // [Modul 2] Pengkondisian: Pastikan user sudah memilih baris
        if (row < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Pilih baris dulu!");
            return;
        }
        if (!validasiInputKosong()) return;

        AngkatBeban baru = buatObjek();
        if (baru == null) return; // Validasi input negatif

        // [Modul 7] Stack: Mengupdate data pada index tertentu
        dataLatihan.set(row, baru);

        refreshTable();
        hitungStatistik();
        clearInput();
        JOptionPane.showMessageDialog(mainPanel, "Data diperbarui!");
    }

    // [Modul 4] Method: Logika untuk menghapus data (DELETE)
    private void hapusSemuaData() {
        if (dataLatihan.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Data sudah kosong!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // [Modul 7] Stack: Menghapus data dari memori
        dataLatihan.clear();

        refreshTable();
        hitungStatistik();
        clearInput();

        JOptionPane.showMessageDialog(mainPanel, "Semua data telah dihapus (Reset Berhasil).");

    }

    // [Modul 4] Method: Logika untuk menghapus semua data (DELETE ALL)
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Pilih data dulu.");
            return;
        }
        // [Modul 7] Stack: Menghapus data dari memori
        dataLatihan.remove(row);

        refreshTable();
        hitungStatistik();
        clearInput();
    }

    // [Modul 4] Method: Membuat Objek AngkatBeban dari Input Form
    private AngkatBeban buatObjek() {
        try {
            // [Modul 1] Variabel: Mengambil teks dari GUI
            String nama = txtNama.getText();
            // Konversi String ke Tipe Data Angka (Double/Int)
            double beban = Double.parseDouble(txtBeban.getText());
            int set = Integer.parseInt(txtSet.getText());
            int rep = Integer.parseInt(txtRepetisi.getText());

            // [Modul 2] Pengkondisian: Validasi agar tidak ada angka minus/nol
            if (beban <= 0 || set <= 0 || rep <= 0) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Beban, Set, dan Repetisi harus lebih besar dari 0!",
                        "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return null; // Return null sebagai penanda error
            }

            // [Modul 5] Class & Object: Instansiasi objek baru
            return new AngkatBeban(nama, beban, set, rep);

        } catch (NumberFormatException e) {
            // [Modul 2] Exception Handling: Menangani jika user input huruf di kolom angka
            JOptionPane.showMessageDialog(mainPanel, "Input harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // [Modul 3] Perulangan: Menghitung total volume dari seluruh data
    private void hitungStatistik() {
        double total = 0;
        // Looping (For-Each) untuk membaca isi Stack satu per satu
        for (AngkatBeban item : dataLatihan) {
            // [Modul 6] Encapsulation: Mengakses method hitungVolume dari objek
            total += item.hitungVolume();
        }
        // Menampilkan hasil ke Label GUI
        lblTotalVolume.setText("TOTAL VOLUME: " + total + " KG");
    }

    // [Modul 2] Validasi sederhana input kosong
    private boolean validasiInputKosong() {
        if (txtNama.getText().isEmpty() || txtBeban.getText().isEmpty() ||
                txtSet.getText().isEmpty() || txtRepetisi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Data belum lengkap!");
            return false;
        }
        return true;
    }

    // Method untuk mengambil data dari Stack dan memasukkannya ke Tabel GUI
    private void refreshTable() {
        tableModel.setRowCount(0); // Reset tabel jadi kosong
        // [Modul 3] Perulangan: Loop data untuk dimasukkan ke baris tabel
        for (AngkatBeban a : dataLatihan) {
            // [Modul 6] Encapsulation: Menggunakan Getter (getNamaGerakan, dll)
            tableModel.addRow(new Object[]{
                    a.getNamaGerakan(), a.getBeban(), a.getSet(), a.getRepetisi(), a.hitungVolume()
            });
        }
    }

    // Method untuk memindahkan data dari Tabel ke TextField saat diklik
    private void pilihBaris() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtNama.setText(dataLatihan.get(row).getNamaGerakan());
            txtBeban.setText(String.valueOf(dataLatihan.get(row).getBeban()));
            txtSet.setText(String.valueOf(dataLatihan.get(row).getSet()));
            txtRepetisi.setText(String.valueOf(dataLatihan.get(row).getRepetisi()));
        }
    }

    // Method untuk membersihkan form input
    private void clearInput() {
        txtNama.setText(""); txtBeban.setText(""); txtSet.setText(""); txtRepetisi.setText("");
    }


    // ==================================================================================
    // BAGIAN 4: GUI SETUP & CUSTOM COMPONENTS (TAMPILAN)
    // ==================================================================================

    // [Modul 8] GUI: Setup Model Tabel
    private void createTableColumns() {
        String[] columnNames = {"Gerakan", "Beban", "Set", "Rep", "Volume"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);
    }

    // [Modul 8] GUI: Styling Tabel (Warna Header, Font)
    private void styleTable() {
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setFont(fontGlobal);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setFont(fontBold);

        if(scrollPane != null) {
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    // [Modul 8] GUI: Method khusus IntelliJ Designer untuk Custom Component
    private void createUIComponents() {
        // Panel Rounded (Sudut Tumpul)
        panelInput = new RoundedPanel(30, Color.WHITE);
        panelTotal = new RoundedPanel(30, Color.BLACK);

        // Tombol Rounded dengan warna berbeda
        btnSimpan = new RoundedButton("SIMPAN", Color.BLACK, Color.WHITE);
        btnUpdate = new RoundedButton("PERBARUI", new Color(100, 100, 100), Color.WHITE);
        btnHapus = new RoundedButton("HAPUS", new Color(220, 53, 69), Color.WHITE);
        btnHapusSemua = new RoundedButton("HAPUS SEMUA", new Color(139, 0, 0), Color.WHITE);

        // Input Field Rounded
        txtNama = new RoundedTextField(15);
        txtBeban = new RoundedTextField(15);
        txtSet = new RoundedTextField(15);
        txtRepetisi = new RoundedTextField(15);
    }

    // --- INNER CLASSES: IMPLEMENTASI GRAFIS MANUAL (GRAPHICS2D) ---

    // Class Panel dengan sudut melengkung
    public static class RoundedPanel extends JPanel {
        private int radius; private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) { this.radius = radius; this.bgColor = bgColor; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(new Color(230,230,230)); // Garis tepi tipis
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }

    // Class Tombol dengan sudut melengkung
    public static class RoundedButton extends JButton {
        private Color bgColor, fgColor;
        public RoundedButton(String text, Color bg, Color fg) {
            super(text); this.bgColor = bg; this.fgColor = fg;
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setFont(new Font("Poppins", Font.BOLD, 14)); setForeground(fg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose(); super.paintComponent(g);
        }
    }

    // Class TextField dengan border melengkung
    public static class RoundedTextField extends JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setFont(new Font("Poppins", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding teks dalam
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.setColor(Color.LIGHT_GRAY); // Warna Border
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}