package MarketYonetim;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Canvas;

public class LoginWindowMarket extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField UserTextField;
    private JPasswordField passwordField;

    private String url = "jdbc:postgresql://localhost:5432/marketyonetim";
    private String username = "postgres";
    private String password = "sefa";
    private Connection con = null;
    private JTextField textField_1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginWindowMarket frame = new LoginWindowMarket();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginWindowMarket() {
       	setIconImage(Toolkit.getDefaultToolkit().getImage("src/MarketYonetim/marketlogo.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 560, 476);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 250, 250));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Kullanıcı Adı");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel.setForeground(new Color(255, 0, 0));
        lblNewLabel.setBounds(49, 40, 136, 38);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Şifre");
        lblNewLabel_1.setForeground(new Color(255, 0, 0));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel_1.setBounds(49, 88, 80, 38);
        contentPane.add(lblNewLabel_1);

        UserTextField = new JTextField();
        UserTextField.setColumns(10);
        UserTextField.setBounds(170, 52, 96, 19);
        contentPane.add(UserTextField);

        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordField.setBounds(170, 100, 96, 19);
        contentPane.add(passwordField);

        JButton btnNewButton = new JButton("Giriş");
        btnNewButton.setForeground(new Color(0, 128, 0));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnNewButton.setBounds(170, 155, 96, 44);
        contentPane.add(btnNewButton);
        
     
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = UserTextField.getText();
                String enteredPassword = new String(passwordField.getPassword());
                try {
                    con = DriverManager.getConnection(url, username, password);
                    PreparedStatement pst = con.prepareStatement("select * from t_user where username=? and password=?");
                    pst.setString(1, enteredUsername);
                    pst.setString(2, enteredPassword);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Hoş geldiniz " + enteredUsername + "!");
                        openMainMenu(); // Ana menüyü aç
                        dispose(); // Giriş ekranını kapat
                    } else {
                        JOptionPane.showMessageDialog(null, "Hatalı kullanıcı adı veya şifre!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage());
                }
            }
        });
    }

    private void openMainMenu() {
        AnaMenu anaMenu = new AnaMenu();
        anaMenu.setVisible(true);
    }
}
