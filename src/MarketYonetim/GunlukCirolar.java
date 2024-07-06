package MarketYonetim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GunlukCirolar extends JFrame {

    private static final long serialVersionUID = 1L;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private JPanel contentPane;
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    GunlukCirolar frame = new GunlukCirolar();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GunlukCirolar() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 803, 502);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblHaftalikCro = new JLabel("GUNLUK CIROLAR");
        lblHaftalikCro.setBounds(5, 5, 779, 29);
        lblHaftalikCro.setHorizontalAlignment(SwingConstants.CENTER);
        lblHaftalikCro.setForeground(Color.RED);
        lblHaftalikCro.setFont(new Font("Tahoma", Font.BOLD, 24));
        contentPane.add(lblHaftalikCro);

        JButton btnNewButton_1_1_1_2_1 = new JButton("Ana Menu");
        btnNewButton_1_1_1_2_1.setBounds(635, 5, 154, 21);
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

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(51, 73, 665, 333);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // Veritabanından verileri çek ve tabloya ekle
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT gunler_id, gun_tarihi, gun_toplami FROM gunluk_cirolar");

            // Tablo modeli oluştur
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Günler ID");
            model.addColumn("Gün Tarihi");
            model.addColumn("Gün Toplamı");

            // Verileri tabloya ekle
            while (resultSet.next()) {
                Object[] row = {resultSet.getInt("gunler_id"), resultSet.getDate("gun_tarihi"), resultSet.getDouble("gun_toplami")};
                model.addRow(row);
            }

            // Tabloya modeli ekle
            table.setModel(model);

            // Bağlantıyı kapat
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
