package MarketYonetim;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class HaftalikCiroModulu extends JFrame {

    private static final long serialVersionUID = 1L;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private JPanel contentPane;
    private JTextField pazartesi;
    private JTextField sali;
    private JTextField carsamba;
    private JTextField persembe;
    private JTextField cuma;
    private JTextField cumartesi;
    private JTextField pazar;
    private JTable table;
    private JTextArea haftaToplam;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HaftalikCiroModulu frame = new HaftalikCiroModulu();
                    frame.setVisible(true);
                    frame.updateTable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public HaftalikCiroModulu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 889, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblHaftalkCiro = new JLabel("HAFTALIK CİRO");
        lblHaftalkCiro.setHorizontalAlignment(SwingConstants.CENTER);
        lblHaftalkCiro.setForeground(Color.RED);
        lblHaftalkCiro.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblHaftalkCiro.setBounds(220, 10, 251, 29);
        contentPane.add(lblHaftalkCiro);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(31, 167, 609, 161);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JLabel lblNewLabel = new JLabel("HAFTA TOPLAM CİRO");
        lblNewLabel.setBounds(420, 357, 138, 13);
        contentPane.add(lblNewLabel);

        JButton btnNewButton = new JButton("Ana Menu");
        btnNewButton.setBackground(UIManager.getColor("Button.highlight"));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        
        
        
        btnNewButton.setBounds(762, 12, 103, 37);
        contentPane.add(btnNewButton);

        JLabel lblPazartesi = new JLabel("Pazartesi");
        lblPazartesi.setBounds(48, 54, 59, 13);
        contentPane.add(lblPazartesi);

        JLabel lblSal = new JLabel("Salı");
        lblSal.setBounds(113, 54, 45, 13);
        contentPane.add(lblSal);

        JLabel lblaramba = new JLabel("Çarşamba");
        lblaramba.setBounds(181, 54, 59, 13);
        contentPane.add(lblaramba);

        JLabel lblPerembe = new JLabel("Perşembe");
        lblPerembe.setBounds(250, 54, 62, 13);
        contentPane.add(lblPerembe);

        JLabel lblCuma = new JLabel("Cuma");
        lblCuma.setBounds(322, 54, 45, 13);
        contentPane.add(lblCuma);

        JLabel lblCumartesi = new JLabel("Cumartesi");
        lblCumartesi.setBounds(397, 54, 56, 13);
        contentPane.add(lblCumartesi);

        JLabel lblPazar = new JLabel("Pazar");
        lblPazar.setBounds(463, 54, 45, 13);
        contentPane.add(lblPazar);

        pazartesi = new JTextField();
        pazartesi.setBounds(48, 80, 45, 19);
        contentPane.add(pazartesi);
        pazartesi.setColumns(10);

        sali = new JTextField();
        sali.setColumns(10);
        sali.setBounds(113, 80, 45, 19);
        contentPane.add(sali);

        carsamba = new JTextField();
        carsamba.setColumns(10);
        carsamba.setBounds(181, 80, 45, 19);
        contentPane.add(carsamba);

        persembe = new JTextField();
        persembe.setColumns(10);
        persembe.setBounds(250, 80, 45, 19);
        contentPane.add(persembe);

        cuma = new JTextField();
        cuma.setColumns(10);
        cuma.setBounds(322, 80, 45, 19);
        contentPane.add(cuma);

        cumartesi = new JTextField();
        cumartesi.setColumns(10);
        cumartesi.setBounds(397, 80, 45, 19);
        contentPane.add(cumartesi);

        pazar = new JTextField();
        pazar.setColumns(10);
        pazar.setBounds(463, 80, 45, 19);
        contentPane.add(pazar);

        haftaToplam = new JTextArea();
        haftaToplam.setBounds(579, 351, 79, 55);
        contentPane.add(haftaToplam);
        
        updateTable();// başka class dan bu uygulamaya geçince tablonun görünmesi için
        
        JButton btnEkle = new JButton("Ekle");
        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Girilen verileri al
                try {
                    // Veritabanına ekleme işlemi
                    try {
                        Connection con = DriverManager.getConnection(url, username, password);
                        PreparedStatement pst = con.prepareStatement(
                                "INSERT INTO haftalik_ciro_modulu (gunler, gun_toplamlari) VALUES (?, ?)");

                        if (!pazartesi.getText().isEmpty()) {
                            pst.setString(1, "Pazartesi");
                            pst.setInt(2, Integer.parseInt(pazartesi.getText()));
                            pst.executeUpdate();
                        }
                        if (!sali.getText().isEmpty()) {
                            pst.setString(1, "Salı");
                            pst.setInt(2, Integer.parseInt(sali.getText()));
                            pst.executeUpdate();
                        }
                        if (!carsamba.getText().isEmpty()) {
                            pst.setString(1, "Çarşamba");
                            pst.setInt(2, Integer.parseInt(carsamba.getText()));
                            pst.executeUpdate();
                        }
                        if (!persembe.getText().isEmpty()) {
                            pst.setString(1, "Perşembe");
                            pst.setInt(2, Integer.parseInt(persembe.getText()));
                            pst.executeUpdate();
                        }
                        if (!cuma.getText().isEmpty()) {
                            pst.setString(1, "Cuma");
                            pst.setInt(2, Integer.parseInt(cuma.getText()));
                            pst.executeUpdate();
                        }
                        if (!cumartesi.getText().isEmpty()) {
                            pst.setString(1, "Cumartesi");
                            pst.setInt(2, Integer.parseInt(cumartesi.getText()));
                            pst.executeUpdate();
                        }
                        if (!pazar.getText().isEmpty()) {
                            pst.setString(1, "Pazar");
                            pst.setInt(2, Integer.parseInt(pazar.getText()));
                            pst.executeUpdate();
                        }

                        con.close();

                        JOptionPane.showMessageDialog(null, "Haftalık ciroya yeni veriler başarıyla eklendi.");

                        // Tabloyu güncelle
                        updateTable();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Haftalık ciroya veri eklenirken bir hata oluştu: " + ex.getMessage());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.");
                }
            }
        });

        btnEkle.setForeground(new Color(0, 128, 0));
        btnEkle.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnEkle.setBounds(546, 74, 163, 29);
        contentPane.add(btnEkle);
    }

    private void updateTable() {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM haftalik_ciro_modulu");

            // Verileri modeldeki tabloya ekle
            int haftaToplamCiro = 0; // Hafta toplamını tutacak değişken
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Gün");
            model.addColumn("Toplam Ciro");
            
            // Verileri modeldeki tabloya ekle
            while (rs.next()) {
                String gun = rs.getString("gunler");
                int gunToplam = rs.getInt("gun_toplamlari");
                
                model.addRow(new Object[]{gun, gunToplam});
                
                haftaToplamCiro += gunToplam; // Toplamı hesapla
            }

            // Modeli tabloya set et
            table.setModel(model);
            con.close();
            
            // Hafta toplamını JTextArea'ya yazdır
            haftaToplam.setText(Integer.toString(haftaToplamCiro));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Tablo güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

}
