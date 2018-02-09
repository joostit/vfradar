package com.joostit.vfradar.geo;

import com.joostit.vfradar.utilities.Numbers;

/**
 * Created by Joost on 5-2-2018.
 */

public class GeoAltitude {

    private static final double ft2m = 0.3048;

    private double bottomAltitudeM = 0;
    private double topAltitudeM = 0;

    public void setBottomM(Double bottom){
        if(bottom != null) {
            bottomAltitudeM = bottom;
        }
    }

    public void setTopM(Double top){
        if(top != null) {
            topAltitudeM = top;
        }
    }

    public void setBottomFt(double bottomFeet){
        bottomAltitudeM = feetToMeter(bottomFeet);
    }

    public void setTopFt(double topFeet){
        topAltitudeM =  feetToMeter(topFeet);
    }

    public double getBottomFt(){
        return meterToFeet(bottomAltitudeM);
    }

    public double getTopFt(){
        return meterToFeet(topAltitudeM);
    }

    public static double meterToFeet(double meter){
        return meter / ft2m;
    }

    public static double feetToMeter(double feet){
        return feet * ft2m;
    }

    public boolean hasAltitudeInfo(){
        return !(Numbers.isDoubleZero(bottomAltitudeM) && Numbers.isDoubleZero(topAltitudeM));
    }
}
