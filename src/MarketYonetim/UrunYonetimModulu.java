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
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UrunYonetimModulu extends JFrame {

    private static final long serialVersionUID = 1L;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String user = "postgres";
    private String password = "sefa";
    private Connection con = null;
    private JPanel contentPane;
    private JTextField urunAdi;
    private JTextField urunAdeti;
    private JTextField birimFiyati;
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    UrunYonetimModulu frame = new UrunYonetimModulu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UrunYonetimModulu() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 832, 580);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 245, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("ÜRÜN YÖNETİM EKRANI");
        lblNewLabel.setBounds(199, 20, 297, 29);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.RED);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        contentPane.add(lblNewLabel);

        urunAdi = new JTextField();
        urunAdi.setBounds(40, 153, 96, 19);
        urunAdi.setColumns(10);
        contentPane.add(urunAdi);

        JLabel urunAdilbl = new JLabel("Ürün Adı");
        urunAdilbl.setBounds(40, 92, 91, 29);
        contentPane.add(urunAdilbl);

        urunAdeti = new JTextField();
        urunAdeti.setBounds(139, 153, 96, 19);
        urunAdeti.setColumns(10);
        contentPane.add(urunAdeti);

        JLabel urunAdetlbl = new JLabel("Adet");
        urunAdetlbl.setBounds(139, 92, 91, 29);
        contentPane.add(urunAdetlbl);

        birimFiyati = new JTextField();
        birimFiyati.setBounds(245, 153, 96, 19);
        birimFiyati.setColumns(10);
        contentPane.add(birimFiyati);

        JLabel urunBirimFiyatlbl = new JLabel("Birim Fiyat");
        urunBirimFiyatlbl.setBounds(240, 92, 91, 29);
        contentPane.add(urunBirimFiyatlbl);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(29, 234, 697, 215);
        contentPane.add(scrollPane_1);

        JButton btnEkle = new JButton("Ekle");
        btnEkle.setBounds(403, 147, 85, 29);
        btnEkle.setForeground(new Color(0, 128, 0));
        btnEkle.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPane.add(btnEkle);

        JButton btnSil = new JButton("Sil");
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Seçilen satırın indeksini al
                    int selectedRowIndex = table.getSelectedRow();
                    
                    // Seçili bir satır olup olmadığını kontrol et
                    if (selectedRowIndex != -1) {
                        // Veritabanı bağlantısını oluştur
                        Connection con = DriverManager.getConnection(url, user, password);

                        // Silinecek ürünün stok_urun_id'sini al
                        int selectedProductId = (int) table.getValueAt(selectedRowIndex, 0);

                        // Sorguyu hazırla
                        String query = "DELETE FROM urun_yonetim_modulu WHERE stok_urun_id = ?";

                        // Sorguyu hazırla ve parametreleri set et
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.setInt(1, selectedProductId);

                        // Sorguyu çalıştır
                        pst.executeUpdate();

                        // Bağlantıyı kapat
                        con.close();

                        // Silme işlemi başarılı mesajı
                        JOptionPane.showMessageDialog(null, "Ürün başarıyla silindi.");

                        // Tabloyu güncelle
                        updateTable();
                    } else {
                        // Eğer bir satır seçilmemişse hata mesajı göster
                        JOptionPane.showMessageDialog(null, "Lütfen silinecek bir ürün seçin.");
                    }
                } catch (SQLException ex) {
                    // Hata durumunda hata mesajı
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ürün silinirken bir hata oluştu: " + ex.getMessage());
                }
            }
        });

        btnSil.setBounds(519, 147, 85, 29);
        btnSil.setForeground(Color.RED);
        btnSil.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPane.add(btnSil);

        JButton btnGuncelle = new JButton("Güncelle");
        btnGuncelle.setBounds(627, 147, 91, 29);
        btnGuncelle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Seçilen satırın indeksini al
                    int selectedRowIndex = table.getSelectedRow();
                    
                    // Seçili bir satır olup olmadığını kontrol et
                    if (selectedRowIndex != -1) {
                        // Veritabanı bağlantısını oluştur
                        Connection con = DriverManager.getConnection(url, user, password);

                        // Güncellenecek ürünün stok_urun_id'sini al
                        int selectedProductId = (int) table.getValueAt(selectedRowIndex, 0);

                        // Sorguyu hazırla
                        String query = "UPDATE urun_yonetim_modulu SET urun_adi = ?, urun_adeti = ?, birim_fiyati = ?, guncellenme_tarihi = ? WHERE stok_urun_id = ?";

                        // Sorguyu hazırla ve parametreleri set et
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.setString(1, urunAdi.getText());
                        pst.setInt(2, Integer.parseInt(urunAdeti.getText()));
                        pst.setDouble(3, Double.parseDouble(birimFiyati.getText()));
                        pst.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        pst.setInt(5, selectedProductId);

                        // Sorguyu çalıştır
                        pst.executeUpdate();

                        // Bağlantıyı kapat
                        con.close();

                        // Güncelleme işlemi başarılı mesajı
                        JOptionPane.showMessageDialog(null, "Ürün başarıyla güncellendi.");

                        // Tabloyu güncelle
                        updateTable();
                    } else {
                        // Eğer bir satır seçilmemişse hata mesajı göster
                        JOptionPane.showMessageDialog(null, "Lütfen güncellenecek bir ürün seçin.");
                    }
                } catch (SQLException ex) {
                    // Hata durumunda hata mesajı
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ürün güncellenirken bir hata oluştu: " + ex.getMessage());
                }
            }
        });

        btnGuncelle.setForeground(new Color(0, 0, 255));
        btnGuncelle.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPane.add(btnGuncelle);

        JButton btnNewButton_1_1_1_2_1 = new JButton("Ana Menu");
        btnNewButton_1_1_1_2_1.setBounds(717, 10, 91, 39);
        btnNewButton_1_1_1_2_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1_1_1_2_1.setForeground(Color.BLACK);
        btnNewButton_1_1_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 10));
        contentPane.add(btnNewButton_1_1_1_2_1);

        // Tablo oluştur
        table = new JTable();
        scrollPane_1.setViewportView(table);
        
        JButton btnRapor = new JButton("Çıktı Al");
        btnRapor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 String reportContent = generateReport(); // Rapor içeriğini oluştur
                 writeReportToFile("urun_yonetim_listesi.txt", reportContent); // Metin dosyası olarak raporu yaz
                 convertReportToPDF(); // PDF olarak raporu dönüştür ve kaydet
        	}
        });
        btnRapor.setBounds(717, 476, 85, 35);
        contentPane.add(btnRapor);

        // Tabloyu güncelle
        updateTable();

        // Buton için ActionListener ekleme
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Veritabanı bağlantısını oluştur
                    Connection con = DriverManager.getConnection(url, user, password);

                    // Sorguyu hazırla
                    String query = "INSERT INTO urun_yonetim_modulu (urun_adi, urun_adeti, birim_fiyati, guncellenme_tarihi) VALUES (?, ?, ?, ?)";

                    // Sorguyu hazırla ve parametreleri set et
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, urunAdi.getText());
                    pst.setInt(2, Integer.parseInt(urunAdeti.getText()));
                    pst.setDouble(3, Double.parseDouble(birimFiyati.getText()));

                    // Şu anki tarihi al
                    Timestamp guncellemeTarihi = new Timestamp(System.currentTimeMillis());
                    pst.setTimestamp(4, guncellemeTarihi);

                    // Sorguyu çalıştır
                    pst.executeUpdate();

                    // Bağlantıyı kapat
                    con.close();

                    // Ekleme işlemi başarılı mesajı
                    JOptionPane.showMessageDialog(null, "Ürün başarıyla eklendi.");

                    // Tabloyu güncelle
                    updateTable();
                } catch (SQLException ex) {
                    // Hata durumunda hata mesajı
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ürün eklenirken bir hata oluştu: " + ex.getMessage());
                }
            }
        });

        // Tabloya ListSelectionListener ekleme
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // Satır seçilmiş mi kontrolü
                        // Seçilen satırın verilerini al
                        String selectedProductName = table.getValueAt(selectedRow, 1).toString();
                        String selectedProductAmount = table.getValueAt(selectedRow, 2).toString();
                        String selectedProductPrice = table.getValueAt(selectedRow, 3).toString();

                        // Alınan verileri ilgili metin alanlarına yerleştir
                        urunAdi.setText(selectedProductName);
                        urunAdeti.setText(selectedProductAmount);
                        birimFiyati.setText(selectedProductPrice);
                    }
                }
            }
        });
    }

    // Tabloyu güncelleyen yöntem
    private void updateTable() {
        try {
            // Veritabanı bağlantısını oluştur
            Connection con = DriverManager.getConnection(url, user, password);

            // Sorguyu hazırla
            String query = "SELECT stok_urun_id, urun_adi, urun_adeti, birim_fiyati, guncellenme_tarihi FROM urun_yonetim_modulu ORDER BY stok_urun_id";

            // Sorguyu hazırla ve parametreleri set et
            PreparedStatement pst = con.prepareStatement(query);

            // Sonucu al
            ResultSet rs = pst.executeQuery();

            // Tablo modeli oluştur
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Stok Ürün ID");
            model.addColumn("Ürün Adı");
            model.addColumn("Adet");
            model.addColumn("Birim Fiyat");
            model.addColumn("Güncelleme Tarihi");

            // ResultSet'ten verileri al ve tabloya ekle
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("stok_urun_id"),
                    rs.getString("urun_adi"),
                    rs.getInt("urun_adeti"),
                    rs.getDouble("birim_fiyati"),
                    rs.getTimestamp("guncellenme_tarihi")
                };
                model.addRow(row);
            }

            // Tabloyu güncelle
            table.setModel(model);

            // Bağlantıyı kapat
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

		
			contentStream.beginText();
			contentStream.newLineAtOffset(50, yCoordinate - cellHeight);

			contentStream.endText();

			// İçerik akışını kapat
			contentStream.close();

			// PDF dosyasını kaydet
			String filePath = System.getProperty("user.home") + "\\Desktop\\urun_yonetim_listesi.pdf";
			document.save(filePath);

			JOptionPane.showMessageDialog(null, "Rapor Masaüstüne kaydedildi: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "PDF raporu oluştururken bir hata oluştu: " + e.getMessage());
		}
	}
}
