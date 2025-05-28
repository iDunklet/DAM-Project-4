package view.Main;

import view.MainMenuView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            new MainMenuView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
