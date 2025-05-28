package view;

import controllers.ScoreController;
import model.*;
import model.userStuff.Score;
import model.userStuff.User;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class GameView {
    private final Color darkBlue = Color.decode("#010221");
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color orange = Color.decode("#E7615A");
    private final Color purple = Color.decode("#4B0082");

    private Font pressStart2P;
    private JFrame gameFrame;
    private Rectangle holeRect;
    private Rectangle ballRect;
    private int strokeCount = 0;
    private long startTime;
    private ClubType selectedClubType;
    private String username;
    private Timer gameTimer;
    private ImageIcon flagGif;
    private ImageIcon scaledFlagGif;

    // Game physics
    private List<Rectangle> obstacles = new ArrayList<>();
    private Point ballPosition = new Point(950, 100);
    private Point badBallPosition = new Point(600, 400);
    private Point dragStart;
    private Point dragEnd;
    private boolean isDragging = false;
    private boolean isBallMoving = false;
    private double ballVelocityX = 0;
    private double ballVelocityY = 0;
    private final int BALL_RADIUS = 25;
    private final double FRICTION = 0.98;
    private ImageIcon goodCat;
    private ImageIcon badCat;
    private boolean collisionHandled = false;
    private ScoreController scoreController;

    public GameView(String username, ClubType clubType) {
        this.username = username;
        this.selectedClubType = clubType;
        this.scoreController = new ScoreController(new utils.DatabaseConnection());

        try {
            loadFont();
            loadImages();
            initGame();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error initializing game: " + e.getMessage());
        }
    }

    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void loadImages() {
        goodCat = new ImageIcon("resources/img/goodCat.jpg");
        badCat = new ImageIcon("resources/img/badCat.jpg");
        flagGif = new ImageIcon("resources/gifs/flag.gif");
        ImageIcon originalFlagGif = new ImageIcon("resources/gifs/flag.gif");
        Image scaledFlag = originalFlagGif.getImage().getScaledInstance(
                100, 150, Image.SCALE_DEFAULT); // Ajusta el tamaño (ancho, alto)
        scaledFlagGif = new ImageIcon(scaledFlag);
    }

    private void initGame() {
        strokeCount = 0;
        startTime = System.currentTimeMillis();
        collisionHandled = false;

        createGameFrame();
        setupObstacles();
        startGameLoop();
    }

    private void createGameFrame() {
        gameFrame = new JFrame("Golf Game - " + username);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(1500, 800);
        gameFrame.setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder());

        JPanel sidePanel = createSidePanel();
        sidePanel.setPreferredSize(new Dimension(300, 800));

        JPanel gamePanel = createGamePanel();

        mainContainer.add(sidePanel, BorderLayout.WEST);
        mainContainer.add(gamePanel, BorderLayout.CENTER);

        gameFrame.setContentPane(mainContainer);
        gameFrame.setVisible(true);
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(20, 20, 40));

        Font infoFont = pressStart2P.deriveFont(14f);
        Color textColor = Color.WHITE;

        JLabel playerLabel = new JLabel("Jugador: " + username);
        playerLabel.setFont(infoFont);
        playerLabel.setForeground(textColor);
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel strokesLabel = new JLabel("Golpes: 0");
        strokesLabel.setFont(infoFont);
        strokesLabel.setForeground(textColor);
        strokesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        strokesLabel.setName("strokesLabel");

        JLabel timeLabel = new JLabel("Tiempo: 00:00");
        timeLabel.setFont(infoFont);
        timeLabel.setForeground(textColor);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setName("timeLabel");

        JLabel clubLabel = new JLabel("Palo: " + selectedClubType.toString());
        clubLabel.setFont(infoFont);
        clubLabel.setForeground(textColor);
        clubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(brightGreen);
        separator.setMaximumSize(new Dimension(0, 5));

        JPanel computerPanel = createComputerPanel();

        sidePanel.add(playerLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(strokesLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(timeLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(clubLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(separator);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(computerPanel);

        return sidePanel;
    }

    private JPanel createComputerPanel() {
        JPanel computerPanel = new JPanel();
        computerPanel.setLayout(new BoxLayout(computerPanel, BoxLayout.Y_AXIS));
        computerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerPanel.setBackground(new Color(20, 20, 40));
        computerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        try {
            ImageIcon computerIcon = new ImageIcon("resources/img/computer.png");
            Image scaledComputer = computerIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            JLabel computerLabel = new JLabel(new ImageIcon(scaledComputer));
            computerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            computerPanel.add(computerLabel);

            ImageIcon gifIcon = new ImageIcon("resources/gifs/typing.gif");
            JLabel gifLabel = new JLabel(gifIcon);
            gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            gifLabel.setBorder(BorderFactory.createEmptyBorder(-50, 0, 0, 0));

            computerPanel.add(gifLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading computer image: " + e.getMessage());
        }

        return computerPanel;
    }

    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };

        gamePanel.setBounds(0, 0, 1200, 800);
        setupGameMouseListeners(gamePanel);

        holeRect = new Rectangle(1000, 650, 30, 30);
        ballRect = new Rectangle(ballPosition.x - BALL_RADIUS, ballPosition.y - BALL_RADIUS,
                BALL_RADIUS * 2, BALL_RADIUS * 2);

        return gamePanel;
    }

    private void drawGame(Graphics g) {
        // Background
        g.setColor(new Color(34, 139, 34));
        g.fillRect(0, 0, 1200, 800);

        // Hole
        g.setColor(Color.BLACK);
        g.fillOval(1000, 650, 40, 40);
        int flagX = holeRect.x ;
        int flagY = holeRect.y - 100;
        g.drawImage(scaledFlagGif.getImage(), flagX, flagY, null);

        // Obstacles
        g.setColor(Color.GRAY);
        for (Rectangle rect : obstacles) {
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        // Balls
        int diameter = BALL_RADIUS * 2;
        g.drawImage(goodCat.getImage(),
                ballPosition.x - BALL_RADIUS,
                ballPosition.y - BALL_RADIUS,
                diameter, diameter, null);

        g.drawImage(badCat.getImage(),
                badBallPosition.x - BALL_RADIUS,
                badBallPosition.y - BALL_RADIUS,
                diameter, diameter, null);

        // Direction line
        if (isDragging && dragStart != null && dragEnd != null) {
            g.setColor(Color.RED);
            g.drawLine(dragStart.x, dragStart.y, dragEnd.x, dragEnd.y);

            int distance = (int)dragStart.distance(dragEnd);
            g.setColor(Color.WHITE);
            g.drawString("Fuerza: " + distance, dragStart.x, dragStart.y - 10);
        }
    }

    private void setupGameMouseListeners(JPanel gamePanel) {
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isPointInBall(e.getPoint())) {
                    isDragging = true;
                    dragStart = e.getPoint();
                    dragEnd = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    isDragging = false;
                    dragEnd = e.getPoint();
                    launchBall();
                    strokeCount++;
                    updateStrokesLabel();
                    gamePanel.repaint();
                }
            }
        });

        gamePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    dragEnd = e.getPoint();
                    gamePanel.repaint();
                }
            }
        });
    }

    private void setupObstacles() {
        obstacles.clear();

        // Walls
        obstacles.add(new Rectangle(0, 750, 1200, 50)); // bottom
        obstacles.add(new Rectangle(1150, 0, 50, 800)); // right
        obstacles.add(new Rectangle(0, 0, 1200, 50)); // top
        obstacles.add(new Rectangle(0, 0, 50, 800)); // left

        // S shape
        obstacles.add(new Rectangle(300, 150, 900, 30)); // top
        obstacles.add(new Rectangle(0, 350, 900, 30)); // middle
        obstacles.add(new Rectangle(300, 550, 900, 30)); // bottom
    }

    private void startGameLoop() {
        gameTimer = new Timer(16, e -> {
            updateBallPosition();
            updateTimer();
            gameFrame.getContentPane().repaint();
        });
        gameTimer.start();
    }

    private void updateBallPosition() {
        ballPosition.x += ballVelocityX;
        ballPosition.y += ballVelocityY;
        ballRect.setLocation(ballPosition.x - BALL_RADIUS, ballPosition.y - BALL_RADIUS);
        updatebadCatPosition();
        checkCatCollision();
        checkHoleCollision();

        checkObstacleCollisions();
        applyPhysics();
    }

    private void checkObstacleCollisions() {
        for (Rectangle obstacle : obstacles) {
            if (ballRect.intersects(obstacle)) {
                Rectangle intersection = ballRect.intersection(obstacle);

                if (intersection.width < intersection.height) {
                    ballVelocityX *= -0.7;
                    if (ballPosition.x < obstacle.x) {
                        ballPosition.x = obstacle.x - BALL_RADIUS;
                    } else {
                        ballPosition.x = obstacle.x + obstacle.width + BALL_RADIUS;
                    }
                } else {
                    ballVelocityY *= -0.7;
                    if (ballPosition.y < obstacle.y) {
                        ballPosition.y = obstacle.y - BALL_RADIUS;
                    } else {
                        ballPosition.y = obstacle.y + obstacle.height + BALL_RADIUS;
                    }
                }
                break;
            }
        }
    }

    private void applyPhysics() {
        ballVelocityX *= FRICTION;
        ballVelocityY *= FRICTION;

        // Border bouncing
        if (ballPosition.x - BALL_RADIUS < 0) {
            ballPosition.x = BALL_RADIUS;
            ballVelocityX *= -0.7;
        } else if (ballPosition.x + BALL_RADIUS > 1200) {
            ballPosition.x = 1200 - BALL_RADIUS;
            ballVelocityX *= -0.7;
        }

        if (ballPosition.y - BALL_RADIUS < 0) {
            ballPosition.y = BALL_RADIUS;
            ballVelocityY *= -0.7;
        } else if (ballPosition.y + BALL_RADIUS > 750) {
            ballPosition.y = 750 - BALL_RADIUS;
            ballVelocityY *= -0.7;
        }

        if (Math.abs(ballVelocityX) < 0.2 && Math.abs(ballVelocityY) < 0.2) {
            ballVelocityX = 0;
            ballVelocityY = 0;
            isBallMoving = false;
        }
    }

    private void updatebadCatPosition() {
        double speed = 1.5;
        double dx = ballPosition.x - badBallPosition.x;
        double dy = ballPosition.y - badBallPosition.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 1) {
            double moveX = speed * dx / distance;
            double moveY = speed * dy / distance;
            badBallPosition.x += (int)Math.round(moveX);
            badBallPosition.y += (int)Math.round(moveY);
        }
    }

    private void checkCatCollision() {
        if (collisionHandled) return;

        int collisionDistance = BALL_RADIUS * 2;
        double distance = ballPosition.distance(badBallPosition);

        if (distance < collisionDistance) {
            collisionHandled = true;
            showFullScreenImage();
            playSound();

            Timer delayTimer = new Timer(1000, e -> {
                // Cierra el juego y vuelve al UserView
                gameFrame.dispose();
                try {
                    new UserView(username);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ((Timer)e.getSource()).stop();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    private void checkHoleCollision() {
        if (holeRect.intersects(ballRect)) {
            gameTimer.stop();
            try {
                long endTime = System.currentTimeMillis();
                long durationSeconds = (endTime - startTime) / 1000;
                int points = calculateScore(strokeCount, durationSeconds);

                User user = getUserByName(username);
                scoreController.saveScore(user, selectedClubType, strokeCount, durationSeconds);

                gameFrame.dispose();
                new GameResultsView(username, selectedClubType, strokeCount, durationSeconds, points);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error saving score: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private User getUserByName(String username) throws SQLException {
        utils.DatabaseConnection dbConnection = new utils.DatabaseConnection();
        try (Connection conn = dbConnection.getConnection()) {
            String query = "SELECT id_user, name, password FROM users WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getInt("id_user"),
                            rs.getString("name"),
                            rs.getString("password")
                    );
                }
            }
        }
        throw new SQLException("User not found: " + username);
    }

    private int calculateScore(int strokes, long duration) {
        double score = 10000.0 / (1 + strokes * 0.5 + duration * 0.1);
        return (int) Math.round(score);
    }

    private void resetGame() {
        ballPosition = new Point(950, 100);
        badBallPosition = new Point(600, 400);
        isBallMoving = false;
        isDragging = false;
        ballVelocityX = 0;
        ballVelocityY = 0;
        collisionHandled = false;
        strokeCount = 0;
        startTime = System.currentTimeMillis();
        ballRect.setLocation(ballPosition.x - BALL_RADIUS, ballPosition.y - BALL_RADIUS);
        gameFrame.getContentPane().repaint();
    }

    private void handleGameOver() throws IOException {
        gameFrame.dispose();
        new UserView(username);
    }

    private void showFullScreenImage() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setAlwaysOnTop(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ImageIcon icon = new ImageIcon("resources/img/badCat.jpg");
        Image scaledImage = icon.getImage().getScaledInstance(
                screenSize.width, screenSize.height, Image.SCALE_SMOOTH);

        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        frame.add(label);
        frame.setVisible(true);

        new Timer(500, e -> frame.dispose()).start();
    }

    private void playSound() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("resources/audio/scream.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPointInBall(Point point) {
        int dx = point.x - ballPosition.x;
        int dy = point.y - ballPosition.y;
        return dx * dx + dy * dy <= BALL_RADIUS * BALL_RADIUS;
    }

    private void launchBall() {
        if (dragStart == null || dragEnd == null) return;

        double dragDistance = dragStart.distance(dragEnd);
        double force = Math.min(dragDistance / 15.0, 30.0);
        double angle = Math.atan2(dragEnd.y - dragStart.y, dragEnd.x - dragStart.x);

        ballVelocityX = -force * Math.cos(angle);
        ballVelocityY = -force * Math.sin(angle);

        isBallMoving = true;
    }

    private void updateStrokesLabel() {
        for (Component comp : gameFrame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component sideComp : ((JPanel) comp).getComponents()) {
                    if (sideComp instanceof JLabel && "strokesLabel".equals(sideComp.getName())) {
                        ((JLabel) sideComp).setText("Golpes: " + strokeCount);
                    }
                }
            }
        }
    }

    private void updateTimer() {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        String timeString = String.format("Tiempo: %02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);

        for (Component comp : gameFrame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component sideComp : ((JPanel) comp).getComponents()) {
                    if (sideComp instanceof JLabel && "timeLabel".equals(sideComp.getName())) {
                        ((JLabel) sideComp).setText(timeString);
                    }
                }
            }
        }
    }
}
