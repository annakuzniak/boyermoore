package com.annakuzniak;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldRetunNoMatchesForEmptyPattern() {
        BoyerMoore boyerMoore = new BoyerMoore();
        List<Integer> matches = boyerMoore.search(
                "TAJEMNICZY OGRÓD",
                ""
        );

        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldFindOverlappingMatches() {
        BoyerMoore boyerMoore = new BoyerMoore();
        List<Integer> matches = boyerMoore.search(
                "ABABAB",
                "ABAB"
        );
        assertEquals(List.of(0, 2), matches);
    }

    @Test
    void shouldFindMatchAfterMismatch() {
        BoyerMoore boyerMoore = new BoyerMoore();
        List<Integer> matches = boyerMoore.search(
                "ABCXABAB",
                "ABAB"
        );
        assertEquals(List.of(4), matches);
    }

    @Test
    void shouldReturnNoMatchesWhenPatternIsAbsentInText() {
        BoyerMoore boyerMoore = new BoyerMoore();
        List<Integer> matches = boyerMoore.search(
                "ABCDEF",
                "XYZ"
        );
        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldReturnNoMatchesWhenPatternIsLongerThanText() {

        BoyerMoore boyerMoore = new BoyerMoore();

        List<Integer> matches = boyerMoore.search(
                "ABC",
                "ABCDE"
        );

        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldFindMatchesWithRepeatedCharacters() {

        BoyerMoore boyerMoore = new BoyerMoore();

        List<Integer> matches = boyerMoore.search(
                "AAAAAA",
                "AAAA"
        );

        assertEquals(List.of(0, 1, 2), matches);
    }

    @Test
    void shouldFindMatchesAfterPartialMatches() {

        BoyerMoore boyerMoore = new BoyerMoore();

        List<Integer> matches = boyerMoore.search(
                "ABCDABXABCDAB",
                "ABCDAB"
        );

        assertEquals(List.of(0, 7), matches);
    }

    @Test
    void shouldFindMatchesInRealDataset() throws Exception {

        DataSetLoader loader = new DataSetLoader();
        String text = loader.loadBook();
        String pattern = "ogród";

        BoyerMoore boyerMoore = new BoyerMoore();

        List<Integer> actual = boyerMoore.search(text, pattern);
        List<Integer> expected = new ArrayList<>();

        int index = text.indexOf(pattern);

        while (index != -1) {
            expected.add(index);
            index = text.indexOf(pattern, index + 1);
        }

        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
    }

}
