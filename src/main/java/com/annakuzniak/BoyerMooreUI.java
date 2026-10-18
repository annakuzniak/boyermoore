package com.annakuzniak;

import java.awt.Color;
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
import javax.swing.SwingWorker;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class BoyerMooreUI extends JFrame {

    private JTextField patternField;
    private JButton searchButton;
    private JButton analyzeButton;
    private String bookText;
    private JLabel resultLabel;
    private JLabel statisticsLabel;
    private JTextArea matchesArea;
    private JLabel complexityLabel;

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
        analyzeButton = new JButton("Analyze Complexity");

        searchPanel.add(patternLabel);
        searchPanel.add(patternField);
        searchPanel.add(searchButton);
        searchPanel.add(analyzeButton);
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
        complexityLabel = new JLabel(
                "Time complexity: O(n*m) worst case | "
                        + "Preprocessing: O(m) | "
                        + "Auxiliary space: O(m)"
        );
        resultsPanel.add(complexityLabel);

        JPanel resultsWrapper = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );
        resultsWrapper.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 100)
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

            searchButton.setEnabled(false);
            resultLabel.setText("Searching...");
            statisticsLabel.setText("Search statistics:");
            matchesArea.setText("");

            SwingWorker<Void, Void> worker = new SwingWorker<>() {

                private List<Integer> matches;
                private SearchStatistics stats;

                @Override
                protected Void doInBackground() {
                    BoyerMoore boyerMoore = new BoyerMoore();
                    matches = boyerMoore.search(bookText, pattern);
                    stats = boyerMoore.getSearchStatistics();
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();

                        resultLabel.setText("Matches found: " + matches.size());

                        double timeMilliseconds =
                                stats.getExecutionTimeNanos() / 1_000_000.0;

                        statisticsLabel.setText(
                                "Comparisons: " + stats.getCharacterComparisons()
                                        + " | Shifts: " + stats.getPatternShifts()
                                        + " | Time: "
                                        + String.format("%.3f", timeMilliseconds)
                                        + " ms"
                        );


                        StringBuilder results = new StringBuilder();
                        List<int[]> highlightRanges = new java.util.ArrayList<>();

                        for (int index : matches) {

                            int start = Math.max(0, index - 40);
                            int end = Math.min(
                                    bookText.length(),
                                    index + pattern.length() + 40
                            );

                            String fragment = bookText.substring(start, end)
                                    .replace('\n', ' ')
                                    .replace('\r', ' ');

                            results.append("Position: ")
                                    .append(index)
                                    .append("\n");

                            int fragmentStart = results.length();

                            results.append(fragment)
                                    .append("\n\n");

                            int highlightStart = fragmentStart + (index - start);
                            int highlightEnd = highlightStart + pattern.length();

                            highlightRanges.add(new int[]{
                                    highlightStart,
                                    highlightEnd
                            });
                        }

                        matchesArea.setText(results.toString());

                        matchesArea.getHighlighter().removeAllHighlights();

                        for (int[] range : highlightRanges) {
                            highlightMatch(range[0], range[1]);
                        }

                        matchesArea.setCaretPosition(0);


                    } catch (Exception ex) {
                        resultLabel.setText("Search failed.");
                        JOptionPane.showMessageDialog(
                                BoyerMooreUI.this,
                                "Search error: " + ex.getMessage(),
                                "Search Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        searchButton.setEnabled(true);
                    }
                }
            };

            worker.execute();
        });

//      complexity analysis button listener
        analyzeButton.addActionListener(event -> {

            if (bookText == null) {
                resultLabel.setText("Book is not loaded.");
                return;
            }

            String pattern = patternField.getText();

            if (pattern.isEmpty()) {
                resultLabel.setText("Please enter a pattern.");
                return;
            }

            analyzeButton.setEnabled(false);
            matchesArea.setText("Analyzing complexity...");

            SwingWorker<List<String>, Void> worker = new SwingWorker<>() {

                @Override
                protected List<String> doInBackground() {
                    ComplexityAnalyzer analyzer = new ComplexityAnalyzer();
                    return analyzer.analyze(bookText, pattern);
                }

                @Override
                protected void done() {
                    try {
                        List<String> results = get();

                        matchesArea.setText(String.join("\n", results));
                        matchesArea.setCaretPosition(0);

                        resultLabel.setText("Complexity analysis completed.");
                        statisticsLabel.setText(
                                "Experimental results for different text lengths"
                        );

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                BoyerMooreUI.this,
                                "Complexity analysis failed: " + ex.getMessage(),
                                "Analysis Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        analyzeButton.setEnabled(true);
                    }
                }
            };

            worker.execute();
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

    private void highlightMatch(int start, int end) {

        try {
            Highlighter highlighter = matchesArea.getHighlighter();

            highlighter.addHighlight(
                    start,
                    end,
                    new DefaultHighlighter.DefaultHighlightPainter(
                            Color.YELLOW
                    )
            );

        } catch (Exception e) {
            System.err.println("Could not highlight match: " + e.getMessage());
        }
    }


}
