package MarketYonetim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.SystemColor;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;



public class PersonelYonetimModulu extends JFrame {

    private static final long serialVersionUID = 1L;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private JPanel contentPane;
    private JTextField personelAdSoyad;
    private JTextField iseGirisTarihi;
    private JTextField mevcutMaas;
    private JTextField personelGorevi;
    private JTextField telNo;
    private JTable table;
    private JTextField email;

 
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    PersonelYonetimModulu frame = new PersonelYonetimModulu();
                    frame.setVisible(true);
                    frame.updateTable();
          
                  
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

   
    public PersonelYonetimModulu() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 810, 519);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 250));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblPersonelYnetim = new JLabel("PERSONEL YÖNETİM");
        lblPersonelYnetim.setHorizontalAlignment(SwingConstants.CENTER);
        lblPersonelYnetim.setForeground(Color.RED);
        lblPersonelYnetim.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblPersonelYnetim.setBounds(224, 10, 297, 29);
        contentPane.add(lblPersonelYnetim);

        JLabel lblpersonelAdSoyad = new JLabel("Personel Ad Soyad");
        lblpersonelAdSoyad.setBounds(10, 131, 124, 29);
        contentPane.add(lblpersonelAdSoyad);

        JLabel lbliseGirisTarihi = new JLabel("İşe Giriş Tarihi");
        lbliseGirisTarihi.setBounds(124, 131, 91, 29);
        contentPane.add(lbliseGirisTarihi);

        JLabel lblmevcutMaas = new JLabel("Mevcut Maaş");
        lblmevcutMaas.setBounds(224, 131, 92, 29);
        contentPane.add(lblmevcutMaas);

        JLabel lblPersonelGorevi = new JLabel("Pozisyon");
        lblPersonelGorevi.setBounds(326, 131, 63, 29);
        contentPane.add(lblPersonelGorevi);

        JLabel lblTelNo = new JLabel("TEL NO");
        lblTelNo.setBounds(395, 131, 63, 29);
        contentPane.add(lblTelNo);

        personelAdSoyad = new JTextField();
        personelAdSoyad.setColumns(10);
        personelAdSoyad.setBounds(0, 170, 114, 19);
        contentPane.add(personelAdSoyad);

        iseGirisTarihi = new JTextField();
        iseGirisTarihi.setColumns(10);
        iseGirisTarihi.setBounds(124, 170, 74, 19);
        contentPane.add(iseGirisTarihi);

        mevcutMaas = new JTextField();
        mevcutMaas.setColumns(10);
        mevcutMaas.setBounds(224, 170, 69, 19);
        contentPane.add(mevcutMaas);

        personelGorevi = new JTextField();
        personelGorevi.setColumns(10);
        personelGorevi.setBounds(322, 170, 52, 19);
        contentPane.add(personelGorevi);

        telNo = new JTextField();
        telNo.setColumns(10);
        telNo.setBounds(396, 170, 52, 19);
        contentPane.add(telNo);
        
        email = new JTextField();
        email.setColumns(10);
        email.setBounds(396, 170, 52, 19);
        contentPane.add(email);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(23, 241, 666, 170);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        updateTable();

        JButton btnEkle = new JButton("Ekle");
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    PreparedStatement pst = con.prepareStatement(
                            "INSERT INTO personel_yonetim_modulu (ad_soyad, ise_giris_tarihi, mevcut_maas, personel_gorevi, personel_telno, email) VALUES (?, ?, ?, ?, ?, ?)");

                    pst.setString(1, personelAdSoyad.getText());
                    pst.setString(2, iseGirisTarihi.getText());
                    pst.setDouble(3, Double.parseDouble(mevcutMaas.getText()));
                    pst.setString(4, personelGorevi.getText());
                    pst.setString(5, telNo.getText());
                    pst.setString(6, email.getText());

                    pst.executeUpdate();
                    con.close();

                    // Ekleme işlemi başarılı mesajı
                    JOptionPane.showMessageDialog(null, "Personel başarıyla eklendi.");
                    // Tabloyu güncelle
                    updateTable();
                } catch (SQLException ex) {
                    // Hata durumunda hata mesajı
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Personel eklenirken bir hata oluştu: " + ex.getMessage());
                }
            }
        });
        btnEkle.setForeground(new Color(0, 128, 0));
        btnEkle.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnEkle.setBounds(712, 162, 85, 32);
        contentPane.add(btnEkle);

        JButton btnSil = new JButton("Sil");
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Seçilen satırın indeksini al
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Lütfen bir personel seçin.");
                        return;
                    }

                    // Seçilen satırdaki personelin ID'sini al
                    int personelID = (int) table.getValueAt(selectedRow, 0);

                    // Veritabanından ilgili personeli sil
                    Connection con = DriverManager.getConnection(url, username, password);
                    PreparedStatement pst = con.prepareStatement("DELETE FROM personel_yonetim_modulu WHERE personel_id = ?");
                    pst.setInt(1, personelID);
                    pst.executeUpdate();
                    con.close();

                    // Silme işlemi başarılı mesajı
                    JOptionPane.showMessageDialog(null, "Personel başarıyla silindi.");
                    // Tabloyu güncelle
                    updateTable();
                } catch (SQLException ex) {
                    // Hata durumunda hata mesajı
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Personel silinirken bir hata oluştu: " + ex.getMessage());
                }
            }
        });
        btnSil.setForeground(Color.RED);
        btnSil.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSil.setBounds(712, 214, 74, 34);
        contentPane.add(btnSil);
        
        
        JButton btnGuncelle = new JButton("Güncelle");
        btnGuncelle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Seçilen satırın indeksini al
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Lütfen bir personel seçin.");
                        return;
                    }

                    // Seçilen satırdaki personelin ID'sini al
                    int personelID = (int) table.getValueAt(selectedRow, 0);

                    // Yeni bilgileri al
                    String yeniAdSoyad = personelAdSoyad.getText();
                    String yeniIseGirisTarihi = iseGirisTarihi.getText();
                    double yeniMevcutMaas = Double.parseDouble(mevcutMaas.getText());
                    String yeniPersonelGorevi = personelGorevi.getText();
                    String yeniTelNo = telNo.getText();
                    String yeniEmail = email.getText();

                    // Veritabanındaki ilgili personeli güncelle
                    Connection con = DriverManager.getConnection(url, username, password);
                    PreparedStatement pst = con.prepareStatement("UPDATE personel_yonetim_modulu SET ad_soyad = ?, ise_giris_tarihi = ?, mevcut_maas = ?, personel_gorevi = ?, personel_telno = ?, email= ? WHERE personel_id = ?");
                    pst.setString(1, yeniAdSoyad);
                    pst.setString(2, yeniIseGirisTarihi);
                    pst.setDouble(3, yeniMevcutMaas);
                    pst.setString(4, yeniPersonelGorevi);
                    pst.setString(5, yeniTelNo);
                    pst.setString(6, yeniEmail);
                    pst.setInt(7, personelID);
                    pst.executeUpdate();
                    con.close();

                    // Güncelleme işlemi başarılı mesajı
                    JOptionPane.showMessageDialog(null, "Personel başarıyla güncellendi.");
                    // Tabloyu güncelle
                    updateTable();
                } catch (SQLException ex) {
                    // Hata durumunda hata mesajı
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Personel güncellenirken bir hata oluştu: " + ex.getMessage());
                }
            }
        });

        btnGuncelle.setForeground(Color.BLUE);
        btnGuncelle.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGuncelle.setBounds(710, 258, 87, 29);
        contentPane.add(btnGuncelle);

        JButton btnNewButton_1_1_1_2_1 = new JButton("Ana Menu");
        btnNewButton_1_1_1_2_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1_1_1_2_1.setForeground(Color.BLACK);
        btnNewButton_1_1_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnNewButton_1_1_1_2_1.setBounds(662, 5, 124, 53);
        contentPane.add(btnNewButton_1_1_1_2_1);
        

    
        
        JButton btnRapor = new JButton("Çıktı Al");
        btnRapor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String reportContent = generateReport(); // Rapor içeriğini oluştur
                writeReportToFile("personel_listesi.txt", reportContent); // Metin dosyası olarak raporu yaz
                convertReportToPDF(); // PDF olarak raporu dönüştür ve kaydet
        	}
        });
        btnRapor.setBounds(683, 437, 85, 35);
        contentPane.add(btnRapor);
        
        email = new JTextField();
        email.setColumns(10);
        email.setBounds(469, 170, 52, 19);
        contentPane.add(email);
        
        JLabel Email = new JLabel("Email");
        Email.setBounds(468, 131, 63, 29);
        contentPane.add(Email);
        
        JButton btnMail = new JButton("Mail");
        btnMail.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Lütfen bir satır seçin.");
                    return;
                }
                
                String name = table.getValueAt(selectedRow, 1).toString();
                String pozisyon = table.getValueAt(selectedRow, 4).toString();
                String email = table.getValueAt(selectedRow, 6).toString();  // Email sütununun indeksini 3 olarak varsayıyorum, gerekirse düzeltin

                sendEmail( name, pozisyon, email);
        		
        	}
        });
        btnMail.setForeground(SystemColor.desktop);
        btnMail.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnMail.setBounds(710, 319, 87, 29);
        contentPane.add(btnMail);

        // Tabloya tıklanıldığında seçili satırın verilerini text alanlarına yükleme
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Tıklanan satırın indeksini al
                int selectedRow = table.getSelectedRow();
                // Seçili satırın verilerini text alanlarına yükleme
                personelAdSoyad.setText(table.getValueAt(selectedRow, 1).toString());
                iseGirisTarihi.setText(table.getValueAt(selectedRow, 2).toString());
                mevcutMaas.setText(table.getValueAt(selectedRow, 3).toString());
                personelGorevi.setText(table.getValueAt(selectedRow, 4).toString());
                telNo.setText(table.getValueAt(selectedRow, 5).toString());
                email.setText(table.getValueAt(selectedRow, 6).toString());
            }
        });

    }

    // Tabloyu güncelleyen yöntem
    private void updateTable() {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM personel_yonetim_modulu ORDER BY personel_id");

            // Tablo modeli oluştur
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Personel ID");
            model.addColumn("Ad Soyad");
            model.addColumn("İşe Giriş Tarihi");
            model.addColumn("Mevcut Maaş");
            model.addColumn("Pozisyon"); // Yeni eklenen sütun
            model.addColumn("Tel No"); // Yeni eklenen sütun
            model.addColumn("E Mail"); 

            // ResultSet'ten verileri al ve tabloya ekle
            while (rs.next()) {
                Object[] row = { rs.getInt("personel_id"), rs.getString("ad_soyad"), rs.getString("ise_giris_tarihi"),
                        rs.getDouble("mevcut_maas"), rs.getString("personel_gorevi"), rs.getString("personel_telno"),rs.getString("email") };
                model.addRow(row);
            }

            // Tabloyu güncelle
            table.setModel(model);

            con.close(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Tablo güncellenirken bir hata oluştu: " + ex.getMessage());
        }
    }
    private String filterUnsupportedCharacters(String text) {
        // Türkçe karakterleri değiştir
        text = text.replace("ı", "i");
        text = text.replace("ş", "s");
        text = text.replace("ğ", "g");
        text = text.replace("ö", "o");
        text = text.replace("İ", "I");


        // Horizontal tab'leri kaldır
        text = text.replaceAll("\t", "");

        return text;
    }


    private String generateReport() {
        StringBuilder report = new StringBuilder();
        
        // Tablo raporu
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                // Karakterlerin uygunluğunu kontrol et ve uygun olmayanları filtrele
                String cellValue = model.getValueAt(i, j).toString();
                cellValue = filterUnsupportedCharacters(cellValue);
                report.append(cellValue).append("\t");
            }
            report.append("\n");
        }
        
        // JTextArea raporu
   
        
        return report.toString();
    }


    // Raporları dosyaya yazan metot
    private void writeReportToFile(String fileName, String content) {
        // Dosyanın yazılacağı konumu belirleyin
    	 String filePath = System.getProperty("user.home") + "\\Desktop\\" + fileName;



        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(content);
            printWriter.close();
            JOptionPane.showMessageDialog(null, "Rapor başarıyla oluşturuldu!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Rapor dosyası oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }
    private void convertReportToPDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Tabloyu içeren modeli al
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount();

            // Hücre genişliği ve yüksekliği
            float cellWidth = 70;
            float cellHeight = 20;

            // Tablonun başlangıç konumunu tanımla
            float startX = 20;
            float startY = 700;

         // Fontu belirle
            PDFont font = PDType1Font.HELVETICA_BOLD;
            contentStream.setFont(font, 9);

            // Sütun başlıklarını PDF'e ekle
            float headerYCoordinate = startY;
            for (int j = 0; j < columnCount; j++) {
                String headerCellValue = model.getColumnName(j);
                headerCellValue = filterUnsupportedCharacters(headerCellValue);

                // Hücre değerini PDF'e ekle
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, headerYCoordinate);
                contentStream.showText(headerCellValue);
                contentStream.endText();

                // Bir sonraki sütuna geç
                startX += cellWidth;
            }

            // Tablo verilerini PDF olarak ekle
            float yCoordinate = startY - cellHeight; // Başlık satırından bir hücre yukarıda başlat
            for (int i = 0; i < rowCount; i++) {
                float xCoordinate = 20; // Yeni bir satır başladığında X koordinatını başa al

                for (int j = 0; j < columnCount; j++) {
                    // Hücre değerini al
                    String cellValue = model.getValueAt(i, j).toString();
                    cellValue = filterUnsupportedCharacters(cellValue);

                    // Hücre değerini PDF'e ekle
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xCoordinate, yCoordinate);
                    contentStream.showText(cellValue);
                    contentStream.endText();

                    // Bir sonraki hücreye geç
                    xCoordinate += cellWidth;
                }

                // Bir sonraki satıra geç
                yCoordinate -= cellHeight;
            }

         // JTextArea içindeki "GunToplam" alanını PDF'e ekle
   
            
         
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yCoordinate - cellHeight);
          
            contentStream.endText();



            // İçerik akışını kapat
            contentStream.close();

            // PDF dosyasını kaydet
            String filePath = System.getProperty("user.home") + "\\Desktop\\personel_listesi.pdf";
            document.save(filePath);

            JOptionPane.showMessageDialog(null, "Rapor başarıyla PDF olarak oluşturuldu: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "PDF raporu oluştururken bir hata oluştu: " + e.getMessage());
        }
    }
    public static void sendEmail( String name, String pozisyon, String email) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");  // Enable STARTTLS for secure communication
        props.put("mail.smtp.host", "smtp.gmail.com");    // Gmail's SMTP server address
        props.put("mail.smtp.port", "587"); 

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                
                return new PasswordAuthentication("sender email", "16-digit special password created specifically for e-mail by Gmail");
            }
        });

        try {
            Message message = new MimeMessage(session);
            session.setDebug(true);

           
            message.setFrom(new InternetAddress("sender email"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("İnsan Kaynakları");
            message.setText("Sayın " + name + " " + pozisyon + " " + "pozisyonundaki" + ",\n\n" +
                    "İş akdiniz sonlandırılmıştır. " +  "\n\n" +
                    "Yeni Hayatınızda Başarılar Dileriz");

          
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect();
                transport.sendMessage(message, message.getAllRecipients());
            }

            System.out.println("E-posta gönderildi.");
            JOptionPane.showMessageDialog(null, "Mail başarıyla gönderildi.");

        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Mail gönderilirken bir hata oluştu: " + e.getMessage());
        }
    }

}
