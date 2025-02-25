import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                agency = getAuthenticatedUser(email, password);

                if (agency != null){
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                    "Email or password invalid",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public Agency agency;

    private Agency getAuthenticatedUser(String email, String password){
        Agency agency = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/login";
        final String USER = "root";
        final String PASS = "";

        try {
            Connection conn= DriverManager.getConnection(DB_URL,USER,PASS);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM agency WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                agency = new Agency();
                agency.email = rs.getString("email");
                agency.password = rs.getString("password");
                agency.name = rs.getString("name");
                agency.phone = rs.getString("phone");


            }
            stmt.close();
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        Agency agency = loginForm.agency;
        if (agency != null){
            System.out.println("Successfully logged in");
            System.out.println("    Email: " + agency.email);
            System.out.println("    Password: " + agency.password);
            System.out.println("    Name: " + agency.name);
            System.out.println("    Phone: " + agency.phone);
        }
        else {
            System.out.println("Failed to log in");
        }
    }
}
