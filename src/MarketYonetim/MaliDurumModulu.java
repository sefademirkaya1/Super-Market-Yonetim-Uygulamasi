package MarketYonetim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class MaliDurumModulu extends JFrame {

    private static final long serialVersionUID = 1L;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private JPanel contentPane;
    private JTextField personel_maasi;
    private JTextField magaza_kirasi;
    private JTextField faturalar;
    private JTextField sarf_malzeme;
    private JTextField aylar;
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    MaliDurumModulu frame = new MaliDurumModulu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MaliDurumModulu() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 952, 624);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 228, 196));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(5, 5, 777, 231);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblMaliDurum = new JLabel("MALİ DURUM");
        lblMaliDurum.setBounds(0, 0, 388, 29);
        lblMaliDurum.setHorizontalAlignment(SwingConstants.CENTER);
        lblMaliDurum.setForeground(Color.RED);
        lblMaliDurum.setFont(new Font("Tahoma", Font.BOLD, 24));
        panel.add(lblMaliDurum);

        JLabel lblGiderler = new JLabel("GİDERLER");
        lblGiderler.setBounds(388, 0, 388, 29);
        lblGiderler.setHorizontalAlignment(SwingConstants.CENTER);
        lblGiderler.setForeground(new Color(0, 0, 205));
        lblGiderler.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        panel.add(lblGiderler);

        JLabel lblNewLabel_1_1 = new JLabel("Personel Maaş");
        lblNewLabel_1_1.setBounds(0, 140, 388, 29);
        panel.add(lblNewLabel_1_1);

        personel_maasi = new JTextField();
        personel_maasi.setBounds(388, 140, 388, 29);
        personel_maasi.setColumns(10);
        panel.add(personel_maasi);

        JLabel lblNewLabel_1_1_1 = new JLabel("Mağaza Kirası");
        lblNewLabel_1_1_1.setBounds(0, 58, 388, 29);
        panel.add(lblNewLabel_1_1_1);

        magaza_kirasi = new JTextField();
        magaza_kirasi.setBounds(388, 58, 388, 29);
        magaza_kirasi.setColumns(10);
        panel.add(magaza_kirasi);

        JLabel lblNewLabel_1_1_1_1 = new JLabel("Faturalar");
        lblNewLabel_1_1_1_1.setBounds(0, 87, 388, 29);
        panel.add(lblNewLabel_1_1_1_1);

        faturalar = new JTextField();
        faturalar.setBounds(388, 87, 388, 29);
        faturalar.setColumns(10);
        panel.add(faturalar);

        JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Sarf Malzeme");
        lblNewLabel_1_1_1_1_1.setBounds(0, 116, 388, 29);
        panel.add(lblNewLabel_1_1_1_1_1);

        sarf_malzeme = new JTextField();
        sarf_malzeme.setBounds(388, 116, 388, 29);
        sarf_malzeme.setColumns(10);
        panel.add(sarf_malzeme);

        JLabel lblAy = new JLabel("Ay");
        lblAy.setBounds(0, 29, 388, 29);
        panel.add(lblAy);

        aylar = new JTextField();
        aylar.setColumns(10);
        aylar.setBounds(388, 29, 388, 29);
        panel.add(aylar);

        JButton btnEkle = new JButton("Ekle");
        btnEkle.setBounds(0, 192, 163, 29);
        btnEkle.setForeground(new Color(0, 128, 0));
        btnEkle.setFont(new Font("Tahoma", Font.BOLD, 12));
        panel.add(btnEkle);

        JButton btnSil = new JButton("Sil");
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Seçili satırı silmek istediğinizden emin misiniz?", "Silme Onayı", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String selectedAy = table.getValueAt(selectedRow, 1).toString(); // Silinecek ayları al
                        deleteGiderler(selectedAy); // Seçili aya göre veritabanından silme işlemi yap
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silinecek bir satır seçin.");
                }
            }
        });
        btnSil.setForeground(Color.RED);
        btnSil.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSil.setBounds(164, 192, 163, 29);
        panel.add(btnSil);

        JButton btnGuncelle = new JButton("Güncelle");
        btnGuncelle.setForeground(Color.BLUE);
        btnGuncelle.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGuncelle.setBounds(324, 192, 163, 29);
        panel.add(btnGuncelle);
        
        JButton btnRapor = new JButton("Çıktı Al");
        btnRapor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String reportContent = generateReport(); // Rapor içeriğini oluştur
                writeReportToFile("mali_durum_raporu.txt", reportContent); // Metin dosyası olarak raporu yaz
                convertReportToPDF(); // PDF olarak raporu dönüştür ve kaydet
            }
        });

        btnRapor.setBounds(682, 190, 85, 35);
        panel.add(btnRapor);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 259, 772, 289);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JButton btnAnamenu = new JButton("AnaMenü");
        btnAnamenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        btnAnamenu.setForeground(Color.RED);
        btnAnamenu.setBounds(821, 5, 114, 36);
        contentPane.add(btnAnamenu);

        // Tabloyu güncelle
        updateTable();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // Eğer bir satır seçildiyse
                        String selectedAy = table.getValueAt(selectedRow, 1).toString();
                        String selectedPersonelMaasi = table.getValueAt(selectedRow, 2).toString();
                        String selectedMagazaKirasi = table.getValueAt(selectedRow, 3).toString();
                        String selectedFaturalar = table.getValueAt(selectedRow, 4).toString();
                        String selectedSarfMalzeme = table.getValueAt(selectedRow, 5).toString();

                        aylar.setText(selectedAy);
                        personel_maasi.setText(selectedPersonelMaasi);
                        magaza_kirasi.setText(selectedMagazaKirasi);
                        faturalar.setText(selectedFaturalar);
                        sarf_malzeme.setText(selectedSarfMalzeme);
                    }
                }
            }
        });

        btnGuncelle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        double personelMaasiValue = Double.parseDouble(personel_maasi.getText());
                        double magazaKirasiValue = Double.parseDouble(magaza_kirasi.getText());
                        double faturalarValue = Double.parseDouble(faturalar.getText());
                        double sarfMalzemeValue = Double.parseDouble(sarf_malzeme.getText());
                        double aylikToplam = personelMaasiValue + magazaKirasiValue + faturalarValue + sarfMalzemeValue;

                        // Doğru sütunu kullanarak ay ID'sini al
                        String selectedAy = table.getValueAt(selectedRow, 1).toString();

                        updateGiderler(selectedAy, aylikToplam, personelMaasiValue, magazaKirasiValue, faturalarValue, sarfMalzemeValue);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen güncellenecek bir satır seçin.");
                }
            }
        });


        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double personelMaasiValue = Double.parseDouble(personel_maasi.getText());
                    double magazaKirasiValue = Double.parseDouble(magaza_kirasi.getText());
                    double faturalarValue = Double.parseDouble(faturalar.getText());
                    double sarfMalzemeValue = Double.parseDouble(sarf_malzeme.getText());

                    // Aylık toplamı hesapla
                    double aylikToplam = personelMaasiValue + magazaKirasiValue + faturalarValue + sarfMalzemeValue;

                    saveGiderler(aylikToplam, personelMaasiValue, magazaKirasiValue, faturalarValue, sarfMalzemeValue);

                    // Yeni girdi ekledikten sonra tabloyu güncelle
                    updateTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.");
                }
            }
        });
    }

    private void saveGiderler(double aylikToplam, double personelMaasi, double magazaKirasi, double faturalar, double sarfMalzeme) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement pst = con.prepareStatement("INSERT INTO mali_durum_giderler_modulu (ay_id, aylar, personel_maasi, magaza_kirasi, faturalar, sarf_malzeme, aylik_toplam) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, aylar.getText());
            pst.setDouble(2, personelMaasi);
            pst.setDouble(3, magazaKirasi);
            pst.setDouble(4, faturalar);
            pst.setDouble(5, sarfMalzeme);
            pst.setDouble(6, aylikToplam);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Giderler başarıyla kaydedildi!");
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Giderler kaydedilirken bir hata oluştu: " + ex.getMessage());
        }
    }

    private void updateTable() {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement pst = con.prepareStatement("SELECT *, personel_maasi + magaza_kirasi + faturalar + sarf_malzeme AS aylik_toplam FROM mali_durum_giderler_modulu ORDER BY ay_id ASC"); // aylara göre sıralama ekledik
            ResultSet rs = pst.executeQuery();

            // Tablo modelini oluştur
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Hücrelerin düzenlenmesini engelle
                }
            };
            table.setModel(model);

            // Sütunları ekle
            model.addColumn("Ay ID");
            model.addColumn("Aylar");
            model.addColumn("Personel Maaşı");
            model.addColumn("Mağaza Kira");
            model.addColumn("Faturalar");
            model.addColumn("Sarf Malzeme");
            model.addColumn("Aylık Toplam");

            // Verileri ekle
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("ay_id"),
                        rs.getString("aylar"),
                        rs.getDouble("personel_maasi"),
                        rs.getDouble("magaza_kirasi"),
                        rs.getDouble("faturalar"),
                        rs.getDouble("sarf_malzeme"),
                        rs.getDouble("aylik_toplam")
                });
            }

            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Tablo güncellenirken bir hata oluştu: " + ex.getMessage());
        }
    }


    private void updateGiderler(String selectedAy, double aylikToplam, double personelMaasi, double magazaKirasi, double faturalar, double sarfMalzeme) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement pst = con.prepareStatement("UPDATE mali_durum_giderler_modulu SET personel_maasi=?, magaza_kirasi=?, faturalar=?, sarf_malzeme=?, aylik_toplam=? WHERE aylar=?");
            pst.setDouble(1, personelMaasi);
            pst.setDouble(2, magazaKirasi);
            pst.setDouble(3, faturalar);
            pst.setDouble(4, sarfMalzeme);
            pst.setDouble(5, aylikToplam);
            pst.setString(6, selectedAy); // Seçili ayı kullanarak güncelleme yap
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Giderler başarıyla güncellendi!");
            updateTable();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Giderler güncellenirken bir hata oluştu: " + ex.getMessage());
        }
    }


    private void deleteGiderler(String selectedAy) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement pst = con.prepareStatement("DELETE FROM mali_durum_giderler_modulu WHERE aylar=?");
            pst.setString(1, selectedAy);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Giderler başarıyla silindi!");
            updateTable();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Giderler silinirken bir hata oluştu: " + ex.getMessage());
        }
    }

    private String filterUnsupportedCharacters(String text) {
        // Türkçe karakterleri değiştir
        text = text.replace("ı", "i");
        text = text.replace("ş", "s");
        text = text.replace("ğ", "g");
        text = text.replace("ö", "o");

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
            float cellWidth = 85;
            float cellHeight = 20;

            // Tablonun başlangıç konumunu tanımla
            float startX = 20;
            float startY = 700;

         // Fontu belirle
            PDFont font = PDType1Font.HELVETICA_BOLD;
            contentStream.setFont(font, 10);

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
            String filePath = System.getProperty("user.home") + "\\Desktop\\mali_durum_raporu.pdf";
            document.save(filePath);

            JOptionPane.showMessageDialog(null, "Rapor başarıyla PDF olarak oluşturuldu: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "PDF raporu oluştururken bir hata oluştu: " + e.getMessage());
        }
    }

}
