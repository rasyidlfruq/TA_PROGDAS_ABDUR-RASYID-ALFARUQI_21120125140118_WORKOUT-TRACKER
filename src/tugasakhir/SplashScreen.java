package tugasakhir;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel lblStatus;
    private Image stickmanImage;
    private int radius = 30;

    public SplashScreen() {
        setSize(450, 350);
        setLocationRelativeTo(null);
        setBackground(new Color(0,0,0,0));
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        try {
            if (new File("arnold_pose.png").exists()) {
                stickmanImage = new ImageIcon("arnold_pose.png").getImage();
            }
        } catch (Exception e) { e.printStackTrace(); }

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

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
        content.setLayout(null);
        content.setOpaque(false);
        setContentPane(content);

        JLabel lblTitle = new JLabel("STICKMAN WORKOUT", SwingConstants.CENTER);
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
        progressBar.setForeground(Color.BLACK);
        progressBar.setBackground(new Color(230, 230, 230));
        content.add(progressBar);
    }

    public void startAnimation() {
        setVisible(true);
        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(25); // Kecepatan loading
                progressBar.setValue(i);
                if (i == 40) lblStatus.setText("Loading motivation...");
                if (i == 80) lblStatus.setText("Almost ready...");
            }
        } catch (InterruptedException e) { e.printStackTrace(); }

        // Setelah loading selesai:
        this.dispose(); // 1. Tutup Splash Screen

        // 2. Buka Intro Page (Halaman Pengantar)
        SwingUtilities.invokeLater(() -> {
            new IntroPage().setVisible(true);
        });
    }
}