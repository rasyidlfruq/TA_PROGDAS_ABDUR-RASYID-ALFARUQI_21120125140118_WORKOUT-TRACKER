package tugasakhir2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

// [Modul 8] GUI: Menggunakan JWindow (bukan JFrame) agar tidak ada tombol close/minimize bawaan
public class SplashScreen extends JWindow {

    // [Modul 1] Variabel & Tipe Data: Deklarasi komponen GUI dan properti
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private Image stickmanImage;
    private int radius = 30; // Variabel integer untuk kelengkungan sudut

    // [Modul 5] Constructor: Method yang dijalankan saat objek SplashScreen dibuat
    public SplashScreen() {
        // Konfigurasi dasar window
        setSize(450, 350);
        setLocationRelativeTo(null); // Posisi tengah layar

        // [Modul 8] GUI Advanced: Membuat background transparan agar bisa dibentuk bulat
        setBackground(new Color(0,0,0,0));

        // Membentuk window menjadi Rounded Rectangle
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        // [Modul 2] Pengkondisian & Exception Handling: Cek apakah file gambar ada?
        try {
            if (new File("arnold_pose.png").exists()) {
                stickmanImage = new ImageIcon("arnold_pose.png").getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // [Modul 8] Custom GUI: Membuat Panel khusus untuk menggambar background dan gambar
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Mengubah Graphics menjadi Graphics2D untuk fitur grafis canggih (Antialiasing)
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Menggambar background putih bulat
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                // Menggambar garis tepi (Border) hitam
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

                // [Modul 2] Pengkondisian: Jika gambar berhasil di-load, tampilkan. Jika tidak, tampilkan teks error.
                if (stickmanImage != null) {
                    g2.drawImage(stickmanImage, 125, 30, 200, 150, this);
                } else {
                    g2.setFont(new Font("Poppins", Font.BOLD, 14));
                    g2.setColor(Color.RED);
                    g2.drawString("arnold_pose.png tidak ditemukan!", 90, 100);
                }
                g2.dispose();
            }
        };
        content.setLayout(null); // Layout manual (Absolute Positioning)
        content.setOpaque(false); // Agar panel transparan mengikuti JWindow
        setContentPane(content);

        // --- Menambahkan Komponen GUI (Label & Progress Bar) ---

        JLabel lblTitle = new JLabel("WORKOUT TRACKER", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBounds(0, 190, 450, 40);
        content.add(lblTitle);

        lblStatus = new JLabel("Preparing muscles...", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Poppins", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setBounds(0, 230, 450, 20);
        content.add(lblStatus);

        progressBar = new JProgressBar();
        progressBar.setBounds(75, 270, 300, 6);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(Color.BLACK); // Warna loading bar
        progressBar.setBackground(new Color(230, 230, 230)); // Warna background bar
        content.add(progressBar);
    }

    // [Modul 4] Method: Fungsi untuk menjalankan logika animasi loading
    public void startAnimation() {
        setVisible(true);
        try {
            // [Modul 3] Perulangan: Loop dari 0 sampai 100 untuk mengisi progress bar
            for (int i = 0; i <= 100; i++) {
                // Thread.sleep digunakan untuk menjeda program (simulasi loading)
                Thread.sleep(20);
                progressBar.setValue(i);

                // [Modul 2] Pengkondisian: Mengubah teks status berdasarkan persentase
                if (i == 40) lblStatus.setText("Loading data...");
                if (i == 80) lblStatus.setText("Starting...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Menutup Splash Screen setelah loading selesai
        this.dispose();

        // --- TRANSISI KE PROGRAM UTAMA ---
        // Membuka MainAppForm setelah splash screen tertutup
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Workout Tracker");
            // Memanggil panel dari MainAppForm (GUI Designer)
            frame.setContentPane(new MainAppForm().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Mengatur ukuran window aplikasi utama
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null); // Tengah layar
            frame.setVisible(true);
        });
    }
}