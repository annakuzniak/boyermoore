package com.annakuzniak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMoore {

    private SearchStatistics searchStatistics = new SearchStatistics();

    public Map<Character, Integer> buildLastOccurrenceTable(String pattern) {

        Map<Character, Integer> lastOccurrence = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            lastOccurrence.put(pattern.charAt(i), i);
        }

        return lastOccurrence;
    }

    public int calculateBadCharacterShift(
            Map<Character, Integer> lastOccurrence,
            char mismatchedCharacter,
            int mismatchIndex) {

        int lastIndex = lastOccurrence.getOrDefault(
                mismatchedCharacter, -1
        );

        return Math.max(1, mismatchIndex - lastIndex);
    }

    public int[] buildGoodSuffixShiftTable(String pattern) {

        int patternLength = pattern.length();

        int[] goodSuffixShift = new int[patternLength + 1];
        int[] borderPosition = new int[patternLength + 1];

        int i = patternLength;
        int j = patternLength + 1;

        borderPosition[i] = j;

        while (i > 0) {
            while (j <= patternLength && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {

                if (goodSuffixShift[j] == 0) {
                    goodSuffixShift[j] = j - i;
                }

                j = borderPosition[j];
            }

            i--;
            j--;

            borderPosition[i] = j;
        }

        j = borderPosition[0];

        for (int k = 0; k <= patternLength; k++) {

            if (goodSuffixShift[k] == 0) {
                goodSuffixShift[k] = j;
            }

            if (k == j) {
                j = borderPosition[j];
            }
        }

        return goodSuffixShift;
    }

    public List<Integer> search(String text, String pattern) {

        searchStatistics = new SearchStatistics();
        long startTime = System.nanoTime();
        List<Integer> matches = new ArrayList<>();

        if (pattern.isEmpty()) {
            searchStatistics.setExecutionTimeNanos(System.nanoTime() - startTime);
            return matches;
        }

        int textLength = text.length();
        int patternLength = pattern.length();
        int[] goodSuffixShift = buildGoodSuffixShiftTable(pattern);

        Map<Character, Integer> lastOccurrence = buildLastOccurrenceTable(pattern);

        int shift = 0;

        while (shift <= textLength - patternLength) {

            int j = patternLength - 1;

            while (j >= 0) {

                searchStatistics.incrementCharacterComparisons();

                if (pattern.charAt(j) != text.charAt(shift + j)) {
                    break;
                }

                j--;
            }

            if (j < 0) {
                matches.add(shift);
                shift += goodSuffixShift[0];
                searchStatistics.incrementPatternShifts();
            } else {
                char mismatchedCharacter = text.charAt(shift + j);

                int badCharacterShift = calculateBadCharacterShift(lastOccurrence, mismatchedCharacter, j);

                int goodSuffixShiftValue = goodSuffixShift[j + 1];

                shift += Math.max(badCharacterShift, goodSuffixShiftValue);
                searchStatistics.incrementPatternShifts();
            }

        }

        long endTime = System.nanoTime();
        searchStatistics.setExecutionTimeNanos(endTime - startTime);

        return matches;
    }

    public SearchStatistics getSearchStatistics() {
        return searchStatistics;
    }

}
