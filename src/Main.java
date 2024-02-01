import javax.swing.*;
import java.awt.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {
    private static JDesktopPane desktopPane;
    private static JButton loginButton;
    private static JButton createAccountButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> initializeUI());
    }

    private static void initializeUI() {
        JFrame title = new JFrame("StockManager");
        desktopPane = new JDesktopPane();

        loginButton = new JButton("Login");
        createAccountButton = new JButton("Criar Conta");

        loginButton.addActionListener(e -> loginButtonClicked());
        createAccountButton.addActionListener(e -> createAccountButtonClicked());

        desktopPane.add(loginButton);
        desktopPane.add(createAccountButton);

        loginButton.setBounds(10, 10, 100, 30);
        createAccountButton.setBounds(120, 10, 150, 30);

        title.setContentPane(desktopPane);
        title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title.setSize(800, 500);
        title.setLocationRelativeTo(null);
        title.setVisible(true);
    }

    public static void openOptionsFrame() {
        OptionsFrame optionsFrame = new OptionsFrame();
        desktopPane.add(optionsFrame, JLayeredPane.DEFAULT_LAYER);
        optionsFrame.setVisible(true);
    }

    private static void loginButtonClicked() {
        LoginFrame loginFrame = new LoginFrame();
        desktopPane.add(loginFrame, JLayeredPane.DEFAULT_LAYER);
        loginFrame.setVisible(true);

        loginFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent event) {
                loginButton.setVisible(true);
                createAccountButton.setVisible(true);
            }
        });

        loginButton.setVisible(false);
        createAccountButton.setVisible(false);
    }

    private static void createAccountButtonClicked() {
        JoinFrame createAccountFrame = new JoinFrame();
        desktopPane.add(createAccountFrame, JLayeredPane.DEFAULT_LAYER);
        createAccountFrame.setVisible(true);

        createAccountFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent event) {
                loginButton.setVisible(true);
                createAccountButton.setVisible(true);
            }
        });

        loginButton.setVisible(false);
        createAccountButton.setVisible(false);
    }
}