package bukantugasakhir;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class IntroPage extends JFrame {

    private Font fontHeader = new Font("Poppins", Font.BOLD, 32);
    private Font fontSubHeader = new Font("Poppins", Font.PLAIN, 16);

    public IntroPage() {
        setTitle("Workout Tracker");
        setSize(700, 760); // Ukuran sama dengan MainApp
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Muncul di tengah layar
        setLayout(null);
        getContentPane().setBackground(Color.WHITE); // Background Putih Bersih

        // --- 1. JUDUL & SUBJUDUL ---
        JLabel lblTitle = new JLabel("WELCOME, CHAMPION.", SwingConstants.CENTER);
        lblTitle.setFont(fontHeader);
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBounds(0, 30, 700, 50);
        add(lblTitle);

        JLabel lblSub = new JLabel("Your journey to greatness begins today.", SwingConstants.CENTER);
        lblSub.setFont(fontSubHeader);
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(0, 80, 700, 30);
        add(lblSub);

        JLabel lblImage = new JLabel();
        lblImage.setBounds(50, 130, 600, 450);
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            if (new File("stickman_pose.png").exists()) {
                ImageIcon rawIcon = new ImageIcon("stickman_pose.png");
                Image scaledImage = rawIcon.getImage().getScaledInstance(574, 434, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaledImage));
            } else {
                lblImage.setText("Image 'stickman_pose.png' not found!");
                lblImage.setForeground(Color.RED);
                lblImage.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(lblImage);

        // --- 3. TOMBOL START ---
        RoundedButton btnStart = new RoundedButton("START YOUR WORKOUT");
        btnStart.setBounds(150, 570, 400, 60);

        btnStart.addActionListener(e -> {
            // Aksi saat tombol ditekan:
            this.dispose(); // 1. Tutup Intro Page
            new MainApp().setVisible(true); // 2. Buka MainApp
        });
        add(btnStart);
    }


    class RoundedButton extends JButton {
        private int radius = 30;
        private Color bgColor = Color.BLACK;
        private Color fgColor = Color.WHITE;

        public RoundedButton(String text) {
            super(text);
            setBackground(bgColor);
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setFont(new Font("Poppins", Font.BOLD, 18));
            setForeground(fgColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Efek Hover (Berubah jadi abu gelap saat kursor di atasnya)
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { bgColor = new Color(50, 50, 50); repaint(); }
                public void mouseExited(MouseEvent e) { bgColor = Color.BLACK; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}