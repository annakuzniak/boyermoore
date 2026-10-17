package com.annakuzniak;

import java.util.HashMap;
import java.util.Map;

public class BoyerMoore {

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

}



