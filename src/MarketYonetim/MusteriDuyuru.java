package MarketYonetim;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MusteriDuyuru extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea duyuru;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    MusteriDuyuru frame = new MusteriDuyuru();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MusteriDuyuru() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 865, 457);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblMusteriDuyuru = new JLabel("MÜŞTERİ DUYURU SİSTEMİ");
        lblMusteriDuyuru.setHorizontalAlignment(SwingConstants.CENTER);
        lblMusteriDuyuru.setForeground(Color.RED);
        lblMusteriDuyuru.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblMusteriDuyuru.setBounds(197, 10, 396, 29);
        contentPane.add(lblMusteriDuyuru);

        JButton btnMusteriYonetim = new JButton("MÜŞTERİ YÖNETİM");
        btnMusteriYonetim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MusteriYonetimModulu musteriYonetimModulu = new MusteriYonetimModulu();
                musteriYonetimModulu.setVisible(true);
                dispose();
            }
        });
        btnMusteriYonetim.setForeground(Color.RED);
        btnMusteriYonetim.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnMusteriYonetim.setBounds(676, 58, 152, 36);
        contentPane.add(btnMusteriYonetim);

        JButton btnAnamenu = new JButton("AnaMenü");
        btnAnamenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        btnAnamenu.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnAnamenu.setForeground(Color.RED);
        btnAnamenu.setBounds(676, 12, 152, 36);
        contentPane.add(btnAnamenu);

        duyuru = new JTextArea();
        duyuru.setBounds(71, 133, 419, 177);
        contentPane.add(duyuru);

        JLabel lblNewLabel = new JLabel("MÜŞTERİLERE GÖNDERİLECEK DUYURUYU GİRİNİZ");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel.setBounds(71, 94, 396, 29);
        contentPane.add(lblNewLabel);

        JButton btnGonder = new JButton("GÖNDER");
        btnGonder.setForeground(new Color(0, 128, 0));
        btnGonder.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGonder.setBounds(347, 339, 143, 42);
        btnGonder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String duyuruMetni = duyuru.getText();
                ArrayList<String> emailList = getEmailAddresses();
                for (String email : emailList) {
                    sendEmail(email, "SEFA MARKET", duyuruMetni);
                }
                JOptionPane.showMessageDialog(null, "Mail başarıyla gönderildi.");
            }
        });
        contentPane.add(btnGonder);
    }

    private ArrayList<String> getEmailAddresses() {
        ArrayList<String> emailList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT email FROM musteri_yonetim_modulu")) {
            
            while (rs.next()) {
                emailList.add(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanından email adresleri alınırken bir hata oluştu: " + e.getMessage());
        }
        return emailList;
    }

    public static void sendEmail(String email, String subject, String messageText) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sender e-mail address", "16-digit special password created specifically for e-mail by Gmail");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sender e-mail address"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("E-posta gönderildi.");

        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Mail gönderilirken bir hata oluştu: " + e.getMessage());
        }
    }
}
