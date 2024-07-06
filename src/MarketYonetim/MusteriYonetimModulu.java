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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class MusteriYonetimModulu extends JFrame {

	private static final long serialVersionUID = 1L;
	private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
	private String user = "postgres";
	private String password = "sefa";
	private Connection con = null;
	private JPanel contentPane;
	private JTextField musteriAdSoyad;
	private JTextField musteriTelNo;
	private JScrollPane scrollPane;
	private JButton btnEkle;
	private JTable table;
	private JTextField musteriEmail;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

					MusteriYonetimModulu frame = new MusteriYonetimModulu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MusteriYonetimModulu() {
	   	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 880, 486);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 250, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblMteriYnetim = new JLabel("MÜŞTERİ YÖNETİM");
		lblMteriYnetim.setBounds(175, 10, 297, 29);
		lblMteriYnetim.setHorizontalAlignment(SwingConstants.CENTER);
		lblMteriYnetim.setForeground(Color.RED);
		lblMteriYnetim.setFont(new Font("Tahoma", Font.BOLD, 24));
		contentPane.add(lblMteriYnetim);

		JLabel lblNewLabel_1_1 = new JLabel("Müşteri Ad Soyad");
		lblNewLabel_1_1.setBounds(10, 131, 116, 29);
		contentPane.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel(" Tel No");
		lblNewLabel_1_1_1.setBounds(118, 131, 91, 29);
		contentPane.add(lblNewLabel_1_1_1);

		musteriTelNo = new JTextField();
		musteriTelNo.setBounds(118, 171, 68, 19);
		contentPane.add(musteriTelNo);
		musteriTelNo.setColumns(10);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 229, 533, 186);
		contentPane.add(scrollPane);

		btnEkle = new JButton("Ekle");
		btnEkle.setBounds(357, 140, 84, 49);
		btnEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String adSoyad = musteriAdSoyad.getText();
				String telNo = musteriTelNo.getText();
				String eMail = musteriEmail.getText();


				// Veritabanına ekleme işlemi
				try {
					Connection con = DriverManager.getConnection(url, user, password);
					PreparedStatement pst = con.prepareStatement(
							"INSERT INTO musteri_yonetim_modulu (musteri_adsoyad, musteri_telno, email) VALUES (?, ?, ?)");
					pst.setString(1, adSoyad);
					pst.setLong(2, Long.parseLong(telNo));
					pst.setString(3, eMail);
					pst.executeUpdate();
					con.close();

					// Tabloyu güncelle
					updateTable();
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Müşteri eklenirken bir hata oluştu: " + ex.getMessage());
				}
			}
		});
		btnEkle.setForeground(new Color(0, 128, 0));
		btnEkle.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(btnEkle);

		JButton btnSil = new JButton("Sil");
		btnSil.setBounds(451, 141, 84, 49);
		btnSil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Seçili satırın indeksini al
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Lütfen silinecek bir satır seçin.");
					return;
				}

				// Seçili satırın müşteri ID'sini al
				int musteriID = (int) table.getValueAt(selectedRow, 0);

				// Veritabanından seçilen müşteriyi sil
				try {
					Connection con = DriverManager.getConnection(url, user, password);
					PreparedStatement pst = con
							.prepareStatement("DELETE FROM musteri_yonetim_modulu WHERE musteri_id = ?");
					pst.setInt(1, musteriID);
					pst.executeUpdate();
					con.close();

					// Tabloyu güncelle
					updateTable();
					JOptionPane.showMessageDialog(null, "Müşteri başarıyla silindi.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Müşteri silinirken bir hata oluştu: " + ex.getMessage());
				}
			}
		});

		btnSil.setForeground(Color.RED);
		btnSil.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnSil);

		JButton btnGuncelle = new JButton("Güncelle");
		btnGuncelle.setBounds(545, 141, 91, 49);

		btnGuncelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Seçili satırın indeksini al
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Lütfen güncellenecek bir satır seçin.");
					return;
				}

				// Seçili satırın müşteri ID'sini al
				int musteriID = (int) table.getValueAt(selectedRow, 0);
				String adSoyad = musteriAdSoyad.getText();
				String telNo = musteriTelNo.getText();
				String eMail = musteriEmail.getText();
				// Veritabanında seçilen müşterinin bilgilerini güncelle
				try {
					Connection con = DriverManager.getConnection(url, user, password);
					PreparedStatement pst = con.prepareStatement(
							"UPDATE musteri_yonetim_modulu SET musteri_adsoyad = ?, musteri_telno = ?, email= ? WHERE musteri_id = ?");
					pst.setString(1, adSoyad);
					pst.setLong(2, Long.parseLong(telNo));
					pst.setString(3, eMail);
					pst.setInt(4, musteriID);
					
					pst.executeUpdate();
					con.close();

					// Tabloyu güncelle
					updateTable();
					JOptionPane.showMessageDialog(null, "Müşteri başarıyla güncellendi.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Müşteri güncellenirken bir hata oluştu: " + ex.getMessage());
				}
			}
		});

		btnGuncelle.setForeground(Color.BLUE);
		btnGuncelle.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnGuncelle);

		btnGuncelle.setForeground(Color.BLUE);
		btnGuncelle.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnGuncelle);

		musteriAdSoyad = new JTextField();
		musteriAdSoyad.setBounds(20, 171, 68, 19);
		musteriAdSoyad.setColumns(10);
		contentPane.add(musteriAdSoyad);

		// Tabloyu oluştur
		table = new JTable();
		scrollPane.setViewportView(table);

		JButton btnNewButton = new JButton("Ana Menu");
		btnNewButton.setBounds(595, 2, 103, 37);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnaMenu anaMenu = new AnaMenu();
				anaMenu.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton.setBackground(new Color(255, 0, 255));
		contentPane.add(btnNewButton);

		JButton btnRapor = new JButton("Çıktı Al");
		btnRapor.setBounds(597, 404, 85, 35);
		btnRapor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String reportContent = generateReport(); // Rapor içeriğini oluştur
	                writeReportToFile("musteri_listesi.txt", reportContent); // Metin dosyası olarak raporu yaz
	                convertReportToPDF(); // PDF olarak raporu dönüştür ve kaydet
			}
		});
		contentPane.add(btnRapor);
		
		musteriEmail = new JTextField();
		musteriEmail.setColumns(10);
		musteriEmail.setBounds(213, 171, 68, 19);
		contentPane.add(musteriEmail);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("E-MAIL");
		lblNewLabel_1_1_1_1.setBounds(213, 131, 91, 29);
		contentPane.add(lblNewLabel_1_1_1_1);
		
		JButton btnMail = new JButton("Musteri Duyuru");
		btnMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  MusteriDuyuru musteriDuyuru = new MusteriDuyuru();
				  musteriDuyuru.setVisible(true);
	                dispose();
			}
		});
		btnMail.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMail.setForeground(new Color(0, 128, 0));
		btnMail.setBackground(new Color(255, 255, 255));
		btnMail.setBounds(684, 135, 131, 55);
		contentPane.add(btnMail);

		// Tabloyu başlangıçta güncelle
		updateTable();
	}

	// Tabloyu güncelle
	private void updateTable() {
		try {
			Connection con = DriverManager.getConnection(url, user, password);
			PreparedStatement pst = con.prepareStatement("SELECT * FROM musteri_yonetim_modulu ORDER BY musteri_id ASC");
			ResultSet rs = pst.executeQuery();

			// Tablo modelini oluştur
			DefaultTableModel model = new DefaultTableModel();
			table.setModel(model);

			// Sütunları ekle
			model.addColumn("Müşteri ID");
			model.addColumn("Müşteri Ad Soyad");
			model.addColumn("Müşteri Tel No");
			model.addColumn("Müşteri E Mail");

			// Verileri ekle
			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("musteri_id"), rs.getString("musteri_adsoyad"),
						rs.getString("musteri_telno"),	rs.getString("email") });
			}

			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Tablo güncellenirken bir hata oluştu: " + ex.getMessage());
		}
		// Tabloya bir ListSelectionListener ekle
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				// Seçili satırın indeksini al
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) { // Eğer bir satır seçildiyse
					// Seçili satırın müşteri ID'sini al
					int musteriID = (int) table.getValueAt(selectedRow, 0);

					// Veritabanından ilgili müşterinin bilgilerini al ve JTextField alanlarına
					// yerleştir
					try {
						Connection con = DriverManager.getConnection(url, user, password);
						PreparedStatement pst = con
								.prepareStatement("SELECT * FROM musteri_yonetim_modulu WHERE musteri_id = ?");
						pst.setInt(1, musteriID);
						ResultSet rs = pst.executeQuery();

						if (rs.next()) {
							musteriAdSoyad.setText(rs.getString("musteri_adsoyad"));
							musteriTelNo.setText(rs.getString("musteri_telno"));
							musteriEmail.setText(rs.getString("email"));
						}

						con.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null,
								"Müşteri bilgileri alınırken bir hata oluştu: " + ex.getMessage());
					}
				}
			}

		});

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

			// JTextArea içindeki "GunToplam" alanını PDF'e ekle
			
			contentStream.beginText();
			contentStream.newLineAtOffset(50, yCoordinate - cellHeight);

			contentStream.endText();

			// İçerik akışını kapat
			contentStream.close();

			// PDF dosyasını kaydet
			String filePath = System.getProperty("user.home") + "\\Desktop\\musteri_listesi.pdf";
			document.save(filePath);

			JOptionPane.showMessageDialog(null, "Rapor Masaüstüne kaydedildi: " + filePath);
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
	       
	                return new PasswordAuthentication("sd.demirrkaya0606@gmail.com", "cpln zjjv xdwd uoqg");
	            }
	        });

	        try {
	            Message message = new MimeMessage(session);
	            session.setDebug(true);

	            
	            message.setFrom(new InternetAddress("sd.demirrkaya0606@gmail.com"));
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
