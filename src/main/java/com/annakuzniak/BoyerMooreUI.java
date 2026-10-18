package com.annakuzniak;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BoyerMooreUI extends JFrame {

    private JTextField patternField;
    private JButton searchButton;
    private String bookText;
    private JLabel resultLabel;
    private JLabel statisticsLabel;
    private JTextArea matchesArea;

    public BoyerMooreUI() {
        setTitle("Boyer-Moore String Search");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

//      main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//      search panel
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        searchPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 60)
        );

        JLabel patternLabel = new JLabel("Search pattern:");

        patternField = new JTextField(25);
        searchButton = new JButton("Search");

        searchPanel.add(patternLabel);
        searchPanel.add(patternField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel);

//      results panel
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(
                new BoxLayout(resultsPanel, BoxLayout.Y_AXIS)
        );

        resultLabel = new JLabel("Please, type a pattern and click Search:");
        statisticsLabel = new JLabel("Search statistics:");

        resultsPanel.add(resultLabel);
        resultsPanel.add(statisticsLabel);

        JPanel resultsWrapper = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );
        resultsWrapper.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 70)
        );

        resultsWrapper.add(resultsPanel);

        mainPanel.add(resultsWrapper);

//      display matches area
        matchesArea = new JTextArea();

        matchesArea.setEditable(false);
        matchesArea.setLineWrap(true);
        matchesArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(matchesArea);

        mainPanel.add(scrollPane);
        add(mainPanel);

//      search button listener
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

            StringBuilder results = new StringBuilder();
            for (Integer index : matches) {
                int start = Math.max(0, index - 40);
                int end = Math.min(
                        bookText.length(),
                        index + pattern.length() + 40
                );
                String fragment = bookText.substring(start, end);
                fragment = fragment.replace('\n', ' ')
                        .replace('\r', ' ');

                results.append("Position")
                        .append(index)
                        .append("\n")
                        .append(fragment)
                        .append("\n\n");
            }
            matchesArea.setText(results.toString());
            matchesArea.setCaretPosition(0);

            SearchStatistics stats = boyerMoore.getSearchStatistics();

            double timeMilliseconds = stats.getExecutionTimeNanos() / 1_000_000.0;

            statisticsLabel.setText(
                    "Comparisons: " + stats.getCharacterComparisons()
                            + " | Shifts: " + stats.getPatternShifts()
                            + " | Time: " + String.format("%.3f", timeMilliseconds) + " ms"
            );

        });

//      book loading
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
