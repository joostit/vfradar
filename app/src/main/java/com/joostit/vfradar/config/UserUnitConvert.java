package com.joostit.vfradar.config;

/**
 * Created by Joost on 1-2-2018.
 */

public final class UserUnitConvert {


    public int getHeight(double gpsAltitudeFeet){
        HeightUnits unit = SysConfig.getHeightUnits();
        double siteElevation = SysConfig.getSiteElevation();
        switch (unit){
            case Feet:
                return (int) Math.round(gpsAltitudeFeet - (siteElevation / 0.3048));

            case Meter:
                return (int) Math.round((gpsAltitudeFeet * 0.3048) - siteElevation);
        }
        return 0;
    }

    public String getHeightUnitIndicator(){
        HeightUnits unit = SysConfig.getHeightUnits();
        double siteElevation = SysConfig.getSiteElevation();

        switch (unit){
            case Feet:
                return "ft";

            case Meter:
                return "m";
        }
        return "";
    }

}
