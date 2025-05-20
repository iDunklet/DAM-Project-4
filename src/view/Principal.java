package view;

import dbstuff.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Principal {
    private Font pressStart2P;
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color darkBlue = Color.decode("#010221");
    private final Color white = Color.decode("#F0F8FF");
    private final Color orange = Color.decode("#E7615A");
    private final Color blueSea = Color.decode("#2F4A99");
    private final Color purple = Color.decode("#4B0082");

    private JPanel panelMain;
    private JButton loginButton;
    private JLabel logoLabel;
    private JButton exitButton;
    private JLabel gato1Label;
    private JButton ingresarButton;
    private JFrame loginFrame;
    private JPanel loginPanel;
    private JPasswordField passField;
    private JLabel passLabel;
    private JTextField userField;
    private JLabel userLabel;
    private JFrame userFrame;
    private JPanel userPanel;
    private JButton playButton;
    private JButton scoresButton;
    private JButton logoutButton;



    public Principal() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);

        this.panelMain();
        this.loginButton();
        this.logoLabel();
        this.exitButton();
        this.gato1Label();
    }

    private void gato1Label() {
        ImageIcon icon = new ImageIcon("resources/gifs/gatoGif1.gif");
        gato1Label = new JLabel(icon);
        gato1Label.setSize(250, 250);
        gato1Label.setOpaque(false);
        gato1Label.setLocation(100, panelMain.getHeight()/2);
        panelMain.add(gato1Label);
    }

    private void logoLabel() {
        ImageIcon icon = new ImageIcon("resources/img/logo.png");
        Image image = icon.getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(image));
        logoLabel.setOpaque(true);
        logoLabel.setVisible(true);
        logoLabel.setSize(600,500);
        int x = (1500 - logoLabel.getWidth()) / 2;
        logoLabel.setLocation(x,50);
        panelMain.add(logoLabel);
    }

    private void panelMain() {
        panelMain = new JPanel(null);
        panelMain.setSize(new Dimension(1500, 800));
        panelMain.setBackground(darkBlue);
    }

    private void loginButton() {
        loginButton = new JButton("Login");
        loginButton.setBackground(null);
        loginButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 50f));
        loginButton.setSize(500, 100);
        loginButton.setForeground(brightGreen);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        int x = (1500 - loginButton.getWidth()) / 2;
        loginButton.setLocation(x, 500);

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setForeground(orange);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setForeground(brightGreen);
            }
        });

        loginButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panelMain);
            frame.dispose();
            openLoginPage();
        });

        panelMain.add(loginButton);
    }

    private void openLoginPage() {
        loginFrame();
        loginPanel();
        loginLogo();
        userLabel();
        userField();
        passLabel();
        passField();
        ingresarButton();


        hoverButtonBrightGreenForOrange(ingresarButton);

        loginPanel.add(ingresarButton);
        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }

    private void hoverButtonBrightGreenForOrange(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(orange);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(brightGreen);
            }
        });
    }

    private void passField() {
        passField = new JPasswordField();
        passField.setFont(pressStart2P.deriveFont(Font.PLAIN, 16f));
        passField.setSize(400, 40);
        passField.setLocation(550, 530);
        loginPanel.add(passField);
    }

    private void passLabel() {
        passLabel = new JLabel("Contraseña:");
        passLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        passLabel.setForeground(white);
        passLabel.setSize(300, 30);
        passLabel.setLocation(550, 500);
        loginPanel.add(passLabel);
    }

    private void userField() {
        userField = new JTextField();
        userField.setFont(pressStart2P.deriveFont(Font.PLAIN, 16f));
        userField.setSize(400, 40);
        userField.setLocation(550, 450);
        loginPanel.add(userField);
    }

    private void userLabel() {
        userLabel = new JLabel("Usuario:");
        userLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        userLabel.setForeground(white);
        userLabel.setSize(200, 30);
        userLabel.setLocation(550, 420);
        loginPanel.add(userLabel);
    }

    private void loginLogo() {
        ImageIcon icon = new ImageIcon("resources/img/logo.png");
        Image image = icon.getImage().getScaledInstance(400, 350, Image.SCALE_SMOOTH);
        JLabel loginLogo = new JLabel(new ImageIcon(image));
        loginLogo.setSize(400, 350);
        int xLogo = (1500 - loginLogo.getWidth()) / 2;
        loginLogo.setLocation(xLogo, 50);
        loginPanel.add(loginLogo);
    }

    private void loginPanel() {
        loginPanel = new JPanel(null);
        loginPanel.setBackground(darkBlue);
        loginPanel.setSize(1500, 800);
    }

    private void loginFrame() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(1500, 800);
        loginFrame.setLocationRelativeTo(null);
    }

    private void ingresarButton() {
        ingresarButton = new JButton("Ingresar");
        ingresarButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        ingresarButton.setForeground(brightGreen);
        ingresarButton.setBackground(darkBlue);
        ingresarButton.setBorderPainted(false);
        ingresarButton.setFocusPainted(false);
        ingresarButton.setContentAreaFilled(false);
        ingresarButton.setSize(200, 50);
        ingresarButton.setLocation(650, 600);
        actioningresarButton();
    }

    private void actioningresarButton() {
        ingresarButton.addActionListener(e -> {
            String name = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (!name.isEmpty() && !password.isEmpty()) {
                boolean success = AuthService.loginOrCreateUser(name, password);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Login exitoso");
                    loginFrame.dispose();
                    openUserPage();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese usuario y contraseña");
            }
        });
    }

    private void openUserPage() {
        userFrame();
        userPanel();
        playButton();
        scoresButton();
        logoButton();
        userFrame.setContentPane(userPanel);
        userFrame.setVisible(true);
    }

    private void logoButton() {
        logoutButton = new JButton("Salir");
        logoutButton.setBounds(600, 500, 300, 60);
        logoutButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        logoutButton.setForeground(orange);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> System.exit(0));
        userPanel.add(logoutButton);
    }

    private void scoresButton() {
        scoresButton = new JButton("Puntuaciones");
        scoresButton.setBounds(600, 400, 300, 60);
        scoresButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        scoresButton.setForeground(brightGreen);
        scoresButton.setContentAreaFilled(false);
        scoresButton.setBorderPainted(false);
        scoresButton.setFocusPainted(false);
        userPanel.add(scoresButton);
        hoverButtonBrightGreenForOrange(scoresButton);

        scoresButton.addActionListener(e -> {
            userFrame.dispose();
            openScorePage();
        });
    }

    private void playButton() {
        playButton = new JButton("Jugar");
        playButton.setBounds(600, 300, 300, 60);
        playButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        playButton.setForeground(brightGreen);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        userPanel.add(playButton);
        hoverButtonBrightGreenForOrange(playButton);

    }

    private void userPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(null);
        userPanel.setBackground(darkBlue);
    }

    private void userFrame() {
        userFrame = new JFrame("Usuario");
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setSize(1500, 800);
        userFrame.setLocationRelativeTo(null);
    }


    private void exitButton() {
        exitButton = new JButton("exit");
        exitButton.setBackground(null);
        exitButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 25f));
        exitButton.setSize(300, 100);
        exitButton.setForeground(blueSea);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setContentAreaFilled(false);
        int x = (1500 - exitButton.getWidth()) / 2;
        exitButton.setLocation(x, 600);
        exitButtonHover();
        exitButtonAction();
        panelMain.add(exitButton);
    }

    private void exitButtonAction() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void exitButtonHover() {
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setForeground(orange);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setForeground(blueSea);
            }
        });
    }
    private void openScorePage() {
        JFrame scoreFrame = new JFrame("Puntuaciones Globales");
        scoreFrame.setSize(1700, 600);
        scoreFrame.setLocationRelativeTo(null);
        scoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(darkBlue);

        String[] columnNames = {
                "Usuario", "Contraseña", "ID Puntos", "Tipo de Club",
                "Duración Ronda", "Dinero", "Puntos", "Fecha"
        };

        Object[][] data = AuthService.getAllUsersWithPoints();

        JTable table = new JTable(data, columnNames);
        table.setFont(pressStart2P.deriveFont(Font.PLAIN, 12f));
        table.setForeground(brightGreen);
        table.setBackground(Color.BLACK);
        table.setRowHeight(30);
        table.setGridColor(purple);
        table.setEnabled(false);
        table.getTableHeader().setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        table.getTableHeader().setForeground(orange);
        table.getTableHeader().setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Puntuaciones Globales", SwingConstants.CENTER);
        titleLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 24f));
        titleLabel.setForeground(brightGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        scoreFrame.add(panel);
        scoreFrame.setVisible(true);
    }


    public static void main(String[] args) throws IOException, FontFormatException {
        JFrame frame = new JFrame("Principal");
        frame.setIconImage(new ImageIcon("resources/img/golfball.png").getImage());
        frame.setContentPane(new Principal().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}