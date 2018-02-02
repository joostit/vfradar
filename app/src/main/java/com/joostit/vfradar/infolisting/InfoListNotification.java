package com.joostit.vfradar.infolisting;

import com.joostit.vfradar.geofencing.FenceAlerts;

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
}
