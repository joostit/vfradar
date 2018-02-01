package com.joostit.vfradar.config;

import com.joostit.vfradar.utilities.Numbers;

/**
 * Created by Joost on 1-2-2018.
 */

public final class UserUnitConvert {

    private final double feetToMeter = 0.3048;

    public int getHeight(double gpsAltitudeFeet) {
        HeightUnits unit = SysConfig.getHeightUnit();
        double siteElevation = SysConfig.getSiteElevation();
        switch (unit) {
            case Feet:
                return (int) Math.round(gpsAltitudeFeet - (siteElevation / feetToMeter));

            case Meter:
                return (int) Math.round((gpsAltitudeFeet * feetToMeter) - siteElevation);
        }
        return 0;
    }

    public String getHeightUnitIndicator() {
        HeightUnits unit = SysConfig.getHeightUnit();

        switch (unit) {
            case Feet:
                return "ft";

            case Meter:
                return "m";
        }
        return "";
    }

    public String getVerticalRateString(Double vertRateFtMin) {

        if (vertRateFtMin == null) {
            return "";
        }

        VerticalRateUnits unit = SysConfig.getVerticalRateUnit();
        StringBuilder sb = new StringBuilder();

        if (vertRateFtMin > 0) {
            sb.append("+");
        }

        double calculatedValue = 0;
        switch (unit) {
            case FeetPerMinute:
                calculatedValue = vertRateFtMin;
                break;

            case MeterPerSecond:
                calculatedValue = (vertRateFtMin * feetToMeter) / 60.0;
                break;
        }

        sb.append(ToSignificantString(calculatedValue));
        sb.append(" ");
        sb.append(getVertRateUnitIndicator());

        return sb.toString();
    }

    private String ToSignificantString(double val) {

        if(Numbers.isDoubleZero(val)){
            return "0";
        } else if (Math.abs(val) < 10.0) {
            Double doubleNumber = Numbers.round(val, 1);
            return doubleNumber.toString();
        } else {
            Integer intNumber = (int) Math.round(val);
            return intNumber.toString();
        }
    }


    public String getVertRateUnitIndicator() {
        switch (SysConfig.getVerticalRateUnit()) {
            case FeetPerMinute:
                return "ft/m";

            case MeterPerSecond:
                return "m/s";
        }
        return "";
    }
}
