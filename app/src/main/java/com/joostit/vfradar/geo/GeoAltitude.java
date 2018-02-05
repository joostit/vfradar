package com.joostit.vfradar.geo;

/**
 * Created by Joost on 5-2-2018.
 */

public class GeoAltitude {

    private static final double ft2m = 0.3048;

    private double bottomAltitudeM;
    private double topAltitudeM;
    private boolean hasAltitudeInfo = false;

    public boolean hasAltitudeInfo(){
        return hasAltitudeInfo;
    }


    public void setBottomM(Double bottom){
        if(bottom != null) {
            bottomAltitudeM = bottom;
            hasAltitudeInfo = true;
        }
    }

    public void setTopM(Double top){
        if(top != null) {
            topAltitudeM = top;
            hasAltitudeInfo = true;
        }
    }

    public void setBottomFt(double bottomFeet){
        bottomAltitudeM = feetToMeter(bottomFeet);
        hasAltitudeInfo = true;
    }

    public void setTopFt(double topFeet){
        topAltitudeM =  feetToMeter(topFeet);
        hasAltitudeInfo = true;
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
}
