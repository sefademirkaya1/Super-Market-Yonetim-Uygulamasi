package MarketYonetim;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

public class AnaMenu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblClock;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    AnaMenu frame = new AnaMenu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AnaMenu() {
    	setBackground(new Color(128, 128, 128));
    	setResizable(false);
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 933, 562);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAnaMen = new JLabel("ANA MENÜ");
        lblAnaMen.setBounds(278, 10, 300, 53);
        lblAnaMen.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnaMen.setForeground(Color.BLUE);
        lblAnaMen.setFont(new Font("Tahoma", Font.BOLD, 24));
        contentPane.add(lblAnaMen);

        JButton btnNewButton = new JButton("GÜNLÜK CİRO");
        btnNewButton.setBounds(94, 105, 185, 47);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GunlukCiroModulu gunlukCiroModulu = new GunlukCiroModulu();
                gunlukCiroModulu.setVisible(true);
                dispose();
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnNewButton.setForeground(Color.RED);
        contentPane.add(btnNewButton);

        JButton btnPersonelYonetim = new JButton("PERSONEL YÖNETİM");
        btnPersonelYonetim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PersonelYonetimModulu personelYonetimModulu = new PersonelYonetimModulu();
                personelYonetimModulu.setVisible(true);
                dispose();
            }
        });
        btnPersonelYonetim.setBounds(336, 105, 213, 47);
        btnPersonelYonetim.setForeground(Color.RED);
        btnPersonelYonetim.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnPersonelYonetim);

        JButton btnMusteriYonetim = new JButton("MÜŞTERİ YÖNETİM");
        btnMusteriYonetim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MusteriYonetimModulu musteriYonetimModulu = new MusteriYonetimModulu();
                musteriYonetimModulu.setVisible(true);
                dispose();
            }
        });
        btnMusteriYonetim.setBounds(578, 105, 213, 47);
        btnMusteriYonetim.setForeground(Color.RED);
        btnMusteriYonetim.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnMusteriYonetim);

        JButton btnUrunYonetim = new JButton("ÜRÜN YÖNETİM");
        btnUrunYonetim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UrunYonetimModulu urunYonetimModulu = new UrunYonetimModulu();
                urunYonetimModulu.setVisible(true);
                dispose();
            }
        });
        btnUrunYonetim.setBounds(94, 194, 185, 47);
        btnUrunYonetim.setForeground(Color.RED);
        btnUrunYonetim.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnUrunYonetim);

        JButton btnMaliDurum = new JButton("MALİ DURUM");
        btnMaliDurum.setBounds(336, 194, 213, 47);
        btnMaliDurum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MaliDurumModulu maliDurumModulu = new MaliDurumModulu();
                maliDurumModulu.setVisible(true);
                dispose();
            }
        });
        btnMaliDurum.setForeground(Color.RED);
        btnMaliDurum.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnMaliDurum);

        JButton btnSikayetIzleme = new JButton("ŞİKAYET İZLEME");
        btnSikayetIzleme.setBounds(578, 194, 213, 47);
        btnSikayetIzleme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SikayetModulu sikayetModulu = new SikayetModulu();
                sikayetModulu.setVisible(true);
                dispose();
            }
        });
        btnSikayetIzleme.setForeground(Color.RED);
        btnSikayetIzleme.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnSikayetIzleme);

        JButton btnHaftalikCiro = new JButton("HAFTALIK CİRO");
        btnHaftalikCiro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HaftalikCirolar haftalikCiro = new HaftalikCirolar();
                haftalikCiro.setVisible(true);
                dispose();
            }
        });
        btnHaftalikCiro.setBounds(94, 283, 185, 47);
        btnHaftalikCiro.setForeground(Color.RED);
        btnHaftalikCiro.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnHaftalikCiro);

        JButton btnAylikCiro = new JButton("AYLIK CİRO");
        btnAylikCiro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AylikCiroModulu aylikCiro = new AylikCiroModulu();
                aylikCiro.setVisible(true);
                dispose();
            }
        });
        btnAylikCiro.setBounds(336, 283, 213, 47);
        btnAylikCiro.setForeground(Color.RED);
        btnAylikCiro.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnAylikCiro);

        JButton btnYillikCiro = new JButton("YILLIK CİRO");
        btnYillikCiro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                YillikCiroModulu yillikCiro = new YillikCiroModulu();
                yillikCiro.setVisible(true);
                dispose();
            }
        });
        btnYillikCiro.setBounds(578, 283, 213, 47);
        btnYillikCiro.setForeground(Color.RED);
        btnYillikCiro.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnYillikCiro);

        lblClock = new JLabel();
        lblClock.setBackground(Color.WHITE);
        lblClock.setForeground(Color.BLACK);
        lblClock.setHorizontalAlignment(SwingConstants.CENTER);
        lblClock.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblClock.setBounds(225, 419, 463, 30);
        contentPane.add(lblClock);

        Thread clockThread = new Thread(() -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            while (true) {
                String currentTime = dateFormat.format(new Date());
                lblClock.setText(currentTime);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.start();
    }
}
