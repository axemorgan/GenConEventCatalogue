package com.axemorgan.genconcatalogue.events;

public enum Day {
    FIRST(1502942400), // Aug 17 2017 GMT-4:00
    SECOND(1503028800), // Aug 18 2017
    THIRD(1503115200), // Aug 19 2017
    FORTH(1503201600); // Aug 20 2017

    private final long startTime;

    Day(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return startTime + 60 * 60 * 24;
    }
}
