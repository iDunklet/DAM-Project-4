package view;

import controllers.AuthController;
import model.userStuff.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class LoginView {
    private final Color darkBlue = Color.decode("#010221");
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color orange = Color.decode("#E7615A");
    private final Color white = Color.decode("#F0F8FF");

    private Font pressStart2P;
    private JFrame loginFrame;
    private JPanel loginPanel;
    private JPasswordField passField;
    private JLabel passLabel;
    private JTextField userField;
    private JLabel userLabel;
    private JButton ingresarButton;

    public LoginView() {
        try {
            loadFont();
            createLoginFrame();
            createLoginPanel();
            addLoginLogo();
            addLoginFields();
            addLoginButton();

            loginFrame.add(loginPanel);
            loginFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening login page: " + e.getMessage());
        }
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void createLoginFrame() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(1500, 800);
        loginFrame.setLocationRelativeTo(null);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(null);
        loginPanel.setBackground(darkBlue);
        loginPanel.setSize(1500, 800);
    }

    private void addLoginLogo() {
        try {
            ImageIcon icon = new ImageIcon("resources/img/logo.png");
            Image image = icon.getImage().getScaledInstance(400, 350, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(image));
            logo.setSize(400, 350);
            logo.setLocation((1500 - logo.getWidth()) / 2, 50);
            loginPanel.add(logo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading login logo: " + e.getMessage());
        }
    }

    private void addLoginFields() {
        // Campo de usuario
        userLabel = new JLabel("Username:");
        setupLabel(userLabel, 200, 30, 550, 420, 20f);
        loginPanel.add(userLabel);

        userField = new JTextField();
        setupTextField(userField, 400, 40, 550, 450);
        loginPanel.add(userField);

        // Campo de contraseña
        passLabel = new JLabel("Password:");
        setupLabel(passLabel, 300, 30, 550, 500, 20f);
        loginPanel.add(passLabel);

        passField = new JPasswordField();
        setupTextField(passField, 400, 40, 550, 530);
        loginPanel.add(passField);
    }

    private void setupLabel(JLabel label, int width, int height, int x, int y, float fontSize) {
        label.setFont(pressStart2P.deriveFont(Font.PLAIN, fontSize));
        label.setForeground(white);
        label.setSize(width, height);
        label.setLocation(x, y);
    }

    private void setupTextField(JComponent field, int width, int height, int x, int y) {
        field.setFont(pressStart2P.deriveFont(Font.PLAIN, 16f));
        field.setSize(width, height);
        field.setLocation(x, y);
    }

    private void addLoginButton() {
        ingresarButton = new JButton("Enter");
        setupButton(ingresarButton, 200, 50, brightGreen, 20f);
        ingresarButton.setLocation(650, 600);

        addHoverEffect(ingresarButton, brightGreen, orange);

        ingresarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                handleLogin(evt);
            }
        });

        loginPanel.add(ingresarButton);
    }

    private void setupButton(JButton button, int width, int height, Color color, float fontSize) {
        button.setFont(pressStart2P.deriveFont(Font.PLAIN, fontSize));
        button.setForeground(color);
        button.setSize(width, height);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
    }

    private void addHoverEffect(JButton button, Color normal, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(normal);
            }
        });
    }

    private void handleLogin(ActionEvent evt) {
        try {
            String name = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter username and password");
                return;
            }
            utils.DatabaseConnection dbConnection = new utils.DatabaseConnection();
            AuthController authController = new AuthController(dbConnection);

            User user = authController.loginOrRegister(name, password);

            loginFrame.dispose();
            new UserView(name);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Login error: " + e.getMessage());
        }
    }
}