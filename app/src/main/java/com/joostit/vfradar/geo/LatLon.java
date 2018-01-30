package com.joostit.vfradar.geo;

import android.nfc.FormatException;

import java.text.NumberFormat;
import java.util.IllegalFormatConversionException;
import java.util.Locale;

/**
 * Created by Joost on 16-1-2018.
 */

public class LatLon {

    public double Latitude;
    public double Longitude;
    private static final String validNumberRegEx = "^\\d+(\\.\\d+)$";
    private static NumberFormat format = NumberFormat.getInstance(Locale.ROOT);

    public LatLon() {
    }

    public LatLon(double lat, double lon) {
        Latitude = lat;
        Longitude = lon;
    }


    @Override
    public String toString() {
        return  String.format(Locale.ROOT, "%f", Latitude) + ", " + String.format(Locale.ROOT, "%f", Longitude);
    }


    public static LatLon parseLatLon(String input) throws NumberFormatException {

        LatLon retVal = new LatLon();
        try {
            String[] parts = input.split(",");

            String latString = parts[0].trim();
            if(latString.matches(validNumberRegEx)) {
                Number latNumber = format.parse(latString);
                retVal.Latitude = latNumber.doubleValue();
            }
            else{
                throw new Exception("Latitude cannot contain non-numerical characters");
            }

            String lonString = parts[1].trim();
            if(lonString.matches(validNumberRegEx)){
                Number lonNumber = format.parse(lonString);
                retVal.Longitude = lonNumber.doubleValue();
            }
            else{
                throw new Exception("Longitude cannot contain non-numerical characters");
            }

        }catch (Exception e){
            throw new NumberFormatException("Cannot parse [" + input + "] to a LatLon object");
        }

        return retVal;
    }

    public String toFriendlyString() {

        double lat = Math.round(Latitude * 1000000) / 1000000.0;
        double lon = Math.round(Longitude * 1000000) / 1000000.0;

        String latStr = String.format(Locale.ROOT, "%.6f", lat);
        String lonStr = String.format(Locale.ROOT, "%.6f", lon);

        return latStr + ", " + lonStr;
    }

    public double DistanceTo(LatLon to) {
        return GeoUtils.Distance(this, to);
    }

    public double BearingTo(LatLon to) {
        return GeoUtils.Bearing(this, to);
    }

    public LatLon Move(double bearing, double distanceM) {
        return GeoUtils.Move(this, bearing, distanceM);
    }
}
