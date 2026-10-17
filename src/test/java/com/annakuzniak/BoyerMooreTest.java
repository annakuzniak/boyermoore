package com.annakuzniak;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoyerMooreTest {

    @Test
    void shouldShiftWhenMismatchedCharacterIsAbsent() {
        BoyerMoore boyerMoore = new BoyerMoore();

        String pattern = "KOKOS";

        Map<Character, Integer> lastOccurrence = boyerMoore.buildLastOccurrenceTable(pattern);

        int shift = boyerMoore.calculateBadCharacterShift(
                lastOccurrence,
                'A',
                3
        );

        assertEquals(4, shift);

    }

    @Test
    void shouldShiftWhenMismatchedCharacterIsPresent() {
        BoyerMoore boyerMoore = new BoyerMoore();

        String pattern = "KOKOS";

        Map<Character, Integer> lastOccurrence = boyerMoore.buildLastOccurrenceTable(pattern);
        int shift = boyerMoore.calculateBadCharacterShift(
                lastOccurrence,
                'K',
                3
        );

        assertEquals(1, shift);
    }
}
