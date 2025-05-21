package view;

import controllers.ScoreController;
import model.userStuff.Score;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ScoresView {
    private final Color darkBlue = Color.decode("#010221");
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color orange = Color.decode("#E7615A");
    private final Color purple = Color.decode("#4B0082");

    private Font pressStart2P;
    private JFrame frameScores;
    private String username;

    public ScoresView(String username) throws IOException {
        this.username = username;
        try {
            loadFont();
            createScoresFrame();
            showScores();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening scores page: " + e.getMessage());
        }
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void createScoresFrame() {
        frameScores = new JFrame("Global Scores");
        frameScores.setSize(1700, 600);
        frameScores.setLocationRelativeTo(null);
        frameScores.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void showScores() throws SQLException {
        utils.DatabaseConnection dbConnection = new utils.DatabaseConnection();
        ScoreController scoreController = new ScoreController(dbConnection);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(darkBlue);

        String[] columns = {"Username", "Password", "Score ID", "Club Type",
                "Round Duration", "Money", "Points", "Date"};

        List<Score> scores = scoreController.getAllScores();
        Object[][] data = convertScoresToTableData(scores);

        JTable table = createScoresTable(columns, data);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);

        JLabel title = new JLabel("Global Scores", SwingConstants.CENTER);
        title.setFont(pressStart2P.deriveFont(Font.PLAIN, 24f));
        title.setForeground(brightGreen);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton backButton = createBackButton();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(darkBlue);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        bottomPanel.add(backButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frameScores.add(panel);
        frameScores.setVisible(true);
    }

    private Object[][] convertScoresToTableData(List<Score> scores) {
        Object[][] data = new Object[scores.size()][8];

        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            data[i] = new Object[]{
                    score.getUser().getUsername(),
                    score.getUser().getPassword(),
                    score.getId(),
                    score.getClubType().toString(),
                    score.getRoundDuration(),
                    score.getMoney(),
                    score.getPoints(),
                    score.getDate()
            };
        }
        return data;
    }

    private JTable createScoresTable(String[] columns, Object[][] data) {
        JTable table = new JTable(data, columns);
        table.setFont(pressStart2P.deriveFont(Font.PLAIN, 12f));
        table.setForeground(brightGreen);
        table.setBackground(Color.BLACK);
        table.setRowHeight(30);
        table.setGridColor(purple);
        table.setEnabled(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        header.setForeground(orange);
        header.setBackground(Color.BLACK);

        return table;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        backButton.setForeground(orange);
        backButton.setBackground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(orange, 2));
        backButton.setPreferredSize(new Dimension(150, 40));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(brightGreen);
                backButton.setBorder(BorderFactory.createLineBorder(brightGreen, 2));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setForeground(orange);
                backButton.setBorder(BorderFactory.createLineBorder(orange, 2));
            }
        });

        backButton.addActionListener(e -> {
            frameScores.dispose();
            try {
                new UserView(username);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return backButton;
    }
}