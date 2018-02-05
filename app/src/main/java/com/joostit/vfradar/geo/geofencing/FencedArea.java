package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.GeoShape;

/**
 * Created by Joost on 2-2-2018.
 */

public class FencedArea {

    public String name = "";
    public FenceAlerts alertType = FenceAlerts.Notification;
    public GeoShape shape = new GeoShape();
    public int bottomFt;
    public int topFt;


    public boolean isInArea(TrackedAircraft ac) {
        boolean retVal;
        if (ac.data.alt != null) {
            if ((ac.data.alt >= bottomFt) && (ac.data.alt <= topFt)) {
                retVal = shape.isInshape(ac.data.position);
            } else {
                retVal = false;
            }
        }
        else{
            retVal = false;
        }
        return retVal;
    }


}
