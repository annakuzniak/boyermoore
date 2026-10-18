package com.annakuzniak;

public class SearchStatistics {

    private long characterComparisons;
    private long patternShifts;
    private long executionTimeNanos;

    public void incrementCharacterComparisons() {
        characterComparisons++;
    }

    public long getCharacterComparisons() {
        return characterComparisons;
    }

    public void incrementPatternShifts() {
        patternShifts++;
    }

    public long getPatternShifts() {
        return patternShifts;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public void setExecutionTimeNanos(long executionTimeNanos) {
        this.executionTimeNanos = executionTimeNanos;
    }

}
