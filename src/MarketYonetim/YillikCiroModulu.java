package MarketYonetim;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.sql.*;

public class YillikCiroModulu extends JFrame {

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
                    UIManager.setLookAndFeel(new FlatLightLaf());

                    YillikCiroModulu frame = new YillikCiroModulu();
                    frame.setVisible(true);
                    frame.calculateAndDisplayYearsTotals();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public YillikCiroModulu() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 791, 610);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblyillikCirolar = new JLabel("YILLIK CİROLAR");
        lblyillikCirolar.setHorizontalAlignment(SwingConstants.CENTER);
        lblyillikCirolar.setForeground(Color.RED);
        lblyillikCirolar.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblyillikCirolar.setBounds(165, 10, 373, 44);
        contentPane.add(lblyillikCirolar);
        
        JButton btnNewButton = new JButton("Ana Menü");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
        	}
        });
        btnNewButton.setBounds(637, 12, 140, 29);
        contentPane.add(btnNewButton);
        
      
        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 650, 450);
        contentPane.add(scrollPane);
        calculateAndDisplayYearsTotals();
     
        
    }
    private void calculateAndDisplayYearsTotals() {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
           
            String sql = "SELECT date_trunc('year', gun_tarihi) AS yil, "
                       + "SUM(gun_toplami) AS yillik_toplam "
                       + "FROM gunluk_cirolar "
                       + "GROUP BY date_trunc('year', gun_tarihi)";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // 
                model.setColumnCount(0);
                model.setRowCount(0);
     
                model.addColumn("Yıl");
                model.addColumn("Yıllık Toplam");

                // Modelde ki tabloya satır ekle
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getDate("yil"), rs.getDouble("yillik_toplam")});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
