package com.joostit.vfradar.utilities;

/**
 * Created by Joost on 16-1-2018.
 */

public final class Convert {

    public static double ft2m(double feet) {
        return feet / 0.3048;
    }

    public static double m2ft(double meters) {
        return meters * 0.3048;
    }

    public static double kn2kmh(double knots) {
        return knots * 1.85200;
    }

    public static double kmh2kn(double kilometersPerHour) {
        return kilometersPerHour * 1.85200;
    }

}
