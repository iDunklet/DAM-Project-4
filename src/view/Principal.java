package view;

import controllers.dbstuff.AuthService;
import controllers.dbstuff.ClubType;
import model.Driver;
import model.Iron;
import model.Putter;
import model.Wood;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.awt.Rectangle;

public class Principal {

    // recorrido
    private List<Rectangle> obstacles = new ArrayList<>();


    // Variables para la física de la pelota
    private Point ballPosition = new Point(100, 400); // Posición inicial
    private Point badBallPosition = new Point(400, 400);
    private Point dragStart; // comienza el arrastre
    private Point dragEnd; // termina el arrastre
    private boolean isDragging = false;
    private boolean isBallMoving = false;
    private double ballVelocityX = 0;
    private double ballVelocityY = 0;
    private final int BALL_RADIUS = 25;
    private final double FRICTION = 0.98; // Fricción del terreno
    private ImageIcon goodCat;
    private ImageIcon badCat;
    private boolean collisionHandled = false;

    // Constructores
    private Putter driver = new Driver();
    private Putter wood = new Wood(false);
    private Putter iron = new Iron(5);

    // CONSTANTES DE COLORES
    private final Color brightGreen = Color.decode("#2BBFAF");
    private final Color darkBlue = Color.decode("#010221");
    private final Color white = Color.decode("#F0F8FF");
    private final Color orange = Color.decode("#E7615A");
    private final Color blueSea = Color.decode("#2F4A99");
    private final Color purple = Color.decode("#4B0082");

    // COMPONENTES PRINCIPALES
    private Font pressStart2P;
    private JPanel panelMain;
    private JFrame loginFrame;
    private JFrame userFrame;

    // COMPONENTES DEL PANEL MAIN
    private JButton loginButton;
    private JLabel logoLabel;
    private JButton exitButton;
    private JLabel gato1Label;

    // COMPONENTES DEL LOGIN
    private JPanel loginPanel;
    private JPasswordField passField;
    private JLabel passLabel;
    private JTextField userField;
    private JLabel userLabel;
    private JButton ingresarButton;

    // COMPONENTES DEL PANEL DE USUARIO
    private JPanel userPanel;
    private JButton playButton;
    private JButton scoresButton;
    private JButton logoutButton;

    //  COMPONENTES DEL PANEL DE SELECCION
    private JFrame clubFrame;
    private JPanel card;
    private String username;
    private JPanel clubPanel;
    private JLabel titleLabel;
    private JPanel driverCard;
    private JPanel woodCard;
    private JPanel ironCard;
    private JLabel nameLabel;


    // COMPONENTES PANEL PUNTUACION
    private JFrame frameScores;

    //COMPONENTES PANEL DE JUEGO
    private JFrame gameFrame;
    private Rectangle holeRect;
    private Rectangle ballRect;
    private int strokeCount = 0;
    private long startTime;
    private ClubType selectedClubType;


    // CONSTRUCTOR PRINCIPAL
    public Principal() {
        try {
            loadFont();
            goodCat = new ImageIcon("resources/img/goodCat.jpg"); // Asegúrate de tener esta imagen
        } catch (IOException | FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading resources: " + e.getMessage());
            System.exit(1);
        }
        try {
            loadFont();
        } catch (IOException | FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading resources: " + e.getMessage());
            System.exit(1);
        }
        try {
            loadFont();
            badCat = new ImageIcon("resources/img/badCat.jpg"); // Asegúrate de tener esta imagen
        } catch (IOException | FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading resources: " + e.getMessage());
            System.exit(1);
        }
        try {
            loadFont();
        } catch (IOException | FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading resources: " + e.getMessage());
            System.exit(1);
        }
        initMainComponents();
    }

    // MÉTODOS DE INICIALIZACIÓN
    private void loadFont() throws IOException, FontFormatException {
        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
    }

    private void initMainComponents() {
        createMainPanel();
        createLogoLabel();
        createLoginButton();
        createExitButton();
        createCatLabel();
        showMainWindow();
    }

    // CONFIGURACIÓN DE LA VENTANA PRINCIPAL
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
                    openLoginPage();
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

    private void createCatLabel() {
        try {
            ImageIcon icon = new ImageIcon("resources/gifs/gatoGif1.gif");
            gato1Label = new JLabel(icon);
            gato1Label.setSize(250, 250);
            gato1Label.setLocation(100, panelMain.getHeight()/2);
            panelMain.add(gato1Label);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading cat animation: " + e.getMessage());
        }
    }

    // MÉTODOS UTILITARIOS
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

    // MÉTODOS DE LA PÁGINA DE LOGIN
    private void openLoginPage() {
        try {
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

    private void handleLogin(ActionEvent evt) {
        try {
            String name = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter username and password");
                return;
            }
            if (AuthService.loginOrCreateUser(name, password)) {
                loginFrame.dispose();
                openUserPage();
            } else {
                JOptionPane.showMessageDialog(null, "Error connecting to database");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Login error: " + e.getMessage());
        }
    }

    // MÉTODOS DEL PANEL DE USUARIO
    private void openUserPage() {
        try {
            createUserFrame();
            createUserPanel();
            addUserButtons();
            userFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening user page: " + e.getMessage());
        }
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
                    openClubSelectionPage();
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
                    openScoresPage();
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
        setupButton(button, width, height, color, fontSize);
        button.setBounds(x, y, width, height);
    }

    // MÉTODOS DE LA PÁGINA DE PUNTUACIONES
    private void openScoresPage() {
        try {
            frameScores = new JFrame("Global Scores");
            frameScores.setSize(1700, 600);
            frameScores.setLocationRelativeTo(null);
            frameScores.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(darkBlue);

            String[] columns = {"Username", "Password", "Score ID", "Club Type",
                    "Round Duration", "Money", "Points", "Date"};

            JTable table = createScoresTable(columns, AuthService.getAllUsersWithPoints());

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(Color.BLACK);

            JLabel title = new JLabel("Global Scores", SwingConstants.CENTER);
            title.setFont(pressStart2P.deriveFont(Font.PLAIN, 24f));
            title.setForeground(brightGreen);
            title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

            // Botón Back
            JButton backButton = new JButton("Back");
            backButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
            backButton.setForeground(orange);
            backButton.setBackground(Color.BLACK);
            backButton.setFocusPainted(false);
            backButton.setBorder(BorderFactory.createLineBorder(orange, 2));
            backButton.setPreferredSize(new Dimension(150, 40));

            // Efecto hover
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

            backButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        frameScores.dispose();
                        openUserPage();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error returning to user page: " + e.getMessage());
                    }
                }
            });

            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(darkBlue);
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
            bottomPanel.add(backButton);

            panel.add(title, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            frameScores.add(panel);
            frameScores.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening scores page: " + e.getMessage());
        }
    }

    private JTable createScoresTable(String[] columns, Object[][] data) {
        JTable table = new JTable(data, columns);
        table.setFont(pressStart2P.deriveFont(Font.PLAIN, 12f));
        table.setForeground(brightGreen);
        table.setBackground(Color.BLACK);
        table.setRowHeight(30);
        table.setGridColor(purple);
        table.setEnabled(false);

        table.getTableHeader().setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        table.getTableHeader().setForeground(orange);
        table.getTableHeader().setBackground(Color.BLACK);

        return table;
    }

    // MÉTODOS DE SELECCIÓN DE PALOS DE GOLF
    private void openClubSelectionPage() {
        try {
            username = userField.getText().trim();

            clubFrame();
            clubPanel();
            tittleLabel();
            driverCard();
            woodCard();

            JPanel ironCard = createSimpleClubCard(iron, username);
            ironCard.setBounds(950, 150, 300, 250);
            clubPanel.add(ironCard);

            // Back button
            JButton backButton = new JButton("Back");
            setupButton(backButton, 200, 50, orange, 20f);
            backButton.setLocation(650, 500);
            addHoverEffect(backButton, orange, brightGreen);

            backButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        clubFrame.dispose();
                        openUserPage();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error returning to user page: " + e.getMessage());
                    }
                }
            });
            clubPanel.add(backButton);

            clubFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening club selection: " + e.getMessage());
        }
    }

    private void woodCard() {
        woodCard = createSimpleClubCard(wood, username);
        woodCard.setBounds(600, 150, 300, 250);
        clubPanel.add(woodCard);
    }

    private void driverCard() {
        driverCard = createSimpleClubCard(driver, username);
        driverCard.setBounds(250, 150, 300, 250);
        clubPanel.add(driverCard);
    }

    private void tittleLabel() {
        titleLabel = new JLabel("SELECT YOUR CLUB", SwingConstants.CENTER);
        setupLabel(titleLabel, 800, 60, 350, 50, 30f);
        clubPanel.add(titleLabel);
    }

    private void clubPanel() {
        clubPanel = new JPanel(null);
        clubPanel.setBackground(darkBlue);
        clubFrame.setContentPane(clubPanel);
    }

    private void clubFrame() {
        clubFrame = new JFrame("Select Golf Club - " + username);
        clubFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clubFrame.setSize(1500, 800);
        clubFrame.setLocationRelativeTo(null);
    }

    private JPanel createSimpleClubCard(Putter club, String username) {
        card = new JPanel(new BorderLayout());
        card.setBackground(blueSea);
        card.setBorder(BorderFactory.createLineBorder(brightGreen, 3));

        nameLabel = new JLabel(club.getName(), SwingConstants.CENTER);
        nameLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 22f));
        nameLabel.setForeground(white);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Club icon

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

        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (club instanceof Driver) {
                        selectedClubType = ClubType.DRIVER;
                    } else if (club instanceof Wood) {
                        selectedClubType = ClubType.WOOD;
                    } else {
                        selectedClubType = ClubType.IRON;
                    }
                    clubFrame.dispose();
                    openGameFrame(username);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error selecting club: " + e.getMessage());
                }
            }
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

    private void openGameFrame(String username) {
        strokeCount = 0;
        startTime = System.currentTimeMillis();
        collisionHandled = false;

        gameFrame = new JFrame("Golf Game - " + username);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(1500, 800);
        gameFrame.setLocationRelativeTo(null);

        // Resetear posición inicial de la pelota
        ballPosition = new Point(1350, 95);
        badBallPosition = new Point(900, 400);
        isBallMoving = false;
        isDragging = false;

        JPanel gamePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo
                g.setColor(new Color(34, 139, 34)); // Verde césped
                g.fillRect(0, 0, getWidth(), getHeight());

                // Hoyo
                g.setColor(Color.BLACK);
                g.fillOval(1250, 650, 30, 30);

                //baCat
                Image badImg = badCat.getImage();

                g.setColor(Color.GRAY);
                for (Rectangle rect : obstacles) {
                    g.fillRect(rect.x, rect.y, rect.width, rect.height);
                }

                // good cat

                Image goodImg = goodCat.getImage();
                int diameter = BALL_RADIUS * 2;
                g.drawImage(goodImg,
                        ballPosition.x - BALL_RADIUS,
                        ballPosition.y - BALL_RADIUS,
                        diameter, diameter, this);

                badImg = badCat.getImage();
                g.drawImage(badImg,
                        badBallPosition.x - BALL_RADIUS,
                        badBallPosition.y - BALL_RADIUS,
                        diameter, diameter, this);



                // Línea de dirección (solo cuando se arrastra)
                if (isDragging && dragStart != null && dragEnd != null) {
                    g.setColor(Color.RED);
                    g.drawLine(dragStart.x, dragStart.y, dragEnd.x, dragEnd.y);

                    // Mostrar fuerza (opcional)
                    int distance = (int)dragStart.distance(dragEnd);
                    g.setColor(Color.WHITE);
                    g.drawString("Fuerza: " + distance, dragStart.x, dragStart.y - 10);
                }
            }
        };
        holeRect = new Rectangle(1300, 200, 30, 30);
        ballRect = new Rectangle(ballPosition.x - BALL_RADIUS, ballPosition.y - BALL_RADIUS,
                BALL_RADIUS * 2, BALL_RADIUS * 2);
        //quadrado

        Rectangle bottomWall = new Rectangle(300, gameFrame.getHeight() - 50, gameFrame.getWidth() - 300, 50);

        Rectangle rightWall = new Rectangle(gameFrame.getWidth() - 50, 0, 50, gameFrame.getHeight());

        Rectangle topWall = new Rectangle(300, 0, gameFrame.getWidth() - 300, 50);

        Rectangle leftConnector = new Rectangle(300, 0, 50, gameFrame.getHeight());


        obstacles.add(bottomWall);
        obstacles.add(rightWall);
        obstacles.add(topWall);
        obstacles.add(leftConnector);

        //S
        // Tramos horizontales de la S
        Rectangle sTop = new Rectangle(600, 150, 900, 30);
        Rectangle sMiddle = new Rectangle(300, 350, 900, 30);
        Rectangle sBottom = new Rectangle(600, 550, 900, 30);

        obstacles.add(sTop);
        obstacles.add(sMiddle);
        obstacles.add(sBottom);


        // Mouse listeners
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
                    strokeCount++; // Contar cada golpe
                    gamePanel.repaint();
                }
            }
        });

        gamePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && !isBallMoving) {
                    dragEnd = e.getPoint();
                    gamePanel.repaint();
                }
            }
        });

        // Timer para la animación
        Timer gameLoopTimer = new Timer(16, e -> {
            updatebadCatPosition();
            checkCatCollision();

            if (isBallMoving) {
                updateBallPosition();
                checkHoleCollision();
            }

            gamePanel.repaint();
        });
        gameLoopTimer.start();
        // Botón de volver
        JButton backButton = new JButton("Volver");
        backButton.setBounds(50, 700, 100, 30);
        backButton.addActionListener(e -> {
            gameLoopTimer.stop();
            gameFrame.dispose();
            openUserPage();
        });
        gamePanel.add(backButton);

        gameFrame.setContentPane(gamePanel);
        gameFrame.setVisible(true);
    }

    private void checkCatCollision() {
        if (collisionHandled) return;

        int collisionDistance = BALL_RADIUS * 2;
        double distance = ballPosition.distance(badBallPosition);

        if (distance < collisionDistance) {
            collisionHandled = true;

            showFullScreenImage();  // mostrar imagen del gato malo
            playSound();            // reproducir sonido

            handleGameOver();
        }
    }

    private void handleGameOver() {
        if (gameFrame != null) {
            gameFrame.dispose();
        }

        openUserPage();
    }

    private void showFullScreenImage() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // sin bordes
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // pantalla completa
        frame.setAlwaysOnTop(true); // encima de todo

        // Obtener tamaño de pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        ImageIcon icon = new ImageIcon("resources/img/badCat.jpg");
        Image originalImage = icon.getImage();


        Image scaledImage = originalImage.getScaledInstance(
                screenSize.width, screenSize.height, Image.SCALE_SMOOTH);

        // Crear nuevo icono y JLabel
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        frame.add(label);
        frame.setVisible(true);

        // cerrar automáticamente
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



    private void updatebadCatPosition() {
        double speed = 1.5; // velocidad mayor para evitar quedar parado

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


    private boolean isPointInBall(Point point) {
        int dx = point.x - ballPosition.x;
        int dy = point.y - ballPosition.y;
        return dx * dx + dy * dy <= BALL_RADIUS * BALL_RADIUS;
    }

    private void launchBall() {
        if (dragStart == null || dragEnd == null) return;

        double dragDistance = dragStart.distance(dragEnd);
        // Ajusta estos valores según la sensibilidad que desees
        double force = Math.min(dragDistance / 15.0, 30.0);

        double angle = Math.atan2(dragEnd.y - dragStart.y, dragEnd.x - dragStart.x);

        // Dirección opuesta al arrastre (por eso los valores negativos)
        ballVelocityX = -force * Math.cos(angle);
        ballVelocityY = -force * Math.sin(angle);

        isBallMoving = true;
    }

    private void updateBallPosition() {
        // Actualizar posición
        ballPosition.x += ballVelocityX;
        ballPosition.y += ballVelocityY;
        ballRect.setLocation(ballPosition.x - BALL_RADIUS, ballPosition.y - BALL_RADIUS);

        // Crear el rectángulo de la pelota
        Rectangle ballRect = new Rectangle(
                ballPosition.x - BALL_RADIUS,
                ballPosition.y - BALL_RADIUS,
                BALL_RADIUS * 2,
                BALL_RADIUS * 2
        );

        // Verificar colisión con obstáculos
        for (Rectangle obstacle : obstacles) {
            if (ballRect.intersects(obstacle)) {
                // Rebote básico: invertir la dirección dependiendo del eje más cercano
                Rectangle intersection = ballRect.intersection(obstacle);

                if (intersection.width < intersection.height) {
                    // Rebote horizontal
                    ballVelocityX *= -0.7;
                    // Reposicionar fuera del obstáculo
                    if (ballPosition.x < obstacle.x) {
                        ballPosition.x = obstacle.x - BALL_RADIUS;
                    } else {
                        ballPosition.x = obstacle.x + obstacle.width + BALL_RADIUS;
                    }
                } else {
                    // Rebote vertical
                    ballVelocityY *= -0.7;
                    if (ballPosition.y < obstacle.y) {
                        ballPosition.y = obstacle.y - BALL_RADIUS;
                    } else {
                        ballPosition.y = obstacle.y + obstacle.height + BALL_RADIUS;
                    }
                }

                break; // detener al primer obstáculo
            }
        }

        // Aplicar fricción
        ballVelocityX *= FRICTION;
        ballVelocityY *= FRICTION;

        // Rebotar en los bordes del frame
        if (ballPosition.x - BALL_RADIUS < 0) {
            ballPosition.x = BALL_RADIUS;
            ballVelocityX *= -0.7;
        } else if (ballPosition.x + BALL_RADIUS > gameFrame.getWidth()) {
            ballPosition.x = gameFrame.getWidth() - BALL_RADIUS;
            ballVelocityX *= -0.7;
        }

        if (ballPosition.y - BALL_RADIUS < 0) {
            ballPosition.y = BALL_RADIUS;
            ballVelocityY *= -0.7;
        } else if (ballPosition.y + BALL_RADIUS > gameFrame.getHeight()) {
            ballPosition.y = gameFrame.getHeight() - BALL_RADIUS;
            ballVelocityY *= -0.7;
        }

        // Detener si la velocidad es muy baja
        if (Math.abs(ballVelocityX) < 0.2 && Math.abs(ballVelocityY) < 0.2) {
            ballVelocityX = 0;
            ballVelocityY = 0;
            isBallMoving = false;
        }
    }


    private void checkHoleCollision() {
        if (holeRect.intersects(ballRect)) {
            long endTime = System.currentTimeMillis();
            long durationSeconds = (endTime - startTime) / 1000;

            // Calcular puntos
            int points = calculateScore(strokeCount, durationSeconds);

            // Guardar en la base de datos
            AuthService.saveScore(username, selectedClubType, strokeCount, durationSeconds, points);

            // Mostrar frame de resultados
            showGameResults(username, selectedClubType, strokeCount, durationSeconds, points);

            // Detener el juego
            isBallMoving = false;
        }
    }
    private int calculateScore(int strokes, long duration) {
        // Fórmula mejorada para calcular la puntuación
        double score = 10000.0 / (1 + strokes * 0.5 + duration * 0.1);
        return (int) Math.round(score);
    }
    private void showGameResults(String username, ClubType clubType, int strokes, long durationSeconds, int points) {
        JFrame resultsFrame = new JFrame("Resultados del Juego");
        resultsFrame.setSize(800, 600);
        resultsFrame.setLocationRelativeTo(gameFrame);
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(darkBlue);

        // Título
        JLabel titleLabel = new JLabel("¡Hoyo conseguido!", SwingConstants.CENTER);
        titleLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 24f));
        titleLabel.setForeground(brightGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        statsPanel.setBackground(darkBlue);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        addStat(statsPanel, "Jugador:", username);
        addStat(statsPanel, "Palo utilizado:", clubType.toString());
        addStat(statsPanel, "Golpes realizados:", String.valueOf(strokes));
        addStat(statsPanel, "Tiempo:", formatTime(durationSeconds));
        addStat(statsPanel, "Puntuación:", String.valueOf(points));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(darkBlue);

        JButton backButton = new JButton("Volver al Menú");
        setupButton(backButton, 200, 50, orange, 16f);
        addHoverEffect(backButton, orange, brightGreen);

        JButton scoresButton = new JButton("Ver Puntuaciones");
        setupButton(scoresButton, 200, 50, brightGreen, 16f);
        addHoverEffect(scoresButton, brightGreen, orange);

        // Acciones de los botones
        backButton.addActionListener(e -> {
            resultsFrame.dispose();
            gameFrame.dispose();
            openUserPage();
        });

        scoresButton.addActionListener(e -> {
            resultsFrame.dispose();
            gameFrame.dispose();
            openScoresPage();
        });

        buttonPanel.add(backButton);
        buttonPanel.add(scoresButton);

        // Ensamblar el frame
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        resultsFrame.add(mainPanel);
        resultsFrame.setVisible(true);
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


    private void addSimpleStat(JPanel panel, String text) {
        JLabel statLabel = new JLabel(text, SwingConstants.CENTER);
        statLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        statLabel.setForeground(brightGreen);
        panel.add(statLabel);
    }

    public static void main(String[] args) {
        try {
            new Principal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void showScoreFrame(int score) {
        JFrame scoreFrame = new JFrame("¡Hoyo conseguido!");
        scoreFrame.setSize(600, 400);
        scoreFrame.setLocationRelativeTo(null);
        scoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(darkBlue);

        // Título
        JLabel titleLabel = new JLabel("¡Felicidades!", SwingConstants.CENTER);
        titleLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 30f));
        titleLabel.setForeground(brightGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Puntuación
        JLabel scoreLabel = new JLabel("Puntuación: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 24f));
        scoreLabel.setForeground(white);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(darkBlue);

        JButton playAgainButton = new JButton("Jugar otra vez");
        setupButton(playAgainButton, 200, 50, brightGreen, 16f);
        addHoverEffect(playAgainButton, brightGreen, orange);

        JButton exitButton = new JButton("Salir");
        setupButton(exitButton, 200, 50, orange, 16f);
        addHoverEffect(exitButton, orange, brightGreen);

        // Acciones de los botones
        playAgainButton.addActionListener(e -> {
            scoreFrame.dispose();
            gameFrame.dispose();
            openClubSelectionPage();
        });

        exitButton.addActionListener(e -> {
            scoreFrame.dispose();
            gameFrame.dispose();
            openUserPage();
        });

        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scoreLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        scoreFrame.add(panel);
        scoreFrame.setVisible(true);
    }
}