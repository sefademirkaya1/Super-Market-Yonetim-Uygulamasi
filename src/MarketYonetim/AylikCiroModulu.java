package MarketYonetim;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.*;
import java.awt.Toolkit;
import com.formdev.flatlaf.FlatLightLaf;

public class AylikCiroModulu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private JTable table;
    private DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // FlatLaf temasını ayarla
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    AylikCiroModulu frame = new AylikCiroModulu();
                    frame.setVisible(true);
                    frame.calculateAndDisplayMonthlyTotals();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AylikCiroModulu() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 749, 509);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 240));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblAylikCirolar = new JLabel("AYLIK CİROLAR");
        lblAylikCirolar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAylikCirolar.setForeground(Color.RED);
        lblAylikCirolar.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAylikCirolar.setBounds(168, 10, 373, 29);
        contentPane.add(lblAylikCirolar);

        JButton btnNewButton = new JButton("Ana Menü");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
            }
        });
        btnNewButton.setBounds(585, 16, 140, 29);
        contentPane.add(btnNewButton);

        // Model for JTable
        model = new DefaultTableModel();
        table = new JTable(model);
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 650, 450);
        contentPane.add(scrollPane);
        calculateAndDisplayMonthlyTotals();
    }

    private void calculateAndDisplayMonthlyTotals() {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Query to calculate monthly totals
            String sql = "SELECT date_trunc('month', gun_tarihi) AS ay_baslangici, "
                    + "date_trunc('month', gun_tarihi) + INTERVAL '1 month - 1 day' AS ay_sonu, "
                    + "SUM(gun_toplami) AS ay_toplami "
                    + "FROM gunluk_cirolar "
                    + "GROUP BY date_trunc('month', gun_tarihi)";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Tabloyu temizle
                model.setColumnCount(0);
                model.setRowCount(0);
                // Tabloya sütun ekle
                model.addColumn("Ay Başlangıcı");
                model.addColumn("Ay Sonu");
                model.addColumn("Aylık Toplam");

                // Satırları tablo modeline ekle
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getDate("ay_baslangici"), rs.getDate("ay_sonu"), rs.getDouble("ay_toplami")});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
