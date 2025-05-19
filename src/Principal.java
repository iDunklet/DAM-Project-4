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
    private JButton playButton;
    private JLabel logoLabel;
    private JButton exitButton;
    private JLabel gato1Label;

    public Principal() throws IOException, FontFormatException {

        pressStart2P = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(12f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);

        this.panelMain();
        this.playButton();
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

    private void playButton() {
        playButton = new JButton("Login");
        playButton.setBackground(null);
        playButton.setFont(pressStart2P.deriveFont(Font.PLAIN, 50f));
        playButton.setSize(500, 100);
        playButton.setForeground(brightGreen);
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        playButton.setContentAreaFilled(false);
        int x = (1500 - playButton.getWidth()) / 2;
        playButton.setLocation(x, 500);

        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButton.setForeground(orange);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButton.setForeground(brightGreen);
            }
        });
        panelMain.add(playButton);
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
