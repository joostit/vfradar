package com.joostit.vfradar;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 20-1-2018.
 */

public class SysConfig {

    //private static final LatLon centerPosition = new LatLon(47.540957, 19.325991);
    private static final LatLon centerPosition = new LatLon(52.278758, 6.899437);

    private static final int maxValidRxAge = 15;

    //private static final String vfrdarCoreDataAddress = "http://192.168.8.210:60002/live/all";
    private static final String vfrdarCoreDataAddress = "http://192.168.178.101:60002/live/all";

    public static LatLon getCenterPosition() {
        return centerPosition;
    }

    public static int getMaxValidRxAge(){
        return maxValidRxAge;
    }

    public static String getVFRadarCoreDataAddress(){
        return vfrdarCoreDataAddress;
    }


}
