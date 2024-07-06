package MarketYonetim;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HaftalikCirolar extends JFrame {

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
                    HaftalikCirolar frame = new HaftalikCirolar();
                    frame.setVisible(true);
                    frame.calculateAndDisplayWeeklyTotals();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

  
    public HaftalikCirolar() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 826, 571);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblHaftalkCrolar = new JLabel("HAFTALIK CIROLAR");
        lblHaftalkCrolar.setForeground(new Color(255, 0, 0));
        lblHaftalkCrolar.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblHaftalkCrolar.setHorizontalAlignment(SwingConstants.CENTER);
        lblHaftalkCrolar.setBounds(184, 10, 373, 29);
        contentPane.add(lblHaftalkCrolar);

        JButton btnNewButton = new JButton("Ana Menü");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AnaMenu anaMenu = new AnaMenu();
                anaMenu.setVisible(true);
                dispose();
        	}
        });
        btnNewButton.setBounds(672, 10, 140, 29);
        contentPane.add(btnNewButton);

       

        model = new DefaultTableModel();
        table = new JTable(model);
     
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 650, 450);
        contentPane.add(scrollPane);
        calculateAndDisplayWeeklyTotals();
       
    }

    private void calculateAndDisplayWeeklyTotals() {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
           
            String sql = "SELECT date_trunc('week', gun_tarihi) AS hafta_baslangici, "
                       + "date_trunc('week', gun_tarihi) + INTERVAL '6 days' AS hafta_sonu, "
                       + "SUM(gun_toplami) AS hafta_toplami "
                       + "FROM gunluk_cirolar "
                       + "GROUP BY date_trunc('week', gun_tarihi)";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
               
                model.setColumnCount(0);
                model.setRowCount(0);
                
                model.addColumn("Hafta Başlangıcı");
                model.addColumn("Hafta Sonu");
                model.addColumn("Haftalık Toplam");

        
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getDate("hafta_baslangici"), rs.getDate("hafta_sonu"), rs.getDouble("hafta_toplami")});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
