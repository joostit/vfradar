package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.GeoAltitude;
import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.geo.GeoObjectTypes;

/**
 * Created by Joost on 2-2-2018.
 */

public class FencedArea extends GeoObject {

    private FenceAlerts alertType = FenceAlerts.Notification;
    public GeoAltitude altitude = new GeoAltitude();

    public FencedArea(){
        super(GeoObjectTypes.NotificationArea);
    }

    public void setAlertType(FenceAlerts alertType){
        this.alertType = alertType;

        switch (this.alertType){
            case Notification:
                super.setObjectType(GeoObjectTypes.NotificationArea);
                break;
            case Warning:
                super.setObjectType(GeoObjectTypes.WarningArea);
                break;
            default:
                super.setObjectType(GeoObjectTypes.GenericGeoArea);

        }
    }

    public FenceAlerts getAlerType(){
        return alertType;
    }

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
