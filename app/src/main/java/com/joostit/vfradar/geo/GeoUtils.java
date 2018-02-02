package com.joostit.vfradar.geo;

/**
 * Created by Joost on 16-1-2018.
 */

public class GeoUtils {

    /**
     * Calculate distance between two shape in latitude and longitude.
     * Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point
     *
     * @returns Distance in Meters
     */
    public static double Distance(LatLon pos1, LatLon pos2) {
        return Distance(pos1, 0, pos2, 0);
    }

    /**
     * Calculate distance between two shape in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double Distance(LatLon pos1, double el1, LatLon pos2, double el2) {

        double lat1 = pos1.latitude;
        double lon1 = pos1.longitude;
        double lat2 = pos2.latitude;
        double lon2 = pos2.longitude;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static double Bearing(LatLon pos1, LatLon pos2) {
        double longitude1 = pos1.longitude;
        double longitude2 = pos2.longitude;
        double latitude1 = Math.toRadians(pos1.latitude);
        double latitude2 = Math.toRadians(pos2.latitude);
        double longDiff = Math.toRadians(longitude2 - longitude1);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    public static LatLon Move(LatLon start, double bearing, double distanceM) {
        double lat1 = Math.toRadians(start.latitude);
        double lng1 = Math.toRadians(start.longitude);
        double brng = Math.toRadians(bearing);

        double R = 6371000;
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distanceM / R) +
                Math.cos(lat1) * Math.sin(distanceM / R) * Math.cos(brng));
        double lng2 = lng1 + Math.atan2(Math.sin(brng) * Math.sin(distanceM / R) * Math.cos(lat1),
                Math.cos(distanceM / R) - Math.sin(lat1) * Math.sin(lat2));

        LatLon result = new LatLon(Math.toDegrees(lat2), Math.toDegrees(lng2));

        return result;
    }
}
