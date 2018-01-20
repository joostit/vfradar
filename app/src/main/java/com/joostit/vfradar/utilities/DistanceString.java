package com.joostit.vfradar.utilities;

/**
 * Created by Joost on 17-1-2018.
 */

public final class DistanceString {

    public static final String getString(double distanceM) {
        if (distanceM < 1000) {

            int m = (int) Math.round(distanceM);

            return m + " m";
        } else if (distanceM < 10000) {
            double km = (int) Math.round(distanceM / 100);

            return (km / 10) + " km";
        } else {
            int km = (int) Math.round(distanceM / 1000);

            return km + " km";
        }
    }


}
