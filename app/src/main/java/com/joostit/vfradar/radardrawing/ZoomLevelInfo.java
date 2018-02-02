package com.joostit.vfradar.radardrawing;

/**
 * Created by Joost on 17-1-2018.
 */

public class ZoomLevelInfo {
    public double ringRadius1;
    public double ringRadius2;
    public double ringRadius3;
    public double rangeRadius;

    public boolean isDistanceWithinLastRing(double distanceM){
        return distanceM < ringRadius3;
    }
}
