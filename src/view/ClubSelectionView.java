package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ClubSelectionView {
    private final Color darkBlue = Color.decode("#010221");
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color white = Color.decode("#F0F8FF");
    private final Color blueSea = Color.decode("#2F4A99");
    private final Color orange = Color.decode("#E7615A");

    private Font pressStart2P;
    private JFrame clubFrame;
    private JPanel clubPanel;
    private JLabel titleLabel;
    private String username;

    // Constructores
    private Putter driver = new Driver();
    private Putter wood = new Wood(false);
    private Putter iron = new Iron(5);

    public ClubSelectionView(String username) throws IOException {
        this.username = username;
        try {
            loadFont();
            createClubFrame();
            createClubPanel();
            addTitleLabel();
            addClubCards();
            addBackButton();
            clubFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening club selection: " + e.getMessage());
        }
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void createClubFrame() {
        clubFrame = new JFrame("Select Golf Club - " + username);
        clubFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clubFrame.setSize(1500, 800);
        clubFrame.setLocationRelativeTo(null);
    }

    private void createClubPanel() {
        clubPanel = new JPanel(null);
        clubPanel.setBackground(darkBlue);
        clubFrame.setContentPane(clubPanel);
    }

    private void addTitleLabel() {
        titleLabel = new JLabel("SELECT YOUR CLUB", SwingConstants.CENTER);
        titleLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 30f));
        titleLabel.setForeground(white);
        titleLabel.setBounds(350, 50, 800, 60);
        clubPanel.add(titleLabel);
    }

    private void addClubCards() {
        // Driver Card
        JPanel driverCard = createClubCard(driver, ClubType.DRIVER);
        driverCard.setBounds(250, 150, 300, 250);
        clubPanel.add(driverCard);

        // Wood Card
        JPanel woodCard = createClubCard(wood, ClubType.WOOD);
        woodCard.setBounds(600, 150, 300, 250);
        clubPanel.add(woodCard);

        // Iron Card
        JPanel ironCard = createClubCard(iron, ClubType.IRON);
        ironCard.setBounds(950, 150, 300, 250);
        clubPanel.add(ironCard);
    }

    private JPanel createClubCard(Putter club, ClubType clubType) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(blueSea);
        card.setBorder(BorderFactory.createLineBorder(brightGreen, 3));

        JLabel nameLabel = new JLabel(club.getName(), SwingConstants.CENTER);
        nameLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 22f));
        nameLabel.setForeground(white);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Club stats
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statsPanel.setBackground(blueSea);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        addSimpleStat(statsPanel, "Power: " + String.format("%.0f", club.getSpeed()));
        addSimpleStat(statsPanel, "Control: " + String.format("%.0f%%", club.getPrecision() * 100));
        addSimpleStat(statsPanel, club.getUseType());

        // Select button
        JButton selectButton = new JButton("Select " + club.getName());
        selectButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        selectButton.setForeground(white);
        selectButton.setBackground(brightGreen);
        selectButton.setFocusPainted(false);
        selectButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        selectButton.addActionListener(e -> {
            clubFrame.dispose();
            new GameView(username, clubType);
        });

        // Hover effect
        selectButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                selectButton.setBackground(orange);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                selectButton.setBackground(brightGreen);
            }
        });

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(statsPanel, BorderLayout.CENTER);
        card.add(selectButton, BorderLayout.SOUTH);

        return card;
    }

    private void addSimpleStat(JPanel panel, String text) {
        JLabel statLabel = new JLabel(text, SwingConstants.CENTER);
        statLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        statLabel.setForeground(brightGreen);
        panel.add(statLabel);
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 20f));
        backButton.setForeground(orange);
        backButton.setBounds(650, 500, 200, 50);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);

        backButton.addActionListener(e -> {
            clubFrame.dispose();
            try {
                new UserView(username);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        clubPanel.add(backButton);
    }
}
