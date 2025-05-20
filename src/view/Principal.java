package view;

import controllers.dbstuff.AuthService;
import controllers.dbstuff.ClubType;
import model.Driver;
import model.Iron;
import model.Putter;
import model.Wood;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class Principal {
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

    // CONSTRUCTOR PRINCIPAL
    public Principal() {
        try {
            loadFont();
            initMainComponents();
        } catch (IOException | FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading resources: " + e.getMessage());
            System.exit(1);
        }
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
            JFrame frame = new JFrame("Global Scores");
            frame.setSize(1700, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
                        frame.dispose();
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

            frame.add(panel);
            frame.setVisible(true);
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
            String username = userField.getText().trim();

            JFrame clubFrame = new JFrame("Select Golf Club - " + username);
            clubFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            clubFrame.setSize(1500, 800);
            clubFrame.setLocationRelativeTo(null);

            JPanel clubPanel = new JPanel(null);
            clubPanel.setBackground(darkBlue);
            clubFrame.setContentPane(clubPanel);

            // Title
            JLabel titleLabel = new JLabel("SELECT YOUR CLUB", SwingConstants.CENTER);
            setupLabel(titleLabel, 800, 60, 350, 50, 30f);
            clubPanel.add(titleLabel);

            // Create the 3 basic clubs
            Putter driver = new Driver();
            Putter wood = new Wood(false);
            Putter iron = new Iron(5);

            // Display the 3 club cards
            JPanel driverCard = createSimpleClubCard(driver, username);
            driverCard.setBounds(250, 150, 300, 250);
            clubPanel.add(driverCard);

            JPanel woodCard = createSimpleClubCard(wood, username);
            woodCard.setBounds(600, 150, 300, 250);
            clubPanel.add(woodCard);

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

    private JPanel createSimpleClubCard(Putter club, String username) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(blueSea);
        card.setBorder(BorderFactory.createLineBorder(brightGreen, 3));

        // Club name
        JLabel nameLabel = new JLabel(club.getName(), SwingConstants.CENTER);
        nameLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 22f));
        nameLabel.setForeground(white);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Club icon
        JLabel iconLabel = new JLabel(getClubIcon(club), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

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
                    ClubType clubType;
                    if (club instanceof Driver) {
                        clubType = ClubType.DRIVER;
                    } else if (club instanceof Wood) {
                        clubType = ClubType.WOOD;
                    } else {
                        clubType = ClubType.IRON;
                    }
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
        card.add(iconLabel, BorderLayout.CENTER);
        card.add(statsPanel, BorderLayout.CENTER);
        card.add(selectButton, BorderLayout.SOUTH);

        return card;
    }

    private void openGameFrame(String username) {
        try {
            JFrame gameFrame = new JFrame("Golf Game - " + username);
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setSize(1500, 800);
            gameFrame.setLocationRelativeTo(null);

            JPanel gamePanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Puedes dibujar aquí el fondo o elementos del juego
                    g.setColor(new Color(34, 139, 34)); // Verde césped
                    g.fillRect(0, 0, getWidth(), getHeight());

                    // Ejemplo: dibujar un hoyo
                    g.setColor(Color.BLACK);
                    g.fillOval(1300, 350, 30, 30);
                }
            };
            gamePanel.setBackground(darkBlue);
            gameFrame.setContentPane(gamePanel);

            // Título
            JLabel title = new JLabel("GOLF GAME", SwingConstants.CENTER);
            setupLabel(title, 800, 60, 350, 30, 30f);
            gamePanel.add(title);

            // Botón Back
            JButton backButton = new JButton("Back");
            setupButton(backButton, 200, 50, orange, 20f);
            backButton.setLocation(50, 680);
            addHoverEffect(backButton, orange, brightGreen);

            backButton.addActionListener(e -> {
                gameFrame.dispose();
                openUserPage();
            });

            gamePanel.add(backButton);

            // Aquí más adelante puedes agregar objetos como la pelota, el palo, etc.

            gameFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error opening game frame: " + e.getMessage());
        }
    }


    private String getClubIcon(Putter club) {
        if (club instanceof Driver) {
            return "🏌️";
        } else if (club instanceof Wood) {
            return "🌲";
        } else {
            return "⚙️";
        }
    }

    private void addSimpleStat(JPanel panel, String text) {
        JLabel statLabel = new JLabel(text, SwingConstants.CENTER);
        statLabel.setFont(pressStart2P.deriveFont(Font.PLAIN, 14f));
        statLabel.setForeground(brightGreen);
        panel.add(statLabel);
    }

    // MÉTODO MAIN
    public static void main(String[] args) {
        try {
            new Principal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}