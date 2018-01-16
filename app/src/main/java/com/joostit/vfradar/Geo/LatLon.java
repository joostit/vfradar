package com.joostit.vfradar.Geo;

import java.util.Locale;

/**
 * Created by Joost on 16-1-2018.
 */

public class LatLon {

    public double Latitude;
    public double Longitude;

    public LatLon() {
    }

    public LatLon(double lat, double lon) {
        Latitude = lat;
        Longitude = lon;
    }

    @Override
    public String toString() {

        double lat = Math.round(Latitude * 10000) / 10000.0;
        double lon = Math.round(Longitude * 10000) / 10000.0;

        String latStr = String.format(Locale.ROOT, "%.4f", lat);
        String lonStr = String.format(Locale.ROOT, "%.4f", lon);

        return latStr + ", " + lonStr;
    }

    public double DistanceTo(LatLon to){
        return GeoUtils.Distance(this, to);
    }

    public double BearingTo(LatLon to){
        return GeoUtils.Bearing(this, to);
    }

    public LatLon Move(double bearing, double distanceM){
        return GeoUtils.Move(this, bearing, distanceM);
    }
}
