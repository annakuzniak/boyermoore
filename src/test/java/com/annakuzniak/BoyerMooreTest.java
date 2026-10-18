package com.annakuzniak;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

    @Test
    void shouldBuildGoodSuffixShiftTable() {

        BoyerMoore boyerMoore = new BoyerMoore();
        String pattern = "ABAB";

        int[] goodSuffixShift = boyerMoore.buildGoodSuffixShiftTable(pattern);
        int[] expected = {2, 2, 2, 4, 1};

        assertArrayEquals(expected, goodSuffixShift);

    }


    @Test
    void shouldBuildGoodSuffixShiftTableWithoutRepeatedCharacters() {

        BoyerMoore boyerMoore = new BoyerMoore();
        String pattern = "ABCD";

        int[] goodSuffixShift =
                boyerMoore.buildGoodSuffixShiftTable(pattern);
        int[] expected = {4, 4, 4, 4, 1};

        assertArrayEquals(expected, goodSuffixShift);
    }

    @Test
    void shouldBuildGoodSuffixShiftTableWithPrefixSuffixOverlap() {

        BoyerMoore boyerMoore = new BoyerMoore();
        String pattern = "ABCDAB";

        int[] goodSuffixShift = boyerMoore.buildGoodSuffixShiftTable(pattern);
        int[] expected = {4, 4, 4, 4, 4, 6, 1};

        assertArrayEquals(expected, goodSuffixShift);
    }

    @Test
    void shouldBuildGoodSuffixShiftTableWithRepeatedCharacters() {

        BoyerMoore boyerMoore = new BoyerMoore();
        String pattern = "AAAA";

        int[] goodSuffixShift = boyerMoore.buildGoodSuffixShiftTable(pattern);
        int[] expected = {1, 1, 2, 3, 4};

        assertArrayEquals(expected, goodSuffixShift);
    }

}
