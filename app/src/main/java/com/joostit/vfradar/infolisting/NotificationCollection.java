package com.joostit.vfradar.infolisting;

import com.joostit.vfradar.geo.geofencing.FenceAlerts;

import java.util.ArrayList;

/**
 * Created by Joost on 21-3-2018.
 */

public class NotificationCollection extends ArrayList<InfoListNotification> {

    public boolean isNotificationLevel() {

        boolean retVal = false;

        for (InfoListNotification note : this) {
            if (note.notificationType == FenceAlerts.Notification) {
                retVal = true;
                break;
            }

            if (note.notificationType == FenceAlerts.Warning) {
                retVal = false;
                break;
            }
        }

        return retVal;
    }

    public boolean isWarningLevel() {
        for (InfoListNotification note : this) {
            if (note.notificationType == FenceAlerts.Warning) {
                return true;
            }
        }

        return false;
    }

    public FenceAlerts getHighestNotificationLevel() {
        if (isWarningLevel()) {
            return FenceAlerts.Warning;
        }

        if (isNotificationLevel()) {
            return FenceAlerts.Notification;
        }

        return FenceAlerts.None;
    }

}
