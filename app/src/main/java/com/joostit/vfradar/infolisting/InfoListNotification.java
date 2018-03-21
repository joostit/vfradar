package com.joostit.vfradar.infolisting;

import com.joostit.vfradar.geo.geofencing.FenceAlerts;

/**
 * Created by Joost on 2-2-2018.
 */

public class InfoListNotification {


    public String name = "";
    public FenceAlerts notificationType;


    public InfoListNotification() {

    }


    public InfoListNotification(String name, FenceAlerts type) {
        this.name = name;
        this.notificationType = type;
    }


    public boolean isOtherMoreSevere(InfoListNotification other) {

        if (other.notificationType == FenceAlerts.Warning) {
            if (this.notificationType == FenceAlerts.Notification) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
