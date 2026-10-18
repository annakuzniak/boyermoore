package com.annakuzniak;

import java.util.ArrayList;
import java.util.List;

public class ComplexityAnalyzer {


    public List<String> analyze(String text, String pattern) {

        List<String> results = new ArrayList<>();
        long previousComparisons = 0;
        int previousTextLength = 0;

        int[] textLengths = {
                1_000,
                5_000,
                10_000,
                50_000,
                100_000
        };

        for (int length : textLengths) {

            int actualLength = Math.min(length, text.length());
            String sample = text.substring(0, actualLength);
            BoyerMoore boyerMoore = new BoyerMoore();
            List<Integer> matches = boyerMoore.search(sample, pattern);
            SearchStatistics statistics = boyerMoore.getSearchStatistics();

            double comparisonsPerCharacter =
                    actualLength == 0
                            ? 0.0
                            : (double) statistics.getCharacterComparisons()
                            / actualLength;

            String result = String.format(
                    "Length: %d | Comparisons: %d | Shifts: %d"
                            + " | Matches: %d | Comparisons/n: %.4f",
                    actualLength,
                    statistics.getCharacterComparisons(),
                    statistics.getPatternShifts(),
                    matches.size(),
                    comparisonsPerCharacter
            );

            results.add(result);

            if (previousTextLength > 0 && previousComparisons > 0) {
                double textGrowth =
                        (double) actualLength / previousTextLength;
                double comparisonGrowth =
                        (double) statistics.getCharacterComparisons()
                                / previousComparisons;
                results.add(String.format(
                        "Text growth: %.2fx | Comparison growth: %.2fx",
                        textGrowth,
                        comparisonGrowth
                ));
            }

            previousTextLength = actualLength;
            previousComparisons = statistics.getCharacterComparisons();

        }

        results.add("");
        results.add("Complexity assessment:");
        results.add("Theoretical worst-case search complexity: O(n*m).");
        results.add("Preprocessing complexity: O(m).");
        results.add("Auxiliary space complexity: O(m).");

        return results;
    }

}
