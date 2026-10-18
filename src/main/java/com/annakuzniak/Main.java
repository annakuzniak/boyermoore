package com.annakuzniak;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            BoyerMooreUI ui = new BoyerMooreUI();
            ui.setVisible(true);
        });
    }
}
