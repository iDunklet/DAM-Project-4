package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class UserView {
    private final Color darkBlue = Color.decode("#010221");
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color orange = Color.decode("#E7615A");

    private Font pressStart2P;
    private JFrame userFrame;
    private JPanel userPanel;
    private JButton playButton;
    private JButton scoresButton;
    private JButton logoutButton;
    private String username;

    public UserView(String username) throws IOException {
        this.username = username;
        try {
            loadFont();
            createUserFrame();
            createUserPanel();
            addUserButtons();
            userFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening user page: " + e.getMessage());
        }
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void createUserFrame() {
        userFrame = new JFrame("User");
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setSize(1500, 800);
        userFrame.setLocationRelativeTo(null);
    }

    private void createUserPanel() {
        userPanel = new JPanel(null);
        userPanel.setBackground(darkBlue);
        userFrame.setContentPane(userPanel);
    }

    private void addUserButtons() {
        // Botón Play
        playButton = new JButton("Play");
        setupUserButton(playButton, 300, 60, brightGreen, 20f, 600, 300);
        addHoverEffect(playButton, brightGreen, orange);

        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    userFrame.dispose();
                    new ClubSelectionView(username);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error opening club selection: " + e.getMessage());
                }
            }
        });
        userPanel.add(playButton);

        // Botón Scores
        scoresButton = new JButton("Scores");
        setupUserButton(scoresButton, 300, 60, brightGreen, 20f, 600, 400);
        addHoverEffect(scoresButton, brightGreen, orange);

        scoresButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    userFrame.dispose();
                    new ScoresView(username);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error opening scores page: " + e.getMessage());
                }
            }
        });
        userPanel.add(scoresButton);

        // Botón Exit
        logoutButton = new JButton("Exit");
        setupUserButton(logoutButton, 300, 60, orange, 20f, 600, 500);

        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        userPanel.add(logoutButton);
    }

    private void setupUserButton(JButton button, int width, int height, Color color, float fontSize, int x, int y) {
        button.setFont(pressStart2P.deriveFont(Font.PLAIN, fontSize));
        button.setForeground(color);
        button.setBounds(x, y, width, height);
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
}