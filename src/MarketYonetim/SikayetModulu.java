package MarketYonetim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SikayetModulu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textAreaPersonel;
    private JTextArea textAreaOzet;
    private JTextArea textAreaTarih;
    private JTable table;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String user = "postgres";
    private String password = "sefa";
    private Connection con = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SikayetModulu frame = new SikayetModulu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SikayetModulu() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 972, 516);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(5, 5, 943, 64);
        contentPane.add(inputPanel);
        inputPanel.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("Personel Ad Soyad");
        lblNewLabel_1.setBounds(10, 11, 114, 30);
        inputPanel.add(lblNewLabel_1);

        textAreaPersonel = new JTextArea(1, 10);
        textAreaPersonel.setBounds(145, 5, 84, 36);
        inputPanel.add(textAreaPersonel);

        JLabel lblNewLabel_1_1 = new JLabel("Şikayet Özeti");
        lblNewLabel_1_1.setBounds(251, 11, 85, 30);
        inputPanel.add(lblNewLabel_1_1);

        textAreaOzet = new JTextArea(1, 10);
        textAreaOzet.setBounds(360, 5, 84, 36);
        inputPanel.add(textAreaOzet);

        JLabel lblNewLabel_1_2 = new JLabel("Şikayet Tarihi");
        lblNewLabel_1_2.setBounds(469, 11, 114, 30);
        inputPanel.add(lblNewLabel_1_2);

        textAreaTarih = new JTextArea(1, 10);
        textAreaTarih.setBounds(578, 11, 84, 36);
        inputPanel.add(textAreaTarih);

        JButton btnEkle = new JButton("Ekle");
        btnEkle.setBounds(672, 8, 65, 36);
        inputPanel.add(btnEkle);

        JButton btnSil = new JButton("Sil");
        btnSil.setForeground(new Color(255, 0, 0));
        btnSil.setBounds(747, 8, 53, 36);
        btnSil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        inputPanel.add(btnSil);
        
        JButton btnAnamen = new JButton("AnaMenü");
        btnAnamen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
        	}
        });
        btnAnamen.setForeground(Color.RED);
        btnAnamen.setBounds(810, 8, 114, 36);
        inputPanel.add(btnAnamen);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 77, 858, 402);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        
        JButton btnRapor = new JButton("Çıktı Al");
        btnRapor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 String reportContent = generateReport(); // Rapor içeriğini oluştur
                 writeReportToFile("sikayet_listesi.txt", reportContent); // Metin dosyası olarak raporu yaz
                 convertReportToPDF(); // PDF olarak raporu dönüştür ve kaydet
        	}
        });
        btnRapor.setBounds(863, 78, 85, 50);
        contentPane.add(btnRapor);

        // Veritabanı bağlantısını kur
        connectToDatabase();

        // Ekle butonuna tıklama olayını ele al
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String personel = textAreaPersonel.getText();
                String ozet = textAreaOzet.getText();
                String tarih = textAreaTarih.getText();
                addComplaint(personel, ozet, tarih); // Şikayeti veritabanına ekle
                updateTable(); // Tabloyu güncelle
            }
        });
        updateTable();
        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Silme işlemi
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) { // Eğer bir satır seçildiyse
                    int complaintID = (int) table.getValueAt(selectedRow, 0); // Şikayet ID'sini al
                    deleteComplaint(complaintID); // Şikayeti veritabanından sil
                    updateTable(); // Tabloyu güncelle
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silinecek bir satır seçin.");
                }
            }
        });
    }

    // Şikayet silme metodu
    private void deleteComplaint(int complaintID) {
        String sql = "DELETE FROM sikayet_modulu WHERE sikayet_id = ?";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, complaintID);
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Şikayet başarıyla silindi!");
            } else {
                JOptionPane.showMessageDialog(null, "Şikayet silinirken bir hata oluştu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Şikayet silinirken bir hata oluştu: " + e.getMessage());
        }
    }


    // Veritabanı bağlantısını kurma 
    private void connectToDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı bağlantısında bir hata oluştu: " + e.getMessage());
        }
    }

    // Tabloyu güncelleyen metod
    private void updateTable() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sikayet_modulu");

            // ResultSet'ten verileri alarak bir TableModel oluştur
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

    // Şikayet ekleme metodu
 // Şikayet ekleme metodu
    private void addComplaint(String personel, String ozet, String tarih) {
        String sql = "INSERT INTO sikayet_modulu (\"p_ad_soyad\", \"sikayet_ozeti\", \"sikayet_tarihi\") VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, personel);
            pst.setString(2, ozet);
            pst.setString(3, tarih);
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    JOptionPane.showMessageDialog(null, "Şikayet başarıyla eklendi! Eklenen ID: " + id);
                }
                updateTable(); // Tabloyu güncelle
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Şikayet eklenirken bir hata oluştu: " + e.getMessage());
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
			float cellWidth = 90;
			float cellHeight = 20;

			// Tablonun başlangıç konumunu tanımla
			float startX = 35;
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
				float xCoordinate = 35; // Yeni bir satır başladığında X koordinatını başa al

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
			String filePath = System.getProperty("user.home") + "\\Desktop\\sikayet_listesi.pdf";
			document.save(filePath);

			JOptionPane.showMessageDialog(null, "Rapor Masaüstüne kaydedildi: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "PDF raporu oluştururken bir hata oluştu: " + e.getMessage());
		}
	}
}
