package com.annakuzniak;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BoyerMooreUI extends JFrame {

    private JTextField patternField;
    private JButton searchButton;
    private String bookText;
    private JLabel resultLabel;
    private JLabel statisticsLabel;


    public BoyerMooreUI() {
        setTitle("Boyer-Moore String Search");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));

        JLabel patternLabel = new JLabel("Search pattern:");

        patternField = new JTextField(25);

        searchButton = new JButton("Search");

        mainPanel.add(patternLabel);
        mainPanel.add(patternField);
        mainPanel.add(searchButton);
        resultLabel = new JLabel("Please, type a pattern and click Search:");
        mainPanel.add(resultLabel);
        statisticsLabel = new JLabel("Search statistics:");
        mainPanel.add(statisticsLabel);

        add(mainPanel);

        searchButton.addActionListener(event -> {
            if (bookText == null) {
                resultLabel.setText("Book is not loaded");
                return;
            }

            String pattern = patternField.getText();
            if (pattern.isEmpty()) {
                resultLabel.setText("Please enter a pattern");
                return;
            }

            BoyerMoore boyerMoore = new BoyerMoore();
            List<Integer> matches = boyerMoore.search(bookText, pattern);
            resultLabel.setText("Matches found: " + matches.size());

            SearchStatistics stats = boyerMoore.getSearchStatistics();

            double timeMilliseconds = stats.getExecutionTimeNanos() / 1_000_000.0;

            statisticsLabel.setText(
                    "Comparisons: " + stats.getCharacterComparisons()
                            + " | Shifts: " + stats.getPatternShifts()
                            + " | Time: " + String.format("%.3f", timeMilliseconds) + " ms"
            );

        });

        try {
            DataSetLoader loader = new DataSetLoader();
            bookText = loader.loadBook();

            System.out.println("Book loaded successfully.");
            System.out.println("Text length: " + bookText.length());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load the book: " + e.getMessage(),
                    "Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
