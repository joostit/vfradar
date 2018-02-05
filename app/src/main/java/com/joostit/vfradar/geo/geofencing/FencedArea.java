package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.GeoAltitude;
import com.joostit.vfradar.geo.GeoObject;

/**
 * Created by Joost on 2-2-2018.
 */

public class FencedArea extends GeoObject {

    public FenceAlerts alertType = FenceAlerts.Notification;
    public GeoAltitude altitude = new GeoAltitude();

    public boolean isInArea(TrackedAircraft ac) {
        boolean retVal;
        if (ac.data.alt != null) {

            boolean isWithinAltitudeBounds;

            if(altitude.hasAltitudeInfo()){
                isWithinAltitudeBounds = (ac.data.alt >= altitude.getBottomFt()) && (ac.data.alt <= altitude.getTopFt());
            }
            else{
                isWithinAltitudeBounds = true;
            }

            if (isWithinAltitudeBounds) {
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
