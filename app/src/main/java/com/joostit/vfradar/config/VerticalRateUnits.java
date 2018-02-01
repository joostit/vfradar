package com.joostit.vfradar.config;

/**
 * Created by Joost on 1-2-2018.
 */

public enum VerticalRateUnits {
    MeterPerSecond(0),
    FeetPerMinute(1);


    private final int value;

    VerticalRateUnits(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
