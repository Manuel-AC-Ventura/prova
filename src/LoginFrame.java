import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class LoginFrame extends JInternalFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LoginFrame() {
        super("Login", true, true, true, true);
        setSize(200, 200);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Entrar");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login bem-sucedido!");
                    dispose();
                    Main.openOptionsFrame();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Nome de usuário ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(usernameField);
        add(passwordField);
        add(loginButton);
    }

    private boolean validateLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/Stocks";
        String user = "root";
        String passwordDB = "123";

        try (Connection connection = DriverManager.getConnection(url, user, passwordDB)) {
            String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}