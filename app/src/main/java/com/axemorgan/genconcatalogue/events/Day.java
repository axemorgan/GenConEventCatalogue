package com.axemorgan.genconcatalogue.events;

public enum Day {
    FIRST(1533182400),
    SECOND(1533268800),
    THIRD(1533355200),
    FORTH(1533441600);

    private final long startTime;

    Day(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }
}
