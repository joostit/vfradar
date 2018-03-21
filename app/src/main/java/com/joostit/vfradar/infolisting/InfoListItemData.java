package com.joostit.vfradar.infolisting;

/**
 * Created by Joost on 19-1-2018.
 */

public class InfoListItemData {

    public int trackId;
    public String name = "";
    public String subName = "";
    public String model = "";
    public String altitude = "";
    public String relativeDistance = "";
    public String relativeBearing = "";
    public String vRate = "";

    public String nameType = "";
    public String subNameType = "";

    public boolean hasAdsb;
    public boolean hasOgn;

    public int relativeDegrees = 0;

    public boolean isSelected = false;

    public NotificationCollection notifications = new NotificationCollection();

    public boolean hasNotification(String name) {
        for (InfoListNotification existing : notifications) {
            if (name.equals(existing.name)) {
                return true;
            }
        }
        return false;
    }

}
