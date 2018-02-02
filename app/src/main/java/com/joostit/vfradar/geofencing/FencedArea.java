package com.joostit.vfradar.geofencing;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 2-2-2018.
 */

public class FencedArea {

    public String name = "";
    public FenceAlerts alertType = FenceAlerts.Notification;
    public List<LatLon> polygon = new ArrayList<>();
    public int bottomFt;
    public int topFt;


    public boolean isInArea(TrackedAircraft ac) {
        boolean retVal;
        if ((ac.data.alt > bottomFt) && (ac.data.alt < topFt)) {
            retVal = isInPolygon(ac.data.position);
        } else {
            retVal = false;
        }
        return retVal;
    }

    private boolean isInPolygon(LatLon position) {
        int i, j;
        boolean isInside = false;
        int sides = polygon.size();
        for (i = 0, j = sides - 1; i < sides; j = i++) {

            if (
                    (
                            (
                                    (polygon.get(i).longitude <= position.longitude) && (position.longitude < polygon.get(j).longitude)
                            ) || (
                                    (polygon.get(j).longitude <= position.longitude) && (position.longitude < polygon.get(i).longitude)
                            )
                    ) &&
                            (position.latitude < (polygon.get(j).latitude - polygon.get(i).latitude) * (position.longitude - polygon.get(i).longitude) / (polygon.get(j).longitude - polygon.get(i).longitude) + polygon.get(i).latitude)
                    ) {
                isInside = !isInside;
            }
        }
        return isInside;

    }

}
