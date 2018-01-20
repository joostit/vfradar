package com.joostit.vfradar;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 20-1-2018.
 */

public class SysConfig {

    private static final LatLon centerPosition = new LatLon(52.278758, 6.899437);

    public static LatLon getCenterPosition() {
        return centerPosition;
    }


}
