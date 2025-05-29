package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuView {
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color darkBlue = Color.decode("#010221");
    private final Color orange = Color.decode("#E7615A");
    private final Color blueSea = Color.decode("#2F4A99");

    private Font pressStart2P;
    private JPanel panelMain;
    private JButton loginButton;
    private JLabel logoLabel;
    private JButton exitButton;

    public MainMenuView() throws IOException, FontFormatException {
        loadFont();
        initMainComponents();
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void initMainComponents() {
        createMainPanel();
        createLogoLabel();
        createLoginButton();
        createExitButton();
        createCatLabels();
        showMainWindow();
    }

    private void createMainPanel() {
        panelMain = new JPanel(null);
        panelMain.setSize(new Dimension(1500, 800));
        panelMain.setBackground(darkBlue);
    }

    private void createLogoLabel() {
        try {
            ImageIcon icon = new ImageIcon("resources/img/logo.png");
            Image image = icon.getImage().getScaledInstance(600, 430, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(image));
            logoLabel.setSize(600,500);
            logoLabel.setLocation((1500 - logoLabel.getWidth()) / 2, 50);
            panelMain.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading logo: " + e.getMessage());
        }
    }

    private void createLoginButton() {
        loginButton = new JButton("Login");
        setupButton(loginButton, 500, 100, brightGreen, 50f);
        loginButton.setLocation((1500 - loginButton.getWidth()) / 2, 500);

        addHoverEffect(loginButton, brightGreen, orange);

        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    ((JFrame)SwingUtilities.getWindowAncestor(panelMain)).dispose();
                    new LoginView();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error opening login page: " + e.getMessage());
                }
            }
        });

        panelMain.add(loginButton);
    }

    private void createExitButton() {
        exitButton = new JButton("Exit");
        setupButton(exitButton, 300, 100, blueSea, 25f);
        exitButton.setLocation((1500 - exitButton.getWidth()) / 2, 600);

        addHoverEffect(exitButton, blueSea, orange);

        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });

        panelMain.add(exitButton);
    }

    private void createCatLabels() {
        try {
            // Left side cats (ajustados para que no se superpongan)
            addCatGif("resources/gifs/gatoGif1.gif", 50, 100);
            addCatGif("resources/gifs/gatoGif2.gif", 50, 500);

            addCatGif("resources/gifs/gatoGif3.gif", 1150, 100);
            addCatGif("resources/gifs/gatoGif4.gif", 1150, 500);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading cat animations: " + e.getMessage());
        }
    }

    private void addCatGif(String path, int x, int y) {
        ImageIcon gifIcon = new ImageIcon(path);

        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setSize(gifIcon.getIconWidth(), gifIcon.getIconHeight()); // Usa el tamaño real del GIF
        gifLabel.setLocation(x, y);
        gifLabel.setOpaque(false);
        panelMain.add(gifLabel);
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
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(hover);
            }
            public void mouseExited(MouseEvent evt) {
                button.setForeground(normal);
            }
        });
    }

    private void showMainWindow() {
        try {
            JFrame frame = new JFrame("Main");
            frame.setIconImage(new ImageIcon("resources/img/golfball.png").getImage());
            frame.setContentPane(panelMain);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1500, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error showing main window: " + e.getMessage());
        }
    }
}
