package com.joostit.vfradar.infolisting;

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

}
