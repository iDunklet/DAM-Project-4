package view;

import model.ClubType;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Color.white;

public class GameResultsView {
    private final Color darkBlue = Color.decode("#010221");
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color orange = Color.decode("#E7615A");

    private Font pressStart2P;
    private JFrame resultsFrame;

    public GameResultsView(String username, ClubType clubType, int strokes, long durationSeconds, int points) throws IOException {
        try {
            loadFont();
            createResultsFrame(username, clubType, strokes, durationSeconds, points);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error showing game results: " + e.getMessage());
        }
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void createResultsFrame(String username, ClubType clubType, int strokes, long durationSeconds, int points) {
        resultsFrame = new JFrame("Resultados del Juego");
        resultsFrame.setSize(800, 600);
        resultsFrame.setLocationRelativeTo(null);
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(darkBlue);

        JLabel titleLabel = new JLabel("¡Hoyo conseguido!", SwingConstants.CENTER);
        titleLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 24f));
        titleLabel.setForeground(brightGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel statsPanel = createStatsPanel(username, clubType, strokes, durationSeconds, points);
        JPanel buttonPanel = createButtonPanel(username);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        resultsFrame.add(mainPanel);
        resultsFrame.setVisible(true);
    }

    private JPanel createStatsPanel(String username, ClubType clubType, int strokes, long durationSeconds, int points) {
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        statsPanel.setBackground(darkBlue);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        addStat(statsPanel, "Jugador:", username);
        addStat(statsPanel, "Palo utilizado:", clubType.toString());
        addStat(statsPanel, "Golpes realizados:", String.valueOf(strokes));
        addStat(statsPanel, "Tiempo:", formatTime(durationSeconds));
        addStat(statsPanel, "Puntuación:", String.valueOf(points));

        return statsPanel;
    }

    private JPanel createButtonPanel(String username) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(darkBlue);

        JButton backButton = new JButton("Volver al Menú");
        setupButton(backButton, 200, 50, orange, 16f);
        addHoverEffect(backButton, orange, brightGreen);

        JButton scoresButton = new JButton("Ver Puntuaciones");
        setupButton(scoresButton, 200, 50, brightGreen, 16f);
        addHoverEffect(scoresButton, brightGreen, orange);

        backButton.addActionListener(e -> {
            resultsFrame.dispose();
            try {
                new UserView(username);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        scoresButton.addActionListener(e -> {
            resultsFrame.dispose();
            try {
                new ScoresView(username);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(scoresButton);

        return buttonPanel;
    }

    private void addStat(JPanel panel, String label, String value) {
        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.setBackground(darkBlue);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(pressStart2P.deriveFont(Font.PLAIN, 16f));
        labelLbl.setForeground(white);

        JLabel valueLbl = new JLabel(value, SwingConstants.RIGHT);
        valueLbl.setFont(pressStart2P.deriveFont(Font.PLAIN, 16f));
        valueLbl.setForeground(brightGreen);

        statPanel.add(labelLbl, BorderLayout.WEST);
        statPanel.add(valueLbl, BorderLayout.EAST);
        panel.add(statPanel);
    }

    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void setupButton(JButton button, int width, int height, Color color, float fontSize) {
        button.setFont(pressStart2P.deriveFont(Font.PLAIN, fontSize));
        button.setForeground(color);
        button.setPreferredSize(new Dimension(width, height));
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