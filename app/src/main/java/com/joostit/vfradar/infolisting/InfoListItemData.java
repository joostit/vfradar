package com.joostit.vfradar.infolisting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 19-1-2018.
 */

public class InfoListItemData {

    public int trackId;
    public String name = "";
    public String cn = "";
    public String model = "";
    public String altitude = "";
    public String relativeDistance = "";
    public String relativeBearing = "";
    public String vRate = "";

    public String nameType = "";

    public boolean hasAdsb;
    public boolean hasOgn;

    public int relativeDegrees = 0;

    public boolean isSelected = false;

    public List<InfoListNotification> notifications = new ArrayList<>();

    public boolean hasNotification(String name){
        for(InfoListNotification existing : notifications){
            if(name.equals(existing.name)){
                return true;
            }
        }
        return false;
    }

}
