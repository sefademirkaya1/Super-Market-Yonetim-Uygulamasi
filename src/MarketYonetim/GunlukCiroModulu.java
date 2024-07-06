package MarketYonetim;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDMMType1Font;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JComboBox;

public class GunlukCiroModulu extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private Connection con = null;
    private JTextField SatisMiktari;
    private JTextField birimFiyat;
    private JTextArea gunToplam;
    private JComboBox<String> comboBox;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    GunlukCiroModulu frame = new GunlukCiroModulu();
                    frame.setVisible(true);
                    frame.updateGunToplam();
                 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GunlukCiroModulu() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 920, 551);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblGnlkCiro = new JLabel("GUNLUK CIRO");
        lblGnlkCiro.setHorizontalAlignment(SwingConstants.CENTER);
        lblGnlkCiro.setForeground(Color.RED);
        lblGnlkCiro.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblGnlkCiro.setBounds(151, 10, 373, 29);
        contentPane.add(lblGnlkCiro);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(118, 152, 664, 299);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // Veritabanı bağlantısını kur
        connectToDatabase();

        // Tabloyu güncelle
        updateTable();

        JButton btnEkle = new JButton("Ekle");
        btnEkle.setForeground(new Color(0, 128, 0));
        btnEkle.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnEkle.setBounds(355, 106, 68, 36);
        contentPane.add(btnEkle);

        JButton btnGuncelle = new JButton("Güncelle");
        btnGuncelle.setForeground(Color.BLUE);
        btnGuncelle.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGuncelle.setBounds(526, 106, 112, 36);
        contentPane.add(btnGuncelle);

        JButton gunSonu = new JButton("Gün Sonu");
        gunSonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearTableAndSaveTotal(); // Yeni gün başlangıcında tabloyu temizle ve günlük toplamı kaydet
            }
        });
        gunSonu.setForeground(Color.BLUE);
        gunSonu.setFont(new Font("Tahoma", Font.BOLD, 12));
        gunSonu.setBounds(792, 365, 114, 62);
        contentPane.add(gunSonu);

        
        JButton btnSil = new JButton("Sil");
        btnSil.setForeground(Color.RED);
        btnSil.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSil.setBounds(431, 105, 85, 36);
        contentPane.add(btnSil);

        JButton btnAnamenu = new JButton("AnaMenü");
        btnAnamenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        btnAnamenu.setForeground(Color.RED);
        btnAnamenu.setBounds(792, 3, 114, 36);
        contentPane.add(btnAnamenu);

        JLabel lblNewLabel = new JLabel("Urun Adi");
        lblNewLabel.setBounds(29, 80, 79, 13);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Satis Miktari");
        lblNewLabel_1.setBounds(153, 80, 77, 13);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Birim Fiyat");
        lblNewLabel_1_1.setBounds(240, 80, 79, 13);
        contentPane.add(lblNewLabel_1_1);

        SatisMiktari = new JTextField();
        SatisMiktari.setColumns(10);
        SatisMiktari.setBounds(151, 116, 68, 19);
        contentPane.add(SatisMiktari);

        birimFiyat = new JTextField();
        birimFiyat.setColumns(10);
        birimFiyat.setBounds(238, 116, 57, 19);
        contentPane.add(birimFiyat);

        gunToplam = new JTextArea();
        gunToplam.setBounds(672, 449, 79, 55);
        contentPane.add(gunToplam);

        JLabel lblNewLabel_2 = new JLabel("Gun Toplami");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNewLabel_2.setForeground(new Color(255, 0, 0));
        lblNewLabel_2.setBounds(569, 471, 79, 19);
        contentPane.add(lblNewLabel_2);
        
        JButton btnRapor = new JButton("Çıktı Al");
        btnRapor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		String reportContent = generateReport(); // Rapor içeriğini oluştur
                writeReportToFile("gunluk_ciro_raporu.txt", reportContent); // Metin dosyası olarak raporu yaz
                convertReportToPDF(); // PDF olarak raporu dönüştür ve kaydet
        	}
        });
        btnRapor.setBounds(648, 106, 85, 35);
        contentPane.add(btnRapor);
        
        JButton gunlukCirolar = new JButton("Günlük Cirolar");
        gunlukCirolar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GunlukCirolar haftalikCirolar = new GunlukCirolar();
        		haftalikCirolar.setVisible(true);
    				dispose();
        	}
        });
        gunlukCirolar.setFont(new Font("Tahoma", Font.BOLD, 12));
        gunlukCirolar.setBounds(778, 49, 128, 36);
        contentPane.add(gunlukCirolar);
        updateGunToplam(); 
        comboBox = new JComboBox<>();
        comboBox.setBounds(29, 115, 105, 21);
        contentPane.add(comboBox);

        // JComboBox'ı doldur
        fillComboBox();
     // comboBox seçimi değiştiğinde birim fiyatı güncelle
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBirimFiyat();
            }
        });
        
      
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
      
                addRecordFromComboBox();
            }
        });  

       
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             
                int selectedRow = table.getSelectedRow();
                // Seçili satırın veri tablosundaki karşılığına eriş
                int urunId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

                // Veritabanından silme işlemi
                deleteRecord(urunId);
                // Tabloyu güncelle
                updateTable();
                // Gün toplamını güncelle
                updateGunToplam();
            }
        });

        btnGuncelle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Seçilen satırın indisini al
                int selectedRow = table.getSelectedRow();

                // Eğer hiçbir satır seçilmediyse uyarı ver ve işlemi sonlandır
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Lütfen güncellenecek bir kayıt seçin.");
                    return;
                }

                // Seçili satırın veri tablosundaki karşılığına eriş
                int urunId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                int satisMiktari = Integer.parseInt(SatisMiktari.getText());
                double birimFiyati = 0.0;
                double kazancValue = 0.0;

                try {
                    birimFiyati = Double.parseDouble(birimFiyat.getText());
                    kazancValue = satisMiktari * birimFiyati;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Birim fiyat alanına geçerli bir sayı girin.");
                    return;
                }

                // Veritabanında güncelleme işlemi
                updateRecord(urunId, satisMiktari, birimFiyati, kazancValue);
                // Tabloyu güncelle
                updateTable();
                // Gün toplamını güncelle
                updateGunToplam();
            }
        });
        // Tablo seçim olay dinleyicisi ekle
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Seçilen satırın indisini al
                int selectedRow = table.getSelectedRow();
                // Seçili satırın veri tablosundaki karşılığına eriş
                if (selectedRow >= 0) {
                    comboBox.setToolTipText(table.getValueAt(selectedRow, 1).toString());
                    SatisMiktari.setText(table.getValueAt(selectedRow, 2).toString());
                    birimFiyat.setText(table.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }
   
    private void connectToDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, password);
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı bağlantısında bir hata oluştu: " + e.getMessage());
        }
    }

   
    private void updateTable() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM gunluk_ciro_modulu");

           
            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnName(i));
            }

            // Verileri modeldeki tabloya ekle
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            // Modeli tabloya set et
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Tablo güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

   
    private void addRecord(String urun, int satisMiktari, double birimFiyati, double kazanc) {
        try {
            PreparedStatement pst = con.prepareStatement("INSERT INTO gunluk_ciro_modulu (urun_adi, satis_miktari, birim_fiyat, kazanc) VALUES (?, ?, ?, ?)");

            pst.setString(1, urun);
            pst.setInt(2, satisMiktari);
            pst.setDouble(3, birimFiyati);
            pst.setDouble(4, kazanc);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Kayıt başarıyla eklendi!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Kayıt eklenirken bir hata oluştu: " + ex.getMessage());
        }
    }

    // Veritabanından bir kaydı silmek için metot
    private void deleteRecord(int urunId) {
        try {
            PreparedStatement pst = con.prepareStatement("DELETE FROM gunluk_ciro_modulu WHERE urun_id=?");

            pst.setInt(1, urunId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Kayıt başarıyla silindi!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Kayıt silinirken bir hata oluştu: " + ex.getMessage());
        }
    }

    // Veritabanında bir kaydı güncellemek için metot
    private void updateRecord(int urunId, int satisMiktari, double birimFiyati, double kazanc) {
        try {
            PreparedStatement pst = con.prepareStatement("UPDATE gunluk_ciro_modulu SET satis_miktari=?, birim_fiyat=?, kazanc=? WHERE urun_id=?");

            pst.setInt(1, satisMiktari);
            pst.setDouble(2, birimFiyati);
            pst.setDouble(3, kazanc);
            pst.setInt(4, urunId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Kayıt başarıyla güncellendi!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Kayıt güncellenirken bir hata oluştu: " + ex.getMessage());
        }
    }

    // Günlük toplamı hesaplayan metot
    private void updateGunToplam() {
        double total = 0.0;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(kazanc) FROM gunluk_ciro_modulu");

            if (rs.next()) {
                total = rs.getDouble(1);
            }

            gunToplam.setText(String.valueOf(total));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gün toplamı hesaplanırken bir hata oluştu: " + e.getMessage());
        }
    }
 // Günlük toplamı veritabanına kaydetme metodu
    private void saveDailyTotalToDatabase() {
        double total = 0.0;

        try {
            // JTextArea'dan günlük toplamı değerini al
            double dailyTotal = Double.parseDouble(gunToplam.getText());

            // Günlük toplamı veritabanına kaydet
            PreparedStatement pst = con.prepareStatement("INSERT INTO gunluk_cirolar (gun_tarihi, gun_toplami) VALUES (CURRENT_DATE, ?)");
            pst.setDouble(1, dailyTotal);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Günlük toplam veritabanına başarıyla kaydedildi!");
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Günlük toplam veritabanına kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }

	private String filterUnsupportedCharacters(String text) {
		// Türkçe karakterleri değiştir
		text = text.replace("ı", "i");
		text = text.replace("ş", "s");
		text = text.replace("ğ", "g");
		text = text.replace("ö", "o");
		text = text.replace("İ", "I");
		text = text.replace("Ö", "O");
		text = text.replace("Ü", "U");
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
			String filePath = System.getProperty("user.home") + "\\Desktop\\gunluk_ciro_raporu.pdf";
			document.save(filePath);

			JOptionPane.showMessageDialog(null, "Rapor Masaüstüne kaydedildi: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "PDF raporu oluştururken bir hata oluştu: " + e.getMessage());
		}
	}

	public void clearTableAndSaveTotal() {
	    clearContent(); // Tabloyu temizle
	    saveDailyTotalToDatabase(); // Günlük toplamı veritabanına kaydet
	    updateTable();
	}

    // İçeriği temizleme metodu
    private void clearContent() {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM gunluk_ciro_modulu");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "İçerik temizlenirken bir hata oluştu: " + e.getMessage());
        }
    }
    private void fillComboBox() {
        try {
            // Veritabanı bağlantısını kur
            connectToDatabase();

            // Veritabanından ürün adlarını sorgula
            String query = "SELECT urun_adi FROM urun_yonetim_modulu";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // JComboBox'a ürün adlarını ekle
            while (resultSet.next()) {
                String urunAdi = resultSet.getString("urun_adi");
                comboBox.addItem(urunAdi);
            }

            // Kullanılan kaynakları kapat
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addRecordFromComboBox() {
    	try {
            String selectedProduct = comboBox.getSelectedItem().toString();
            int satisMiktari = Integer.parseInt(SatisMiktari.getText());
            double birimFiyati = Double.parseDouble(birimFiyat.getText());
            double kazancValue = satisMiktari * birimFiyati;

            // Veritabanına ekleme işlemi
            addRecord(selectedProduct, satisMiktari, birimFiyati, kazancValue);

            // Tabloyu güncelle
            updateTable();

            // Gün toplamını güncelle
            updateGunToplam();

            // Ürün miktarını güncelle
            updateProductQuantity(selectedProduct, satisMiktari);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir satış miktarı ve birim fiyat girin.");
        }
    }
    private void updateProductQuantity(String selectedProduct, int satisMiktari) {
        try {
            // Güncellenme tarihini ve saatini al
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            
            // SQL sorgusunu güncelle, karakter dizisini timestamp türüne dönüştür
            PreparedStatement pst = con.prepareStatement("UPDATE urun_yonetim_modulu SET urun_adeti = urun_adeti - ?, guncellenme_tarihi = to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS') WHERE urun_adi = ?");
            pst.setInt(1, satisMiktari);
            pst.setString(2, formattedDateTime);
            pst.setString(3, selectedProduct);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Ürün miktarı başarıyla güncellendi!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ürün miktarı güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }


    private void updateBirimFiyat() {
        try {
            String selectedProduct = comboBox.getSelectedItem().toString();
            PreparedStatement pst = con.prepareStatement("SELECT birim_fiyati FROM urun_yonetim_modulu WHERE urun_adi = ?");
            pst.setString(1, selectedProduct);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double birimFiyati = rs.getDouble("birim_fiyati");
                birimFiyat.setText(String.valueOf(birimFiyati));
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Birim fiyatı güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

}
