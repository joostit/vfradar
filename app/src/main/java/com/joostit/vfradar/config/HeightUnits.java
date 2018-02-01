package com.joostit.vfradar.config;

/**
 * Created by Joost on 1-2-2018.
 */

public enum HeightUnits {
    Meter(1),
    Feet(2);


    private final int value;

    HeightUnits(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
