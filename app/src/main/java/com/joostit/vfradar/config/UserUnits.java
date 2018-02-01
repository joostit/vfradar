package com.joostit.vfradar.config;

/**
 * Created by Joost on 1-2-2018.
 */

public final class UserUnits {

    private double siteElevation = SysConfig.getSiteElevation();
    private HeightUnits unit = SysConfig.getHeightUnits();

    public double getHeight(double feet){

        switch (unit){
            case Feet:
                return feet;

            case Meter:
                return Math.round(feet / 0.3048);
        }
        return 0;
    }

    public String getHeightUnitIndicator(){

        switch (unit){
            case Feet:
                return "ft";

            case Meter:
                return "m";
        }
        return "";
    }

}
