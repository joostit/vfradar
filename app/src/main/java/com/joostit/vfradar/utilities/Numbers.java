package com.joostit.vfradar.utilities;

/**
 * Created by Joost on 20-1-2018.
 */

public final class Numbers {


    public static double round(double input, int decimals) {

        double factor;
        if (decimals > 0) {
            factor = Math.pow(10, decimals);
        } else {
            factor = 0;
        }

        return Math.round(input * (double) factor) / (double) factor;
    }


}
