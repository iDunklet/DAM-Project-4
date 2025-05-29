package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class Main {
    private static Clip backgroundClip;

    public static void main(String[] args) {
        playBackgroundMusic();
        try {
            new MainMenuView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void playBackgroundMusic() {
        try {
            File musicPath = new File("resources/audio/music.wav");
            if (musicPath.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicPath);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();
            } else {
                System.err.println("No se encontró el archivo de música.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}