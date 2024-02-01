import javax.swing.*;
import java.awt.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class JoinFrame extends JInternalFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JoinFrame() {
        super("Criar Conta", true, true, true, true);
        setSize(200, 200);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton createAccountButton = new JButton("Criar Conta");

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (insertData(username, password)) {
                    dispose();
                    Main.openOptionsFrame();
                } else {
                    JOptionPane.showMessageDialog(JoinFrame.this, "Erro ao criar a conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(usernameField);
        add(passwordField);
        add(createAccountButton);
    }

    private boolean insertData(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/Stocks";
        String user = "root";
        String passwordDB = "123";

        try (Connection connection = DriverManager.getConnection(url, user, passwordDB)) {
            String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}